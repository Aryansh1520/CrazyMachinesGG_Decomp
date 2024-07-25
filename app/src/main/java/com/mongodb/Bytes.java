package com.mongodb;

import java.nio.ByteOrder;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.bson.BSON;
import org.bson.types.BSONTimestamp;
import org.bson.types.Code;
import org.bson.types.CodeWScope;
import org.bson.types.ObjectId;

/* loaded from: classes.dex */
public class Bytes extends BSON {
    static final int BATCH_INSERT_SIZE = 8388608;
    static final ObjectId COLLECTION_REF_ID;
    static final int CONNECTIONS_PER_HOST;
    static final int MAX_OBJECT_SIZE = 4194304;
    public static final ByteOrder ORDER;
    public static final int QUERYOPTION_AWAITDATA = 32;
    public static final int QUERYOPTION_EXHAUST = 64;
    public static final int QUERYOPTION_NOTIMEOUT = 16;
    public static final int QUERYOPTION_OPLOGREPLAY = 8;
    public static final int QUERYOPTION_PARTIAL = 128;
    public static final int QUERYOPTION_SLAVEOK = 4;
    public static final int QUERYOPTION_TAILABLE = 2;
    public static final int RESULTFLAG_AWAITCAPABLE = 8;
    public static final int RESULTFLAG_CURSORNOTFOUND = 1;
    public static final int RESULTFLAG_ERRSET = 2;
    public static final int RESULTFLAG_SHARDCONFIGSTALE = 4;
    static final Logger LOGGER = Logger.getLogger("com.mongodb");
    static final boolean D = Boolean.getBoolean("DEBUG.MONGO");

    static {
        if (LOGGER.getLevel() == null) {
            if (D) {
                LOGGER.setLevel(Level.ALL);
            } else {
                LOGGER.setLevel(Level.WARNING);
            }
        }
        ORDER = ByteOrder.LITTLE_ENDIAN;
        CONNECTIONS_PER_HOST = Integer.parseInt(System.getProperty("MONGO.POOLSIZE", "10"));
        COLLECTION_REF_ID = new ObjectId(-1, -1, -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class OptionHolder {
        final OptionHolder _parent;
        int _options = 0;
        boolean _hasOptions = false;

        /* JADX INFO: Access modifiers changed from: package-private */
        public OptionHolder(OptionHolder parent) {
            this._parent = parent;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void set(int options) {
            this._options = options;
            this._hasOptions = true;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int get() {
            if (this._hasOptions) {
                return this._options;
            }
            if (this._parent == null) {
                return 0;
            }
            return this._parent.get();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void add(int option) {
            set(get() | option);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void reset() {
            this._hasOptions = false;
        }
    }

    public static byte getType(Object o) {
        if (o == null) {
            return (byte) 10;
        }
        if (o instanceof DBPointer) {
            return BSON.REF;
        }
        if ((o instanceof Integer) || (o instanceof Short) || (o instanceof Byte) || (o instanceof AtomicInteger)) {
            return BSON.NUMBER_INT;
        }
        if ((o instanceof Long) || (o instanceof AtomicLong)) {
            return BSON.NUMBER_LONG;
        }
        if (o instanceof Number) {
            return (byte) 1;
        }
        if (o instanceof String) {
            return (byte) 2;
        }
        if (o instanceof List) {
            return (byte) 4;
        }
        if (o instanceof byte[]) {
            return (byte) 5;
        }
        if (o instanceof ObjectId) {
            return (byte) 7;
        }
        if (o instanceof Boolean) {
            return (byte) 8;
        }
        if (o instanceof Date) {
            return (byte) 9;
        }
        if (o instanceof BSONTimestamp) {
            return BSON.TIMESTAMP;
        }
        if (o instanceof Pattern) {
            return BSON.REGEX;
        }
        if ((o instanceof DBObject) || (o instanceof DBRefBase)) {
            return (byte) 3;
        }
        if (o instanceof Code) {
            return BSON.CODE;
        }
        if (o instanceof CodeWScope) {
            return BSON.CODE_W_SCOPE;
        }
        return (byte) -1;
    }
}
