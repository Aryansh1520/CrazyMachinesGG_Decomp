package com.mongodb.gridfs;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/* loaded from: classes.dex */
public class GridFS {
    public static final String DEFAULT_BUCKET = "fs";
    public static final int DEFAULT_CHUNKSIZE = 262144;
    public static final long MAX_CHUNKSIZE = 3500000;
    protected final String _bucketName;
    protected final DBCollection _chunkCollection;
    protected final DB _db;
    protected final DBCollection _filesCollection;

    public GridFS(DB db) {
        this(db, DEFAULT_BUCKET);
    }

    public GridFS(DB db, String bucket) {
        this._db = db;
        this._bucketName = bucket;
        this._filesCollection = this._db.getCollection(this._bucketName + ".files");
        this._chunkCollection = this._db.getCollection(this._bucketName + ".chunks");
        if (this._filesCollection.count() < 1000) {
            this._filesCollection.ensureIndex(BasicDBObjectBuilder.start().add("filename", 1).add("uploadDate", 1).get());
        }
        if (this._chunkCollection.count() < 1000) {
            this._chunkCollection.ensureIndex(BasicDBObjectBuilder.start().add("files_id", 1).add("n", 1).get(), BasicDBObjectBuilder.start().add("unique", 1).get());
        }
        this._filesCollection.setObjectClass(GridFSDBFile.class);
    }

    public DBCursor getFileList() {
        return this._filesCollection.find().sort(new BasicDBObject("filename", 1));
    }

    public DBCursor getFileList(DBObject query) {
        return this._filesCollection.find(query).sort(new BasicDBObject("filename", 1));
    }

    public GridFSDBFile find(ObjectId id) {
        return findOne(id);
    }

    public GridFSDBFile findOne(ObjectId id) {
        return findOne(new BasicDBObject("_id", id));
    }

    public GridFSDBFile findOne(String filename) {
        return findOne(new BasicDBObject("filename", filename));
    }

    public GridFSDBFile findOne(DBObject query) {
        return _fix(this._filesCollection.findOne(query));
    }

    public List<GridFSDBFile> find(String filename) {
        return find(new BasicDBObject("filename", filename));
    }

    public List<GridFSDBFile> find(DBObject query) {
        List<GridFSDBFile> files = new ArrayList<>();
        DBCursor c = this._filesCollection.find(query);
        while (c.hasNext()) {
            files.add(_fix(c.next()));
        }
        return files;
    }

    private GridFSDBFile _fix(Object o) {
        if (o == null) {
            return null;
        }
        if (!(o instanceof GridFSDBFile)) {
            throw new RuntimeException("somehow didn't get a GridFSDBFile");
        }
        GridFSDBFile f = (GridFSDBFile) o;
        f._fs = this;
        return f;
    }

    public void remove(ObjectId id) {
        this._filesCollection.remove(new BasicDBObject("_id", id));
        this._chunkCollection.remove(new BasicDBObject("files_id", id));
    }

    public void remove(String filename) {
        remove(new BasicDBObject("filename", filename));
    }

    public void remove(DBObject query) {
        for (GridFSDBFile f : find(query)) {
            f.remove();
        }
    }

    public GridFSInputFile createFile(byte[] data) {
        return createFile((InputStream) new ByteArrayInputStream(data), true);
    }

    public GridFSInputFile createFile(File f) throws IOException {
        return createFile(new FileInputStream(f), f.getName(), true);
    }

    public GridFSInputFile createFile(InputStream in) {
        return createFile(in, (String) null);
    }

    public GridFSInputFile createFile(InputStream in, boolean closeStreamOnPersist) {
        return createFile(in, null, closeStreamOnPersist);
    }

    public GridFSInputFile createFile(InputStream in, String filename) {
        return new GridFSInputFile(this, in, filename);
    }

    public GridFSInputFile createFile(InputStream in, String filename, boolean closeStreamOnPersist) {
        return new GridFSInputFile(this, in, filename, closeStreamOnPersist);
    }

    public GridFSInputFile createFile(String filename) {
        return new GridFSInputFile(this, filename);
    }

    public GridFSInputFile createFile() {
        return new GridFSInputFile(this);
    }

    public String getBucketName() {
        return this._bucketName;
    }

    public DB getDB() {
        return this._db;
    }
}
