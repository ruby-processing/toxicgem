package com.toxi.nio;

import java.net.SocketAddress;

/**
 *
 * @author tux
 */
public class UDPClientState {
    SocketAddress addr;
    boolean isReady;
    long lastUpdate;

    /**
     *
     * @param a
     */
    public UDPClientState(SocketAddress a) {
        addr = a;
        isReady = true;
        lastUpdate = System.currentTimeMillis();
        System.out.println("new client: " + addr);
    }

    /**
     *
     * @return
     */
    public SocketAddress getAddress() {
        return addr;
    }
}
