package com.mongodb.gridfs;

import android.support.v4.view.MotionEventCompat;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import org.bson.types.ObjectId;

/* loaded from: classes.dex */
public class GridFSInputFile extends GridFSFile {
    private byte[] _buffer;
    private boolean _closeStreamOnPersist;
    private int _currentBufferPosition;
    private int _currentChunkNumber;
    private final InputStream _in;
    private MessageDigest _messageDigester;
    private OutputStream _outputStream;
    private boolean _savedChunks;
    private long _totalBytes;

    static /* synthetic */ int access$012(GridFSInputFile x0, int x1) {
        int i = x0._currentBufferPosition + x1;
        x0._currentBufferPosition = i;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public GridFSInputFile(GridFS fs, InputStream in, String filename, boolean closeStreamOnPersist) {
        this._savedChunks = false;
        this._buffer = null;
        this._currentChunkNumber = 0;
        this._currentBufferPosition = 0;
        this._totalBytes = 0L;
        this._messageDigester = null;
        this._outputStream = null;
        this._fs = fs;
        this._in = in;
        this._filename = filename;
        this._closeStreamOnPersist = closeStreamOnPersist;
        this._id = new ObjectId();
        this._chunkSize = 262144L;
        this._uploadDate = new Date();
        try {
            this._messageDigester = MessageDigest.getInstance("MD5");
            this._messageDigester.reset();
            this._buffer = new byte[(int) this._chunkSize];
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No MD5!");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public GridFSInputFile(GridFS fs, InputStream in, String filename) {
        this(fs, in, filename, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public GridFSInputFile(GridFS fs, String filename) {
        this(fs, null, filename);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public GridFSInputFile(GridFS fs) {
        this(fs, null, null);
    }

    public void setId(Object id) {
        this._id = id;
    }

    public void setFilename(String fn) {
        this._filename = fn;
    }

    public void setContentType(String ct) {
        this._contentType = ct;
    }

    public void setChunkSize(long chunkSize) {
        if (this._outputStream == null && !this._savedChunks) {
            this._chunkSize = chunkSize;
            this._buffer = new byte[(int) this._chunkSize];
        }
    }

    @Override // com.mongodb.gridfs.GridFSFile
    public void save() {
        save(this._chunkSize);
    }

    public void save(long chunkSize) {
        if (this._outputStream != null) {
            throw new MongoException("cannot mix OutputStream and regular save()");
        }
        if (!this._savedChunks) {
            try {
                saveChunks(chunkSize);
            } catch (IOException ioe) {
                throw new MongoException("couldn't save chunks", ioe);
            }
        }
        super.save();
    }

    public int saveChunks() throws IOException {
        return saveChunks(this._chunkSize);
    }

    public int saveChunks(long chunkSize) throws IOException {
        if (this._outputStream != null) {
            throw new MongoException("cannot mix OutputStream and regular save()");
        }
        if (this._savedChunks) {
            throw new MongoException("chunks already saved!");
        }
        if (chunkSize <= 0 || chunkSize > GridFS.MAX_CHUNKSIZE) {
            throw new MongoException("chunkSize must be greater than zero and less than or equal to GridFS.MAX_CHUNKSIZE");
        }
        if (this._chunkSize != chunkSize) {
            this._chunkSize = chunkSize;
            this._buffer = new byte[(int) this._chunkSize];
        }
        int bytesRead = 0;
        while (bytesRead >= 0) {
            this._currentBufferPosition = 0;
            bytesRead = _readStream2Buffer();
            _dumpBuffer(true);
        }
        _finishData();
        return this._currentChunkNumber;
    }

    public OutputStream getOutputStream() {
        if (this._outputStream == null) {
            this._outputStream = new MyOutputStream();
        }
        return this._outputStream;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void _dumpBuffer(boolean writePartial) {
        if ((this._currentBufferPosition >= this._chunkSize || writePartial) && this._currentBufferPosition != 0) {
            byte[] writeBuffer = this._buffer;
            if (this._currentBufferPosition != this._chunkSize) {
                writeBuffer = new byte[this._currentBufferPosition];
                System.arraycopy(this._buffer, 0, writeBuffer, 0, this._currentBufferPosition);
            }
            DBObject chunk = createChunk(this._id, this._currentChunkNumber, writeBuffer);
            this._fs._chunkCollection.save(chunk);
            this._currentChunkNumber++;
            this._totalBytes += writeBuffer.length;
            this._messageDigester.update(writeBuffer);
            this._currentBufferPosition = 0;
        }
    }

    protected DBObject createChunk(Object id, int currentChunkNumber, byte[] writeBuffer) {
        return BasicDBObjectBuilder.start().add("files_id", id).add("n", Integer.valueOf(currentChunkNumber)).add("data", writeBuffer).get();
    }

    private int _readStream2Buffer() throws IOException {
        int bytesRead = 0;
        while (this._currentBufferPosition < this._chunkSize && bytesRead >= 0) {
            bytesRead = this._in.read(this._buffer, this._currentBufferPosition, ((int) this._chunkSize) - this._currentBufferPosition);
            if (bytesRead > 0) {
                this._currentBufferPosition += bytesRead;
            } else if (bytesRead == 0) {
                throw new RuntimeException("i'm doing something wrong");
            }
        }
        return bytesRead;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void _finishData() {
        if (!this._savedChunks) {
            this._md5 = Util.toHex(this._messageDigester.digest());
            this._messageDigester = null;
            this._length = this._totalBytes;
            this._savedChunks = true;
            try {
                if (this._in != null && this._closeStreamOnPersist) {
                    this._in.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /* loaded from: classes.dex */
    class MyOutputStream extends OutputStream {
        MyOutputStream() {
        }

        @Override // java.io.OutputStream
        public void write(int b) throws IOException {
            byte[] byteArray = {(byte) (b & MotionEventCompat.ACTION_MASK)};
            write(byteArray, 0, 1);
        }

        @Override // java.io.OutputStream
        public void write(byte[] b, int off, int len) throws IOException {
            int offset = off;
            int length = len;
            while (length > 0) {
                int toCopy = length;
                if (toCopy > GridFSInputFile.this._chunkSize - GridFSInputFile.this._currentBufferPosition) {
                    toCopy = ((int) GridFSInputFile.this._chunkSize) - GridFSInputFile.this._currentBufferPosition;
                }
                System.arraycopy(b, offset, GridFSInputFile.this._buffer, GridFSInputFile.this._currentBufferPosition, toCopy);
                GridFSInputFile.access$012(GridFSInputFile.this, toCopy);
                offset += toCopy;
                length -= toCopy;
                if (GridFSInputFile.this._currentBufferPosition == GridFSInputFile.this._chunkSize) {
                    GridFSInputFile.this._dumpBuffer(false);
                }
            }
        }

        @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            GridFSInputFile.this._dumpBuffer(true);
            GridFSInputFile.this._finishData();
            GridFSInputFile.super.save();
        }
    }
}
