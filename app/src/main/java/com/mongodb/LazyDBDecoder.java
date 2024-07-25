package com.mongodb;

import java.io.IOException;
import java.io.InputStream;
import org.bson.LazyBSONDecoder;

/* loaded from: classes.dex */
public class LazyDBDecoder extends LazyBSONDecoder implements DBDecoder {
    public static DBDecoderFactory FACTORY = new LazyDBDecoderFactory();

    /* loaded from: classes.dex */
    static class LazyDBDecoderFactory implements DBDecoderFactory {
        LazyDBDecoderFactory() {
        }

        @Override // com.mongodb.DBDecoderFactory
        public DBDecoder create() {
            return new LazyDBDecoder();
        }
    }

    @Override // com.mongodb.DBDecoder
    public DBCallback getDBCallback(DBCollection collection) {
        return new LazyDBCallback(collection);
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
