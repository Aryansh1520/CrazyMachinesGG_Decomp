package com.mongodb;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class WriteConcern implements Serializable {
    private static final long serialVersionUID = 1884671104750417011L;
    boolean _continueOnErrorForInsert;
    boolean _fsync;
    boolean _j;
    Object _w;
    int _wtimeout;
    public static final WriteConcern NONE = new WriteConcern(-1);
    public static final WriteConcern NORMAL = new WriteConcern(0);
    public static final WriteConcern SAFE = new WriteConcern(1);
    public static final WriteConcern MAJORITY = new Majority();
    public static final WriteConcern FSYNC_SAFE = new WriteConcern(true);
    public static final WriteConcern JOURNAL_SAFE = new WriteConcern(1, 0, false, true);
    public static final WriteConcern REPLICAS_SAFE = new WriteConcern(2);
    private static Map<String, WriteConcern> _namedConcerns = null;

    public WriteConcern() {
        this(0);
    }

    public WriteConcern(int w) {
        this(w, 0, false);
    }

    public WriteConcern(String w) {
        this(w, 0, false, false);
    }

    public WriteConcern(int w, int wtimeout) {
        this(w, wtimeout, false);
    }

    public WriteConcern(boolean fsync) {
        this(1, 0, fsync);
    }

    public WriteConcern(int w, int wtimeout, boolean fsync) {
        this(w, wtimeout, fsync, false);
    }

    public WriteConcern(int w, int wtimeout, boolean fsync, boolean j) {
        this(w, wtimeout, fsync, j, false);
    }

    public WriteConcern(int w, int wtimeout, boolean fsync, boolean j, boolean continueOnInsertError) {
        this._w = 0;
        this._wtimeout = 0;
        this._fsync = false;
        this._j = false;
        this._continueOnErrorForInsert = false;
        this._w = Integer.valueOf(w);
        this._wtimeout = wtimeout;
        this._fsync = fsync;
        this._j = j;
        this._continueOnErrorForInsert = continueOnInsertError;
    }

    public WriteConcern(String w, int wtimeout, boolean fsync, boolean j) {
        this(w, wtimeout, fsync, j, false);
    }

    public WriteConcern(String w, int wtimeout, boolean fsync, boolean j, boolean continueOnInsertError) {
        this._w = 0;
        this._wtimeout = 0;
        this._fsync = false;
        this._j = false;
        this._continueOnErrorForInsert = false;
        if (w == null) {
            throw new IllegalArgumentException("w can not be null");
        }
        this._w = w;
        this._wtimeout = wtimeout;
        this._fsync = fsync;
        this._j = j;
        this._continueOnErrorForInsert = continueOnInsertError;
    }

    public BasicDBObject getCommand() {
        BasicDBObject _command = new BasicDBObject("getlasterror", 1);
        if (((this._w instanceof Integer) && ((Integer) this._w).intValue() > 0) || ((this._w instanceof String) && this._w != null)) {
            _command.put("w", this._w);
            _command.put("wtimeout", (Object) Integer.valueOf(this._wtimeout));
        }
        if (this._fsync) {
            _command.put("fsync", (Object) true);
        }
        if (this._j) {
            _command.put("j", (Object) true);
        }
        return _command;
    }

    public void setWObject(Object w) {
        if (!(w instanceof Integer) && !(w instanceof String)) {
            throw new IllegalArgumentException("The w parameter must be an int or a String");
        }
        this._w = w;
    }

    public Object getWObject() {
        return this._w;
    }

    public int getW() {
        return ((Integer) this._w).intValue();
    }

    public String getWString() {
        return this._w.toString();
    }

    public int getWtimeout() {
        return this._wtimeout;
    }

    public boolean getFsync() {
        return this._fsync;
    }

    public boolean fsync() {
        return this._fsync;
    }

    public boolean raiseNetworkErrors() {
        if (this._w instanceof Integer) {
            return ((Integer) this._w).intValue() >= 0;
        }
        return this._w != null;
    }

    public boolean callGetLastError() {
        if (this._w instanceof Integer) {
            return ((Integer) this._w).intValue() > 0;
        }
        return this._w != null;
    }

    public static WriteConcern valueOf(String name) {
        if (_namedConcerns == null) {
            HashMap<String, WriteConcern> newMap = new HashMap<>(8, 1.0f);
            Field[] arr$ = WriteConcern.class.getFields();
            for (Field f : arr$) {
                if (Modifier.isStatic(f.getModifiers()) && f.getType().equals(WriteConcern.class)) {
                    try {
                        newMap.put(f.getName().toLowerCase(), (WriteConcern) f.get(null));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            _namedConcerns = newMap;
        }
        return _namedConcerns.get(name.toLowerCase());
    }

    public String toString() {
        return "WriteConcern " + getCommand() + " / (Continue Inserting on Errors? " + getContinueOnErrorForInsert() + ")";
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WriteConcern that = (WriteConcern) o;
        return this._continueOnErrorForInsert == that._continueOnErrorForInsert && this._fsync == that._fsync && this._j == that._j && this._wtimeout == that._wtimeout && this._w.equals(that._w);
    }

    public int hashCode() {
        int result = this._w.hashCode();
        return (((((((result * 31) + this._wtimeout) * 31) + (this._fsync ? 1 : 0)) * 31) + (this._j ? 1 : 0)) * 31) + (this._continueOnErrorForInsert ? 1 : 0);
    }

    public boolean getJ() {
        return this._j;
    }

    public WriteConcern continueOnErrorForInsert(boolean continueOnErrorForInsert) {
        if (this._w instanceof Integer) {
            return new WriteConcern(((Integer) this._w).intValue(), this._wtimeout, this._fsync, this._j, continueOnErrorForInsert);
        }
        if (this._w instanceof String) {
            return new WriteConcern((String) this._w, this._wtimeout, this._fsync, this._j, continueOnErrorForInsert);
        }
        throw new IllegalStateException("The w parameter must be an int or a String");
    }

    public boolean getContinueOnErrorForInsert() {
        return this._continueOnErrorForInsert;
    }

    public static Majority majorityWriteConcern(int wtimeout, boolean fsync, boolean j) {
        return new Majority(wtimeout, fsync, j);
    }

    /* loaded from: classes.dex */
    public static class Majority extends WriteConcern {
        private static final long serialVersionUID = -4128295115883875212L;

        public Majority() {
            super("majority", 0, false, false);
        }

        public Majority(int wtimeout, boolean fsync, boolean j) {
            super("majority", wtimeout, fsync, j);
        }

        @Override // com.mongodb.WriteConcern
        public String toString() {
            return "[Majority] WriteConcern " + getCommand();
        }
    }
}
