package org.bson;

import java.util.HashMap;
import org.bson.LazyBSONObject;
import org.bson.io.BSONByteBuffer;

/* loaded from: classes.dex */
public class KeyCachingLazyBSONObject extends LazyBSONObject {
    private HashMap<String, LazyBSONObject.ElementRecord> fieldIndex;

    public KeyCachingLazyBSONObject(byte[] data, LazyBSONCallback cbk) {
        super(data, cbk);
        this.fieldIndex = new HashMap<>();
    }

    public KeyCachingLazyBSONObject(byte[] data, int offset, LazyBSONCallback cbk) {
        super(data, offset, cbk);
        this.fieldIndex = new HashMap<>();
    }

    public KeyCachingLazyBSONObject(BSONByteBuffer buffer, LazyBSONCallback callback) {
        super(buffer, callback);
        this.fieldIndex = new HashMap<>();
    }

    public KeyCachingLazyBSONObject(BSONByteBuffer buffer, int offset, LazyBSONCallback callback) {
        super(buffer, offset, callback);
        this.fieldIndex = new HashMap<>();
    }

    @Override // org.bson.LazyBSONObject, org.bson.BSONObject
    public Object get(String key) {
        ensureFieldList();
        return super.get(key);
    }

    @Override // org.bson.LazyBSONObject, org.bson.BSONObject
    public boolean containsField(String s) {
        ensureFieldList();
        if (this.fieldIndex.containsKey(s)) {
            return super.containsField(s);
        }
        return false;
    }

    private synchronized void ensureFieldList() {
        if (this.fieldIndex != null) {
            try {
                int offset = this._doc_start_offset + 4;
                while (true) {
                    int offset2 = offset;
                    if (isElementEmpty(offset2)) {
                        break;
                    }
                    int fieldSize = sizeCString(offset2);
                    int offset3 = offset2 + 1;
                    int elementSize = getElementBSONSize(offset2);
                    String name = this._input.getCString(offset3);
                    LazyBSONObject.ElementRecord _t_record = new LazyBSONObject.ElementRecord(name, offset3);
                    this.fieldIndex.put(name, _t_record);
                    offset = offset3 + fieldSize + elementSize;
                }
            } catch (Exception e) {
                this.fieldIndex = new HashMap<>();
            }
        }
    }
}
