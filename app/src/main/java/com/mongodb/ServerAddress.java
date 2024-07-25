package com.mongodb;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/* loaded from: classes.dex */
public class ServerAddress {
    volatile InetSocketAddress _address;
    final String _host;
    final int _port;

    public ServerAddress() throws UnknownHostException {
        this(defaultHost(), defaultPort());
    }

    public ServerAddress(String host) throws UnknownHostException {
        this(host, defaultPort());
    }

    public ServerAddress(String host, int port) throws UnknownHostException {
        String host2 = (host == null ? defaultHost() : host).trim();
        host2 = host2.length() == 0 ? defaultHost() : host2;
        int idx = host2.indexOf(":");
        if (idx > 0) {
            if (port != defaultPort()) {
                throw new IllegalArgumentException("can't specify port in construct and via host");
            }
            port = Integer.parseInt(host2.substring(idx + 1));
            host2 = host2.substring(0, idx).trim();
        }
        this._host = host2;
        this._port = port;
        updateInetAddress();
    }

    public ServerAddress(InetAddress addr) {
        this(new InetSocketAddress(addr, defaultPort()));
    }

    public ServerAddress(InetAddress addr, int port) {
        this(new InetSocketAddress(addr, port));
    }

    public ServerAddress(InetSocketAddress addr) {
        this._address = addr;
        this._host = this._address.getHostName();
        this._port = this._address.getPort();
    }

    public boolean sameHost(String host) {
        int idx = host.indexOf(":");
        int port = defaultPort();
        if (idx > 0) {
            port = Integer.parseInt(host.substring(idx + 1));
            host = host.substring(0, idx);
        }
        return this._port == port && this._host.equalsIgnoreCase(host);
    }

    public boolean equals(Object other) {
        if (other instanceof ServerAddress) {
            ServerAddress a = (ServerAddress) other;
            return a._port == this._port && a._host.equals(this._host);
        }
        if (other instanceof InetSocketAddress) {
            return this._address.equals(other);
        }
        return false;
    }

    public int hashCode() {
        return this._host.hashCode() + this._port;
    }

    public String getHost() {
        return this._host;
    }

    public int getPort() {
        return this._port;
    }

    public InetSocketAddress getSocketAddress() {
        return this._address;
    }

    public String toString() {
        return this._address.toString();
    }

    public static String defaultHost() {
        return "127.0.0.1";
    }

    public static int defaultPort() {
        return DBPort.PORT;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateInetAddress() throws UnknownHostException {
        InetSocketAddress oldAddress = this._address;
        this._address = new InetSocketAddress(InetAddress.getByName(this._host), this._port);
        return !this._address.equals(oldAddress);
    }
}
