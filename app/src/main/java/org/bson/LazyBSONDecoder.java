package org.bson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import org.bson.io.Bits;

/* loaded from: classes.dex */
public class LazyBSONDecoder implements BSONDecoder {
    static final Logger LOG = Logger.getLogger(LazyBSONDecoder.class.getName());
    private static int BYTES_IN_INTEGER = 4;

    @Override // org.bson.BSONDecoder
    public BSONObject readObject(byte[] b) {
        try {
            return readObject(new ByteArrayInputStream(b));
        } catch (IOException ioe) {
            throw new BSONException("should be impossible", ioe);
        }
    }

    @Override // org.bson.BSONDecoder
    public BSONObject readObject(InputStream in) throws IOException {
        BSONCallback c = new LazyBSONCallback();
        decode(in, c);
        return (BSONObject) c.get();
    }

    @Override // org.bson.BSONDecoder
    public int decode(byte[] b, BSONCallback callback) {
        try {
            return decode(new ByteArrayInputStream(b), callback);
        } catch (IOException ioe) {
            throw new BSONException("should be impossible", ioe);
        }
    }

    @Override // org.bson.BSONDecoder
    public int decode(InputStream in, BSONCallback callback) throws IOException {
        byte[] objSizeBuffer = new byte[BYTES_IN_INTEGER];
        Bits.readFully(in, objSizeBuffer, 0, BYTES_IN_INTEGER);
        int objSize = Bits.readInt(objSizeBuffer);
        byte[] data = new byte[objSize];
        System.arraycopy(objSizeBuffer, 0, data, 0, BYTES_IN_INTEGER);
        Bits.readFully(in, data, BYTES_IN_INTEGER, objSize - BYTES_IN_INTEGER);
        callback.gotBinary(null, (byte) 0, data);
        return objSize;
    }
}
