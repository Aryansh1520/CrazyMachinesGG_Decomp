package com.mongodb;

import java.io.IOException;
import java.io.InputStream;
import org.bson.BasicBSONDecoder;

/* loaded from: classes.dex */
public class DefaultDBDecoder extends BasicBSONDecoder implements DBDecoder {
    public static DBDecoderFactory FACTORY = new DefaultFactory();

    /* loaded from: classes.dex */
    static class DefaultFactory implements DBDecoderFactory {
        DefaultFactory() {
        }

        @Override // com.mongodb.DBDecoderFactory
        public DBDecoder create() {
            return new DefaultDBDecoder();
        }
    }

    @Override // com.mongodb.DBDecoder
    public DBCallback getDBCallback(DBCollection collection) {
        return new DefaultDBCallback(collection);
    }

    @Override // com.mongodb.DBDecoder
    public DBObject decode(byte[] b, DBCollection collection) {
        DBCallback cbk = getDBCallback(collection);
        cbk.reset();
        decode(b, cbk);
        return (DBObject) cbk.get();
    }

    @Override // com.mongodb.DBDecoder
    public DBObject decode(InputStream in, DBCollection collection) throws IOException {
        DBCallback cbk = getDBCallback(collection);
        cbk.reset();
        decode(in, cbk);
        return (DBObject) cbk.get();
    }
}
