package org.bson;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import org.bson.types.BSONTimestamp;
import org.bson.types.BasicBSONList;
import org.bson.types.Binary;
import org.bson.types.Code;
import org.bson.types.CodeWScope;
import org.bson.types.MaxKey;
import org.bson.types.MinKey;
import org.bson.types.ObjectId;

/* loaded from: classes.dex */
public class BasicBSONCallback implements BSONCallback {
    private Object _root;
    private final LinkedList<BSONObject> _stack = new LinkedList<>();
    private final LinkedList<String> _nameStack = new LinkedList<>();

    public BasicBSONCallback() {
        reset();
    }

    public BSONObject create() {
        return new BasicBSONObject();
    }

    protected BSONObject createList() {
        return new BasicBSONList();
    }

    @Override // org.bson.BSONCallback
    public BSONCallback createBSONCallback() {
        return new BasicBSONCallback();
    }

    public BSONObject create(boolean array, List<String> path) {
        return array ? createList() : create();
    }

    @Override // org.bson.BSONCallback
    public void objectStart() {
        if (this._stack.size() > 0) {
            throw new IllegalStateException("something is wrong");
        }
        objectStart(false);
    }

    @Override // org.bson.BSONCallback
    public void objectStart(boolean array) {
        this._root = create(array, null);
        this._stack.add((BSONObject) this._root);
    }

    @Override // org.bson.BSONCallback
    public void objectStart(String name) {
        objectStart(false, name);
    }

    public void objectStart(boolean array, String name) {
        this._nameStack.addLast(name);
        BSONObject o = create(array, this._nameStack);
        this._stack.getLast().put(name, o);
        this._stack.addLast(o);
    }

    @Override // org.bson.BSONCallback
    public Object objectDone() {
        BSONObject o = this._stack.removeLast();
        if (this._nameStack.size() > 0) {
            this._nameStack.removeLast();
        } else if (this._stack.size() > 0) {
            throw new IllegalStateException("something is wrong");
        }
        return !BSON.hasDecodeHooks() ? o : (BSONObject) BSON.applyDecodingHooks(o);
    }

    @Override // org.bson.BSONCallback
    public void arrayStart() {
        objectStart(true);
    }

    @Override // org.bson.BSONCallback
    public void arrayStart(String name) {
        objectStart(true, name);
    }

    @Override // org.bson.BSONCallback
    public Object arrayDone() {
        return objectDone();
    }

    @Override // org.bson.BSONCallback
    public void gotNull(String name) {
        cur().put(name, null);
    }

    @Override // org.bson.BSONCallback
    public void gotUndefined(String name) {
    }

    @Override // org.bson.BSONCallback
    public void gotMinKey(String name) {
        cur().put(name, new MinKey());
    }

    @Override // org.bson.BSONCallback
    public void gotMaxKey(String name) {
        cur().put(name, new MaxKey());
    }

    @Override // org.bson.BSONCallback
    public void gotBoolean(String name, boolean v) {
        _put(name, Boolean.valueOf(v));
    }

    @Override // org.bson.BSONCallback
    public void gotDouble(String name, double v) {
        _put(name, Double.valueOf(v));
    }

    @Override // org.bson.BSONCallback
    public void gotInt(String name, int v) {
        _put(name, Integer.valueOf(v));
    }

    @Override // org.bson.BSONCallback
    public void gotLong(String name, long v) {
        _put(name, Long.valueOf(v));
    }

    @Override // org.bson.BSONCallback
    public void gotDate(String name, long millis) {
        _put(name, new Date(millis));
    }

    @Override // org.bson.BSONCallback
    public void gotRegex(String name, String pattern, String flags) {
        _put(name, Pattern.compile(pattern, BSON.regexFlags(flags)));
    }

    @Override // org.bson.BSONCallback
    public void gotString(String name, String v) {
        _put(name, v);
    }

    @Override // org.bson.BSONCallback
    public void gotSymbol(String name, String v) {
        _put(name, v);
    }

    @Override // org.bson.BSONCallback
    public void gotTimestamp(String name, int time, int inc) {
        _put(name, new BSONTimestamp(time, inc));
    }

    @Override // org.bson.BSONCallback
    public void gotObjectId(String name, ObjectId id) {
        _put(name, id);
    }

    @Override // org.bson.BSONCallback
    public void gotDBRef(String name, String ns, ObjectId id) {
        _put(name, new BasicBSONObject("$ns", ns).append("$id", id));
    }

    @Override // org.bson.BSONCallback
    @Deprecated
    public void gotBinaryArray(String name, byte[] data) {
        gotBinary(name, (byte) 0, data);
    }

    @Override // org.bson.BSONCallback
    public void gotBinary(String name, byte type, byte[] data) {
        if (type == 0 || type == 2) {
            _put(name, data);
        } else {
            _put(name, new Binary(type, data));
        }
    }

    @Override // org.bson.BSONCallback
    public void gotUUID(String name, long part1, long part2) {
        _put(name, new UUID(part1, part2));
    }

    @Override // org.bson.BSONCallback
    public void gotCode(String name, String code) {
        _put(name, new Code(code));
    }

    @Override // org.bson.BSONCallback
    public void gotCodeWScope(String name, String code, Object scope) {
        _put(name, new CodeWScope(code, (BSONObject) scope));
    }

    protected void _put(String name, Object o) {
        BSONObject cur = cur();
        if (BSON.hasDecodeHooks()) {
            o = BSON.applyDecodingHooks(o);
        }
        cur.put(name, o);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public BSONObject cur() {
        return this._stack.getLast();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String curName() {
        if (this._nameStack.isEmpty()) {
            return null;
        }
        return this._nameStack.getLast();
    }

    @Override // org.bson.BSONCallback
    public Object get() {
        return this._root;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setRoot(Object o) {
        this._root = o;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isStackEmpty() {
        return this._stack.size() < 1;
    }

    @Override // org.bson.BSONCallback
    public void reset() {
        this._root = null;
        this._stack.clear();
        this._nameStack.clear();
    }
}
