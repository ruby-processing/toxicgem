package com.toxi.net;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import toxi.util.datatypes.TypedProperties;

/**
 * <p>
 * An <a href="http://mostpixelsever.com">MPE</a> inspired synch server for
 * networked multi-screen applications. All communication is realised via UDP.
 * Clients are not requested to acknowledge every single packet received, but
 * need to send a heart beat in a regular (configurable) interval. The server
 * discards any data received from clients, so it doesn't matter what is being
 * sent back. A single byte will suffice.
 * </p>
 * 
 * <p>
 * The server can be configured via CLI arguments and/or Java property files and
 * can be connected to an existing logger. When run on from the commandline a
 * console logger is automatically added.
 * </p>
 * 
 * @author Karsten Schmidt <info@postspectacular.com>
 */
public class UDPSyncServer {

    private static final int DEFAULT_RECEIVE_PACKET_SIZE = 32;

    private static final int DEFAULT_NUM_CLIENTS = 1;

    private static final int DEFAULT_FRAMERATE = 30;

    private static final int DEFAULT_PORT = 9002;

    private static final int DEFAULT_RECEIVE_TIMEOUT = 1;

    private ServerState state;

    DatagramSocket socket;
    HashMap<String, UDPConnection> connections = new HashMap<>();

    /**
     *
     */
    @Option(name = "-port", aliases = "-p", usage = "server socket port number")
    protected int port = DEFAULT_PORT;

    @Option(name = "-config", aliases = "-c", usage = "path to configuration file", metaVar = "PATH")
    private String configFile;
    private TypedProperties config;

    @Option(name = "-packetsize", aliases = "-s", usage = "receive packet size")
    private int receivePacketSize = DEFAULT_RECEIVE_PACKET_SIZE;
    private byte[] receiveData;

    /**
     *
     */
    @Option(name = "-rectimeout", aliases = "-rt", usage = "receive timeout (in ms), minimum 1ms")
    protected int receiveTimeOut = DEFAULT_RECEIVE_TIMEOUT;

    /**
     *
     */
    @Option(name = "-ttl", usage = "client time-to-live, max. time interval after which clients expire")
    protected int connectionTimeOut = UDPConnection.TTL;

    /**
     *
     */
    @Option(name = "-numclients", aliases = "-num", usage = "number of clients")
    protected int numClients = DEFAULT_NUM_CLIENTS;

    /**
     *
     */
    @Option(name = "-framerate", aliases = "-fps", usage = "target framerate")
    protected int frameRate = DEFAULT_FRAMERATE;

    /**
     *
     */
    protected int frameDuration;

    /**
     *
     */
    protected int frameCount;

    private static Logger LOGGER;

    /**
     * Optional server event listener
     */
    protected ServerListener listener;

    /**
     *
     */
    public UDPSyncServer() {
        config = new TypedProperties();
        setMaxReceivePacketSize(DEFAULT_RECEIVE_PACKET_SIZE);
    }

    /**
     *
     * @param args
     */
    public static void main(String args[]) {
        new UDPSyncServer().execute(args);
    }

    /**
     * Main entry point for CLI.
     * 
     * @param args
     *            command line arguments
     */
    public void execute(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        parser.setUsageWidth(80);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java UDPSyncServer [options]");
            parser.printUsage(System.err);
            System.err.println();
            return;
        }

