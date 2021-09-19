package com.toxi.net;

import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class UDPSyncClient extends Thread {
    private ClientListener listener;

    protected String serverName;
    protected int port;
    protected int clientID;
    protected int ttl;

    private int frameCount;
    protected long lastUpdate;

    private static final Logger logger = Logger.getLogger(UDPSyncClient.class
            .getName());

    UDPSyncClient(String serverName, int port, int clientID, int ttl) {
        this.serverName = serverName;
        this.port = port;
        this.clientID = clientID;
        this.ttl = ttl;
    }

    @Override
    public void run() {
        DatagramSocket clientSocket = null;
        lastUpdate = System.currentTimeMillis();
        try {
            clientSocket = new DatagramSocket();
            InetAddress ip = InetAddress.getByName(serverName);
            byte[] sendData = new byte[] { 1 };
            byte[] receiveData = new byte[8];
            // do initial sign on by sending a message to the server
            DatagramPacket sendPacket = new DatagramPacket(sendData,
                    sendData.length, ip, port);
            clientSocket.send(sendPacket);
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData,
                        receiveData.length);
                clientSocket.receive(receivePacket);
                String payload = new String(receivePacket.getData(), 0,
                        receivePacket.getLength());
                if (logger != null)
                    logger.finest(payload);
                int newFrameCount = Integer.parseInt(payload);
                if (newFrameCount > 0) {
                    while (frameCount < newFrameCount) {
                        if (listener != null)
                            listener.triggerUpdate();
                        frameCount++;
                    }
                }
                if (listener != null)
                    listener.triggerFrame();
                long now = System.currentTimeMillis();
                if (now - lastUpdate > ttl) {
                    sendPacket = new DatagramPacket(sendData, sendData.length,
                            ip, port);
                    clientSocket.send(sendPacket);
                    lastUpdate = now;
                    if (logger != null)
                        logger.log(Level.FINER, "TTL updated on: {0}", lastUpdate);
                }
            }
        } catch (IOException | NumberFormatException ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            if (clientSocket != null)
                clientSocket.close();
        }
    }

    public void setListener(ClientListener l) {
        listener = l;
    }
}