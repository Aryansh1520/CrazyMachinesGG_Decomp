package org.bson;

import org.bson.types.ObjectId;

/* loaded from: classes.dex */
public class EmptyBSONCallback implements BSONCallback {
    @Override // org.bson.BSONCallback
    public void objectStart() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public void objectStart(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public void objectStart(boolean array) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public Object objectDone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public BSONCallback createBSONCallback() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public void arrayStart() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public void arrayStart(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public Object arrayDone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public void gotNull(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public void gotUndefined(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public void gotMinKey(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public void gotMaxKey(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public void gotBoolean(String name, boolean v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public void gotDouble(String name, double v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public void gotInt(String name, int v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public void gotLong(String name, long v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public void gotDate(String name, long millis) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public void gotString(String name, String v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public void gotSymbol(String name, String v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public void gotRegex(String name, String pattern, String flags) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public void gotTimestamp(String name, int time, int inc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public void gotObjectId(String name, ObjectId id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public void gotDBRef(String name, String ns, ObjectId id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    @Deprecated
    public void gotBinaryArray(String name, byte[] data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public void gotUUID(String name, long part1, long part2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public void gotCode(String name, String code) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public void gotCodeWScope(String name, String code, Object scope) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public Object get() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.BSONCallback
    public void gotBinary(String name, byte type, byte[] data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
