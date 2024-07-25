package com.mongodb.gridfs;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bson.BSONObject;

/* loaded from: classes.dex */
public abstract class GridFSFile implements DBObject {
    static final Set<String> VALID_FIELDS = Collections.unmodifiableSet(new HashSet(Arrays.asList("_id", "filename", "contentType", "length", "chunkSize", "uploadDate", "aliases", "md5")));
    List<String> _aliases;
    long _chunkSize;
    String _contentType;
    String _filename;
    Object _id;
    long _length;
    String _md5;
    Date _uploadDate;
    protected GridFS _fs = null;
    DBObject _extradata = new BasicDBObject();

    public void save() {
        if (this._fs == null) {
            throw new MongoException("need _fs");
        }
        this._fs._filesCollection.save(this);
    }

    public void validate() {
        if (this._fs == null) {
            throw new MongoException("no _fs");
        }
        if (this._md5 == null) {
            throw new MongoException("no _md5 stored");
        }
        DBObject cmd = new BasicDBObject("filemd5", this._id);
        cmd.put("root", this._fs._bucketName);
        DBObject res = this._fs._db.command(cmd);
        if (res != null && res.containsField("md5")) {
            String m = res.get("md5").toString();
            if (m.equals(this._md5)) {
                return;
            } else {
                throw new MongoException("md5 differ.  mine [" + this._md5 + "] theirs [" + m + "]");
            }
        }
        throw new MongoException("no md5 returned from server: " + res);
    }

    public int numChunks() {
        double d = this._length;
        return (int) Math.ceil(d / this._chunkSize);
    }

    public Object getId() {
        return this._id;
    }

    public String getFilename() {
        return this._filename;
    }

    public String getContentType() {
        return this._contentType;
    }

    public long getLength() {
        return this._length;
    }

    public long getChunkSize() {
        return this._chunkSize;
    }

    public Date getUploadDate() {
        return this._uploadDate;
    }

    public List<String> getAliases() {
        return (List) this._extradata.get("aliases");
    }

    public DBObject getMetaData() {
        return (DBObject) this._extradata.get("metadata");
    }

    public void setMetaData(DBObject metadata) {
        this._extradata.put("metadata", metadata);
    }

    public String getMD5() {
        return this._md5;
    }

    @Override // org.bson.BSONObject
    public Object put(String key, Object v) {
        if (key == null) {
            throw new RuntimeException("key should never be null");
        }
        if (key.equals("_id")) {
            this._id = v;
        } else if (key.equals("filename")) {
            this._filename = v == null ? null : v.toString();
        } else if (key.equals("contentType")) {
            this._contentType = (String) v;
        } else if (key.equals("length")) {
            this._length = ((Number) v).longValue();
        } else if (key.equals("chunkSize")) {
            this._chunkSize = ((Number) v).longValue();
        } else if (key.equals("uploadDate")) {
            this._uploadDate = (Date) v;
        } else if (key.equals("md5")) {
            this._md5 = (String) v;
        } else {
            this._extradata.put(key, v);
        }
        return v;
    }

    @Override // org.bson.BSONObject
    public Object get(String key) {
        if (key == null) {
            throw new RuntimeException("key should never be null");
        }
        if (key.equals("_id")) {
            return this._id;
        }
        if (key.equals("filename")) {
            return this._filename;
        }
        if (key.equals("contentType")) {
            return this._contentType;
        }
        if (key.equals("length")) {
            return Long.valueOf(this._length);
        }
        if (key.equals("chunkSize")) {
            return Long.valueOf(this._chunkSize);
        }
        if (key.equals("uploadDate")) {
            return this._uploadDate;
        }
        if (key.equals("md5")) {
            return this._md5;
        }
        return this._extradata.get(key);
    }

    @Override // org.bson.BSONObject
    public void putAll(BSONObject o) {
        throw new UnsupportedOperationException();
    }

    @Override // org.bson.BSONObject
    public void putAll(Map m) {
        throw new UnsupportedOperationException();
    }

    @Override // org.bson.BSONObject
    public Map toMap() {
        throw new UnsupportedOperationException();
    }

    @Override // org.bson.BSONObject
    public Object removeField(String key) {
        throw new UnsupportedOperationException();
    }

    @Override // org.bson.BSONObject
    @Deprecated
    public boolean containsKey(String s) {
        return containsField(s);
    }

    @Override // org.bson.BSONObject
    public boolean containsField(String s) {
        return keySet().contains(s);
    }

    @Override // org.bson.BSONObject
    public Set<String> keySet() {
        Set<String> keys = new HashSet<>();
        keys.addAll(VALID_FIELDS);
        keys.addAll(this._extradata.keySet());
        return keys;
    }

    @Override // com.mongodb.DBObject
    public boolean isPartialObject() {
        return false;
    }

    @Override // com.mongodb.DBObject
    public void markAsPartialObject() {
        throw new RuntimeException("can't load partial GridFSFile file");
    }

    public String toString() {
        return JSON.serialize(this);
    }

    protected void setGridFS(GridFS fs) {
        this._fs = fs;
    }
}
