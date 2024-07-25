package com.mongodb;

import java.net.InetAddress;
import java.net.UnknownHostException;

/* loaded from: classes.dex */
public class DBAddress extends ServerAddress {
    final String _db;

    public DBAddress(String urlFormat) throws UnknownHostException {
        super(_getHostSection(urlFormat));
        _check(urlFormat, "urlFormat");
        this._db = _fixName(_getDBSection(urlFormat));
        _check(this._host, "host");
        _check(this._db, "db");
    }

    static String _getHostSection(String urlFormat) {
        if (urlFormat == null) {
            throw new NullPointerException("urlFormat can't be null");
        }
        int idx = urlFormat.indexOf("/");
        if (idx >= 0) {
            return urlFormat.substring(0, idx);
        }
        return null;
    }

    static String _getDBSection(String urlFormat) {
        if (urlFormat == null) {
            throw new NullPointerException("urlFormat can't be null");
        }
        int idx = urlFormat.indexOf("/");
        if (idx >= 0) {
            return urlFormat.substring(idx + 1);
        }
        return urlFormat;
    }

    static String _fixName(String name) {
        return name.replace('.', '-');
    }

    public DBAddress(DBAddress other, String dbname) throws UnknownHostException {
        this(other._host, other._port, dbname);
    }

    public DBAddress(String host, String dbname) throws UnknownHostException {
        this(host, DBPort.PORT, dbname);
    }

    public DBAddress(String host, int port, String dbname) throws UnknownHostException {
        super(host, port);
        this._db = dbname.trim();
    }

    public DBAddress(InetAddress addr, int port, String dbname) {
        super(addr, port);
        _check(dbname, "name");
        this._db = dbname.trim();
    }

    static void _check(String thing, String name) {
        if (thing == null) {
            throw new NullPointerException(name + " can't be null ");
        }
        if (thing.trim().length() == 0) {
            throw new IllegalArgumentException(name + " can't be empty");
        }
    }

    @Override // com.mongodb.ServerAddress
    public int hashCode() {
        return super.hashCode() + this._db.hashCode();
    }

    @Override // com.mongodb.ServerAddress
    public boolean equals(Object other) {
        if (other instanceof DBAddress) {
            DBAddress a = (DBAddress) other;
            return a._port == this._port && a._db.equals(this._db) && a._host.equals(this._host);
        }
        if (other instanceof ServerAddress) {
            return other.equals(this);
        }
        return false;
    }

    public DBAddress getSister(String name) {
        try {
            return new DBAddress(this._host, this._port, name);
        } catch (UnknownHostException uh) {
            throw new MongoInternalException("shouldn't be possible", uh);
        }
    }

    public String getDBName() {
        return this._db;
    }

    @Override // com.mongodb.ServerAddress
    public String toString() {
        return super.toString() + "/" + this._db;
    }
}
