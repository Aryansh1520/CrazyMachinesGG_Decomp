package com.mongodb.gridfs;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.bson.BSON;

/* loaded from: classes.dex */
public class GridFSDBFile extends GridFSFile {
    public InputStream getInputStream() {
        return new MyInputStream();
    }

    public long writeTo(String filename) throws IOException {
        return writeTo(new File(filename));
    }

    public long writeTo(File f) throws IOException {
        FileOutputStream out;
        FileOutputStream out2 = null;
        try {
            out = new FileOutputStream(f);
        } catch (Throwable th) {
            th = th;
        }
        try {
            long writeTo = writeTo(out);
            if (out != null) {
                out.close();
            }
            return writeTo;
        } catch (Throwable th2) {
            th = th2;
            out2 = out;
            if (out2 != null) {
                out2.close();
            }
            throw th;
        }
    }

    public long writeTo(OutputStream out) throws IOException {
        int nc = numChunks();
        for (int i = 0; i < nc; i++) {
            out.write(getChunk(i));
        }
        return this._length;
    }

    byte[] getChunk(int i) {
        if (this._fs == null) {
            throw new RuntimeException("no gridfs!");
        }
        DBObject chunk = this._fs._chunkCollection.findOne(BasicDBObjectBuilder.start("files_id", this._id).add("n", Integer.valueOf(i)).get());
        if (chunk == null) {
            throw new MongoException("can't find a chunk!  file id: " + this._id + " chunk: " + i);
        }
        return (byte[]) chunk.get("data");
    }

    /* loaded from: classes.dex */
    class MyInputStream extends InputStream {
        final int _numChunks;
        int _currentChunkIdx = -1;
        int _offset = 0;
        byte[] _data = null;

        MyInputStream() {
            this._numChunks = GridFSDBFile.this.numChunks();
        }

        @Override // java.io.InputStream
        public int available() {
            if (this._data == null) {
                return 0;
            }
            return this._data.length - this._offset;
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }

        @Override // java.io.InputStream
        public void mark(int readlimit) {
            throw new RuntimeException("mark not supported");
        }

        @Override // java.io.InputStream
        public void reset() {
            throw new RuntimeException("mark not supported");
        }

        @Override // java.io.InputStream
        public boolean markSupported() {
            return false;
        }

        @Override // java.io.InputStream
        public int read() {
            byte[] b = new byte[1];
            int res = read(b);
            if (res < 0) {
                return -1;
            }
            return b[0] & BSON.MINKEY;
        }

        @Override // java.io.InputStream
        public int read(byte[] b) {
            return read(b, 0, b.length);
        }

        @Override // java.io.InputStream
        public int read(byte[] b, int off, int len) {
            if (this._data == null || this._offset >= this._data.length) {
                if (this._currentChunkIdx + 1 >= this._numChunks) {
                    return -1;
                }
                GridFSDBFile gridFSDBFile = GridFSDBFile.this;
                int i = this._currentChunkIdx + 1;
                this._currentChunkIdx = i;
                this._data = gridFSDBFile.getChunk(i);
                this._offset = 0;
            }
            int r = Math.min(len, this._data.length - this._offset);
            System.arraycopy(this._data, this._offset, b, off, r);
            this._offset += r;
            return r;
        }

        @Override // java.io.InputStream
        public long skip(long numBytesToSkip) throws IOException {
            if (numBytesToSkip > 0 && this._currentChunkIdx != this._numChunks) {
                long offsetInFile = 0;
                if (this._currentChunkIdx >= 0) {
                    offsetInFile = (this._currentChunkIdx * GridFSDBFile.this._chunkSize) + this._offset;
                }
                if (numBytesToSkip + offsetInFile >= GridFSDBFile.this._length) {
                    this._currentChunkIdx = this._numChunks;
                    this._data = null;
                    return GridFSDBFile.this._length - offsetInFile;
                }
                int temp = this._currentChunkIdx;
                this._currentChunkIdx = (int) ((numBytesToSkip + offsetInFile) / GridFSDBFile.this._chunkSize);
                if (temp != this._currentChunkIdx) {
                    this._data = GridFSDBFile.this.getChunk(this._currentChunkIdx);
                }
                this._offset = (int) ((numBytesToSkip + offsetInFile) % GridFSDBFile.this._chunkSize);
                return numBytesToSkip;
            }
            return 0L;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void remove() {
        this._fs._filesCollection.remove(new BasicDBObject("_id", this._id));
        this._fs._chunkCollection.remove(new BasicDBObject("files_id", this._id));
    }
}
