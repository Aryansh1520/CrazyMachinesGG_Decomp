package com.mongodb;

import com.mappn.sdk.common.utils.BaseConstants;
import com.mappn.sdk.pay.util.Constants;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/* loaded from: classes.dex */
public class MongoURI {
    static final Logger LOGGER = Logger.getLogger("com.mongodb.MongoURI");
    public static final String MONGODB_PREFIX = "mongodb://";
    final String _collection;
    final String _database;
    final List<String> _hosts;
    final MongoOptions _options = new MongoOptions();
    final char[] _password;
    final String _uri;
    final String _username;

    public MongoURI(String uri) {
        String serverPart;
        String nsPart;
        String optionsPart;
        this._uri = uri;
        if (!uri.startsWith(MONGODB_PREFIX)) {
            throw new IllegalArgumentException("uri needs to start with mongodb://");
        }
        String uri2 = uri.substring(MONGODB_PREFIX.length());
        int idx = uri2.lastIndexOf("/");
        if (idx < 0) {
            serverPart = uri2;
            nsPart = null;
            optionsPart = null;
        } else {
            serverPart = uri2.substring(0, idx);
            nsPart = uri2.substring(idx + 1);
            int idx2 = nsPart.indexOf("?");
            if (idx2 >= 0) {
                optionsPart = nsPart.substring(idx2 + 1);
                nsPart = nsPart.substring(0, idx2);
            } else {
                optionsPart = null;
            }
        }
        List<String> all = new LinkedList<>();
        int idx3 = serverPart.indexOf("@");
        if (idx3 > 0) {
            String authPart = serverPart.substring(0, idx3);
            serverPart = serverPart.substring(idx3 + 1);
            int idx4 = authPart.indexOf(":");
            this._username = authPart.substring(0, idx4);
            this._password = authPart.substring(idx4 + 1).toCharArray();
        } else {
            this._username = null;
            this._password = null;
        }
        String[] arr$ = serverPart.split(Constants.TERM);
        for (String s : arr$) {
            all.add(s);
        }
        this._hosts = Collections.unmodifiableList(all);
        if (nsPart != null) {
            int idx5 = nsPart.indexOf(".");
            if (idx5 < 0) {
                this._database = nsPart;
                this._collection = null;
            } else {
                this._database = nsPart.substring(0, idx5);
                this._collection = nsPart.substring(idx5 + 1);
            }
        } else {
            this._database = null;
            this._collection = null;
        }
        if (optionsPart == null || optionsPart.length() <= 0) {
            return;
        }
        parseOptions(optionsPart);
    }

    private void parseOptions(String optionsPart) {
        String readPreferenceType = null;
        DBObject firstTagSet = null;
        List<DBObject> remainingTagSets = new ArrayList<>();
        String[] arr$ = optionsPart.split("&|;");
        for (String _part : arr$) {
            int idx = _part.indexOf("=");
            if (idx >= 0) {
                String key = _part.substring(0, idx).toLowerCase();
                String value = _part.substring(idx + 1);
                if (key.equals("maxpoolsize")) {
                    this._options.connectionsPerHost = Integer.parseInt(value);
                } else if (key.equals("minpoolsize")) {
                    LOGGER.warning("Currently No support in Java driver for Min Pool Size.");
                } else if (key.equals("waitqueuemultiple")) {
                    this._options.threadsAllowedToBlockForConnectionMultiplier = Integer.parseInt(value);
                } else if (key.equals("waitqueuetimeoutms")) {
                    this._options.maxWaitTime = Integer.parseInt(value);
                } else if (key.equals("connecttimeoutms")) {
                    this._options.connectTimeout = Integer.parseInt(value);
                } else if (key.equals("sockettimeoutms")) {
                    this._options.socketTimeout = Integer.parseInt(value);
                } else if (key.equals("autoconnectretry")) {
                    this._options.autoConnectRetry = _parseBoolean(value);
                } else if (key.equals("slaveok")) {
                    this._options.slaveOk = _parseBoolean(value);
                } else if (key.equals("safe")) {
                    this._options.safe = _parseBoolean(value);
                } else if (key.equals("w")) {
                    this._options.w = Integer.parseInt(value);
                } else if (key.equals("wtimeout")) {
                    this._options.wtimeout = Integer.parseInt(value);
                } else if (key.equals("fsync")) {
                    this._options.fsync = _parseBoolean(value);
                } else if (key.equals("readpreference")) {
                    readPreferenceType = value;
                } else if (key.equals("readpreferencetags")) {
                    DBObject tagSet = getTagSet(value.trim());
                    if (firstTagSet == null) {
                        firstTagSet = tagSet;
                    } else {
                        remainingTagSets.add(tagSet);
                    }
                } else {
                    LOGGER.warning("Unknown or Unsupported Option '" + key + "'");
                }
            }
        }
        if (readPreferenceType != null) {
            if (firstTagSet == null) {
                this._options.readPreference = ReadPreference.valueOf(readPreferenceType);
            } else {
                this._options.readPreference = ReadPreference.valueOf(readPreferenceType, firstTagSet, (DBObject[]) remainingTagSets.toArray(new DBObject[remainingTagSets.size()]));
            }
        }
    }

    private DBObject getTagSet(String tagSetString) {
        DBObject tagSet = new BasicDBObject();
        if (tagSetString.length() > 0) {
            String[] arr$ = tagSetString.split(Constants.TERM);
            for (String tag : arr$) {
                String[] tagKeyValuePair = tag.split(":");
                if (tagKeyValuePair.length != 2) {
                    throw new IllegalArgumentException("Bad read preference tags: " + tagSetString);
                }
                tagSet.put(tagKeyValuePair[0].trim(), tagKeyValuePair[1].trim());
            }
        }
        return tagSet;
    }

    boolean _parseBoolean(String _in) {
        String in = _in.trim();
        return in != null && in.length() > 0 && (in.equals(BaseConstants.DEFAULT_UC_CNO) || in.toLowerCase().equals("true") || in.toLowerCase().equals("yes"));
    }

    public String getUsername() {
        return this._username;
    }

    public char[] getPassword() {
        return this._password;
    }

    public List<String> getHosts() {
        return this._hosts;
    }

    public String getDatabase() {
        return this._database;
    }

    public String getCollection() {
        return this._collection;
    }

    public MongoOptions getOptions() {
        return this._options;
    }

    public Mongo connect() throws UnknownHostException {
        return new Mongo(this);
    }

    public DB connectDB() throws UnknownHostException {
        return connect().getDB(this._database);
    }

    public DB connectDB(Mongo m) {
        return m.getDB(this._database);
    }

    public DBCollection connectCollection(DB db) {
        return db.getCollection(this._collection);
    }

    public DBCollection connectCollection(Mongo m) {
        return connectDB(m).getCollection(this._collection);
    }

    public String toString() {
        return this._uri;
    }
}