        try {
            if (configFile != null)
                config.load(new FileInputStream(configFile));
            LOGGER = Logger.getLogger("com.postspectacular");
            LOGGER.addHandler(new ConsoleHandler());
            LOGGER.setLevel(Level.CONFIG);
            configure(config);
            run();
        } catch (FileNotFoundException e) {
            System.err.println("cannot find config file @ " + args[0]);
        } catch (IOException e) {
            System.err.println("cannot read config file @ " + args[0]);
        }
    }

    /**
     * Configures the server with settings using the passed-in properties.
     * 
     * @param config
     */
    public void configure(TypedProperties config) {
        this.config = config;
        port = config.getInt("server.port", port);
        setMaxReceivePacketSize(config.getInt("udp.packetsize",
                receivePacketSize));
        numClients = config.getInt("server.numclients", numClients);
        frameRate = config.getInt("server.framerate", frameRate);
        frameDuration = 1000 / frameRate;
        connectionTimeOut = config.getInt("udp.ttl", connectionTimeOut);
        UDPConnection.setTTL(connectionTimeOut);
        receiveTimeOut = config.getInt("udp.receivetimeout", receiveTimeOut);
        if (LOGGER != null) {
            LOGGER.log(Level.INFO, "configured server... port:{0} clients:{1} fps:{2} ttl:{3} rto:{4}", new Object[]{port, numClients, frameRate, connectionTimeOut, receiveTimeOut});
        }
        setState(ServerState.WAITING_FOR_CLIENTS);
    }

    /**
     * Main server loop/state machine. Creates socket and handles syncing of
     * connected clients.
     */
    public void run() {
        try {
            socket = new DatagramSocket(port);
            if (LOGGER != null)
                LOGGER.log(Level.INFO, "creating socket @ port {0}", port);
            if (listener != null) {
                listener.serverStarted();
            }
            while (true) {
                switch (state) {

                    case WAITING_FOR_CLIENTS:
                        if (LOGGER != null)
                            LOGGER.info("Server running, waiting for connections...");
                        // disable timeout, i.e socket will block indefinitely
                        // until
                        // a packet is received
                        socket.setSoTimeout(0);
                        while (connections.size() < numClients) {
                            receiveAndAddConnection();
                        }
                        // all connected, start syncing...
                        setState(ServerState.SYNCHING);
                        break;

                    case SYNCHING:
                        socket.setSoTimeout(receiveTimeOut);
                        if (connections.size() > 0) {
                            doHeartBeat();
                            byte[] payload = getSyncPayload();
                            Iterator<UDPConnection> iter = connections.values()
                                    .iterator();
                            // Uses nanoTime for better precision / more stable
                            // framerate, but unavailable on OSX!!!
                            long beginSynch = System.nanoTime();
                            while (iter.hasNext()) {
                                UDPConnection conn = iter.next();
                                if (!conn.isAlive()) {
                                    iter.remove();
                                    if (LOGGER != null)
                                        LOGGER.log(Level.WARNING, "{0} disconnected", conn);
                                    if (listener != null)
                                        listener.clientDisconnected(conn);
                                }
                                conn.send(socket, payload);
                            }
                            int returnCount = 0;
                            while (returnCount < numClients
                                    && returnCount < connections.size() + 1) {
                                try {
                                    DatagramPacket receivePacket = new DatagramPacket(
                                            receiveData, receiveData.length);
                                    socket.receive(receivePacket);
                                    InetAddress ip = receivePacket.getAddress();
                                    int portDP = receivePacket.getPort();
                                    String connID = UDPConnection.buildHash(ip,
                                            portDP);
                                    UDPConnection conn = connections
                                            .get(connID);
                                    if (conn != null) {
                                        conn.update();
                                        returnCount++;
                                    } else {
                                        // renewed connection
                                        conn = new UDPConnection(ip, portDP);
                                        conn.send(socket, getSyncPayload());
                                        connections.put(connID, conn);
                                        if (LOGGER != null)
                                            LOGGER.log(Level.FINE, "re-adding connection: {0}", conn);
                                    }
                                    if (listener != null)
                                        listener.clientUpdated(conn,
                                                receivePacket);
                                } catch (SocketTimeoutException e) {
                                    // no further packets available
                                    break;
                                }
                            }
                            long endSynch = System.nanoTime();
                            long delta = (endSynch - beginSynch) / 1000000;
                            if (delta < frameDuration) {
                                int sleep = frameDuration - (int) delta;
                                if (LOGGER != null)
                                    LOGGER.log(Level.FINEST, "sleeping: {0}", sleep);
                                Thread.sleep(sleep);
                            }
                        } else {
                            if (LOGGER != null)
                                LOGGER.info("all clients disconnected");
                            setState(ServerState.WAITING_FOR_CLIENTS);
                        }
                }
            }
        } catch (IOException | InterruptedException e) {
            handleException(e);
        } finally {
            if (LOGGER != null)
                LOGGER.info("server shutting down...");
            if (socket != null)
                socket.close();
        }
    }

    /**
     * Attempts to receive data sent from clients to confirm they're still
     * connected. The timeout for this action is set to 1ms by default, so
     * currently not more than 25 machines can be reliably connected & synched
     * at 25fps.
     * 
     * If a connection times out it's removed from the list of active clients.
     * On the other hand if a new connection hash is found we know a client has
     * reconnected and will be added back to the pool.
     */
    protected void receiveAndAddConnection() {
        try {
            DatagramPacket receivePacket = new DatagramPacket(receiveData,
                    receiveData.length);
            if (LOGGER != null)
                LOGGER.log(Level.INFO, "waiting for {0} more clients to reconnect...", (numClients - connections.size()));
            socket.receive(receivePacket);
            InetAddress ip = receivePacket.getAddress();
            int portDP = receivePacket.getPort();
            String connID = UDPConnection.buildHash(ip, portDP);
            if (connections.get(connID) == null) {
                if (connections.isEmpty()) {
                    frameCount = 0;
                    if (LOGGER != null)
                        LOGGER.info("resetting frame count");
                }
                UDPConnection conn = new UDPConnection(ip, portDP);
                connections.put(connID, conn);
                conn.send(socket, getSyncPayload());
                if (LOGGER != null)
                    LOGGER.log(Level.FINE, "added new connection: {0}", conn);
                if (listener != null) {
                    listener.clientConnected(conn);
                }
            }
        } catch (SocketTimeoutException e) {
        } catch (IOException e) {
            handleException(e);
        }
    }

    private void handleException(Exception e) {
        if (listener != null)
            listener.serverError(e);
        if (LOGGER != null)
            LOGGER.log(Level.SEVERE, "Server error", e);
        else
            e.printStackTrace();
    }

    /**
     * Updates the internal state of the heartbeat info sent out later.
     */
    protected void doHeartBeat() {
        frameCount++;
    }

    /**
     * Collects and formats payload data sent via UDP to all clients. By default
     * only the current frame number is sent. If more data is required you can
     * either overwrite this method or append data via an attached
     * {@link ServerListener}.
     * 
     * @return payload data as byte array
     */
    protected byte[] getSyncPayload() {
        StringBuilder sb = new StringBuilder();
        sb.append(frameCount);
        byte[] defaultPayload = sb.toString().getBytes();
        if (listener != null) {
            byte[] userPayload = listener.getSyncPayload();
            if (userPayload != null) {
                byte[] combinedPayload = new byte[defaultPayload.length
                        + userPayload.length];
                System.arraycopy(defaultPayload, 0, combinedPayload, 0,
                        defaultPayload.length);
                System.arraycopy(userPayload, 0, combinedPayload,
                        defaultPayload.length, userPayload.length);
                return combinedPayload;
            }
        }
        return defaultPayload;
    }

    /**
     * Triggers and logs a new server state. If present, this will also notify
     * the {@link ServerListener}.
     * 
     * @param s
     *            new server state
     */
    private void setState(ServerState s) {
        state = s;
        if (LOGGER != null)
            LOGGER.log(Level.CONFIG, "new server state: {0}", state);
        if (listener != null)
            listener.serverStateChanged(s);
    }

    /**
     * @return Maximum size of UDP packets the server is able to receive
     */
    public int getMaxPacketSize() {
        return receiveData.length;
    }

    /**
     * Sets the max. size of UDP packets the server is able to receive
     * 
     * @param size
     */
    public final void setMaxReceivePacketSize(int size) {
        receivePacketSize = size;
        if (receiveData == null) {
            receiveData = new byte[size];
        } else if (receiveData.length != size) {
            synchronized (receiveData) {
                receiveData = new byte[size];
            }
        }
    }

    /**
     * @return configured number of clients the server is expecting and
     *         initially waiting for to connect before any syncing begins.
     */
    public int getNumClients() {
        return numClients;
    }

    /**
     * Sets the number of clients the server is expecting and initially waiting
     * for to connect before any syncing begins.
     * 
     * @param numClients
     */
    public void setNumClients(int numClients) {
        this.numClients = numClients;
    }

    /**
     * Attaches a logger to the server.
     * 
     * @param logger
     */
    public void setLogger(Logger logger) {
        UDPSyncServer.LOGGER = logger;
    }

    /**
     * Attaches an event listener to the server.
     * 
     * @param listener
     */
    public void setListener(ServerListener listener) {
        this.listener = listener;
        if (LOGGER != null)
            LOGGER.log(Level.INFO, "new server listener: {0}", listener);
    }
}