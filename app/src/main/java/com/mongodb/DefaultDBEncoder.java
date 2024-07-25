package com.mongodb;

import org.bson.BSON;
import org.bson.BSONObject;
import org.bson.BasicBSONEncoder;
import org.bson.io.OutputBuffer;
import org.bson.types.ObjectId;

/* loaded from: classes.dex */
public class DefaultDBEncoder extends BasicBSONEncoder implements DBEncoder {
    public static DBEncoderFactory FACTORY = new DefaultFactory();

    @Override // com.mongodb.DBEncoder
    public int writeObject(OutputBuffer buf, BSONObject o) {
        set(buf);
        int x = super.putObject(o);
        done();
        return x;
    }

    /* loaded from: classes.dex */
    static class DefaultFactory implements DBEncoderFactory {
        DefaultFactory() {
        }

        @Override // com.mongodb.DBEncoderFactory
        public DBEncoder create() {
            return new DefaultDBEncoder();
        }
    }

    @Override // org.bson.BasicBSONEncoder
    protected boolean putSpecial(String name, Object val) {
        if (val instanceof DBPointer) {
            DBPointer r = (DBPointer) val;
            putDBPointer(name, r._ns, (ObjectId) r._id);
            return true;
        }
        if (val instanceof DBRefBase) {
            putDBRef(name, (DBRefBase) val);
            return true;
        }
        return false;
    }

    protected void putDBPointer(String name, String ns, ObjectId oid) {
        _put(BSON.REF, name);
        _putValueString(ns);
        this._buf.writeInt(oid._time());
        this._buf.writeInt(oid._machine());
        this._buf.writeInt(oid._inc());
    }

    protected void putDBRef(String name, DBRefBase ref) {
        _put((byte) 3, name);
        int sizePos = this._buf.getPosition();
        this._buf.writeInt(0);
        _putObjectField("$ref", ref.getRef());
        _putObjectField("$id", ref.getId());
        this._buf.write(0);
        this._buf.writeInt(sizePos, this._buf.getPosition() - sizePos);
    }
}
