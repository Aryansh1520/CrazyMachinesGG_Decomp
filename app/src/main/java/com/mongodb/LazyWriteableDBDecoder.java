package com.mongodb;

/* loaded from: classes.dex */
public class LazyWriteableDBDecoder extends LazyDBDecoder {
    public static DBDecoderFactory FACTORY = new LazyDBDecoderFactory();

    /* loaded from: classes.dex */
    static class LazyDBDecoderFactory implements DBDecoderFactory {
        LazyDBDecoderFactory() {
        }

        @Override // com.mongodb.DBDecoderFactory
        public DBDecoder create() {
            return new LazyWriteableDBDecoder();
        }
    }

    @Override // com.mongodb.LazyDBDecoder, com.mongodb.DBDecoder
    public DBCallback getDBCallback(DBCollection collection) {
        return new LazyWriteableDBCallback(collection);
    }
}
