package org.bson;

import com.mappn.sdk.pay.chargement.ChargeActivity;
import com.mongodb.DBRefBase;
import com.mongodb.QueryOperators;
import com.mongodb.util.MyAsserts;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import org.bson.io.BasicOutputBuffer;
import org.bson.io.OutputBuffer;
import org.bson.types.BSONTimestamp;
import org.bson.types.Binary;
import org.bson.types.Code;
import org.bson.types.CodeWScope;
import org.bson.types.MaxKey;
import org.bson.types.MinKey;
import org.bson.types.ObjectId;
import org.bson.types.Symbol;

/* loaded from: classes.dex */
public class BasicBSONEncoder implements BSONEncoder {
    static final boolean DEBUG = false;
    protected OutputBuffer _buf;

    @Override // org.bson.BSONEncoder
    public byte[] encode(BSONObject o) {
        BasicOutputBuffer buf = new BasicOutputBuffer();
        set(buf);
        putObject(o);
        done();
        return buf.toByteArray();
    }

    @Override // org.bson.BSONEncoder
    public void set(OutputBuffer out) {
        if (this._buf != null) {
            throw new IllegalStateException("in the middle of something");
        }
        this._buf = out;
    }

    @Override // org.bson.BSONEncoder
    public void done() {
        this._buf = null;
    }

    protected boolean handleSpecialObjects(String name, BSONObject o) {
        return false;
    }

    protected boolean putSpecial(String name, Object o) {
        return false;
    }

    @Override // org.bson.BSONEncoder
    public int putObject(BSONObject o) {
        return putObject(null, o);
    }

    protected int putObject(String name, BSONObject o) {
        if (o == null) {
            throw new NullPointerException("can't save a null object");
        }
        int start = this._buf.getPosition();
        byte myType = 3;
        if (o instanceof List) {
            myType = 4;
        }
        if (handleSpecialObjects(name, o)) {
            return this._buf.getPosition() - start;
        }
        if (name != null) {
            _put(myType, name);
        }
        int sizePos = this._buf.getPosition();
        this._buf.writeInt(0);
        List transientFields = null;
        boolean rewriteID = myType == 3 && name == null;
        if (myType == 3) {
            if (rewriteID && o.containsField("_id")) {
                _putObjectField("_id", o.get("_id"));
            }
            Object temp = o.get("_transientFields");
            if (temp instanceof List) {
                transientFields = (List) temp;
            }
        }
        if (o instanceof Map) {
            for (Map.Entry<String, Object> e : ((Map) o).entrySet()) {
                if (!rewriteID || !e.getKey().equals("_id")) {
                    if (transientFields == null || !transientFields.contains(e.getKey())) {
                        _putObjectField(e.getKey(), e.getValue());
                    }
                }
            }
        } else {
            for (String s : o.keySet()) {
                if (!rewriteID || !s.equals("_id")) {
                    if (transientFields == null || !transientFields.contains(s)) {
                        Object val = o.get(s);
                        _putObjectField(s, val);
                    }
                }
            }
        }
        this._buf.write(0);
        this._buf.writeInt(sizePos, this._buf.getPosition() - sizePos);
        return this._buf.getPosition() - start;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void _putObjectField(String name, Object val) {
        if (!name.equals("_transientFields")) {
            if (name.equals(QueryOperators.WHERE) && (val instanceof String)) {
                _put(BSON.CODE, name);
                _putValueString(val.toString());
                return;
            }
            Object val2 = BSON.applyEncodingHooks(val);
            if (val2 == null) {
                putNull(name);
                return;
            }
            if (val2 instanceof Date) {
                putDate(name, (Date) val2);
                return;
            }
            if (val2 instanceof Number) {
                putNumber(name, (Number) val2);
                return;
            }
            if (val2 instanceof Character) {
                putString(name, val2.toString());
                return;
            }
            if (val2 instanceof String) {
                putString(name, val2.toString());
                return;
            }
            if (val2 instanceof ObjectId) {
                putObjectId(name, (ObjectId) val2);
                return;
            }
            if (val2 instanceof BSONObject) {
                putObject(name, (BSONObject) val2);
                return;
            }
            if (val2 instanceof Boolean) {
                putBoolean(name, (Boolean) val2);
                return;
            }
            if (val2 instanceof Pattern) {
                putPattern(name, (Pattern) val2);
                return;
            }
            if (val2 instanceof Map) {
                putMap(name, (Map) val2);
                return;
            }
            if (val2 instanceof Iterable) {
                putIterable(name, (Iterable) val2);
                return;
            }
            if (val2 instanceof byte[]) {
                putBinary(name, (byte[]) val2);
                return;
            }
            if (val2 instanceof Binary) {
                putBinary(name, (Binary) val2);
                return;
            }
            if (val2 instanceof UUID) {
                putUUID(name, (UUID) val2);
                return;
            }
            if (val2.getClass().isArray()) {
                putArray(name, val2);
                return;
            }
            if (val2 instanceof Symbol) {
                putSymbol(name, (Symbol) val2);
                return;
            }
            if (val2 instanceof BSONTimestamp) {
                putTimestamp(name, (BSONTimestamp) val2);
                return;
            }
            if (val2 instanceof CodeWScope) {
                putCodeWScope(name, (CodeWScope) val2);
                return;
            }
            if (val2 instanceof Code) {
                putCode(name, (Code) val2);
                return;
            }
            if (val2 instanceof DBRefBase) {
                BSONObject temp = new BasicBSONObject();
                temp.put("$ref", ((DBRefBase) val2).getRef());
                temp.put("$id", ((DBRefBase) val2).getId());
                putObject(name, temp);
                return;
            }
            if (val2 instanceof MinKey) {
                putMinKey(name);
            } else if (val2 instanceof MaxKey) {
                putMaxKey(name);
            } else if (!putSpecial(name, val2)) {
                throw new IllegalArgumentException("can't serialize " + val2.getClass());
            }
        }
    }

    private void putArray(String name, Object array) {
        _put((byte) 4, name);
        int sizePos = this._buf.getPosition();
        this._buf.writeInt(0);
        int size = Array.getLength(array);
        for (int i = 0; i < size; i++) {
            _putObjectField(String.valueOf(i), Array.get(array, i));
        }
        this._buf.write(0);
        this._buf.writeInt(sizePos, this._buf.getPosition() - sizePos);
    }

    private void putIterable(String name, Iterable l) {
        _put((byte) 4, name);
        int sizePos = this._buf.getPosition();
        this._buf.writeInt(0);
        int i = 0;
        for (Object obj : l) {
            _putObjectField(String.valueOf(i), obj);
            i++;
        }
        this._buf.write(0);
        this._buf.writeInt(sizePos, this._buf.getPosition() - sizePos);
    }

    private void putMap(String name, Map m) {
        _put((byte) 3, name);
        int sizePos = this._buf.getPosition();
        this._buf.writeInt(0);
        for (Map.Entry entry : m.entrySet()) {
            _putObjectField(entry.getKey().toString(), entry.getValue());
        }
        this._buf.write(0);
        this._buf.writeInt(sizePos, this._buf.getPosition() - sizePos);
    }

    protected void putNull(String name) {
        _put((byte) 10, name);
    }

    protected void putUndefined(String name) {
        _put((byte) 6, name);
    }

    protected void putTimestamp(String name, BSONTimestamp ts) {
        _put(BSON.TIMESTAMP, name);
        this._buf.writeInt(ts.getInc());
        this._buf.writeInt(ts.getTime());
    }

    protected void putCodeWScope(String name, CodeWScope code) {
        _put(BSON.CODE_W_SCOPE, name);
        int temp = this._buf.getPosition();
        this._buf.writeInt(0);
        _putValueString(code.getCode());
        putObject(code.getScope());
        this._buf.writeInt(temp, this._buf.getPosition() - temp);
    }

    protected void putCode(String name, Code code) {
        _put(BSON.CODE, name);
        this._buf.getPosition();
        _putValueString(code.getCode());
    }

    protected void putBoolean(String name, Boolean b) {
        _put((byte) 8, name);
        this._buf.write(b.booleanValue() ? 1 : 0);
    }

    protected void putDate(String name, Date d) {
        _put((byte) 9, name);
        this._buf.writeLong(d.getTime());
    }

    protected void putNumber(String name, Number n) {
        if ((n instanceof Integer) || (n instanceof Short) || (n instanceof Byte) || (n instanceof AtomicInteger)) {
            _put(BSON.NUMBER_INT, name);
            this._buf.writeInt(n.intValue());
        } else if ((n instanceof Long) || (n instanceof AtomicLong)) {
            _put(BSON.NUMBER_LONG, name);
            this._buf.writeLong(n.longValue());
        } else {
            if ((n instanceof Float) || (n instanceof Double)) {
                _put((byte) 1, name);
                this._buf.writeDouble(n.doubleValue());
                return;
            }
            throw new IllegalArgumentException("can't serialize " + n.getClass());
        }
    }

    protected void putBinary(String name, byte[] data) {
        putBinary(name, 0, data);
    }

    protected void putBinary(String name, Binary val) {
        putBinary(name, val.getType(), val.getData());
    }

    private void putBinary(String name, int type, byte[] data) {
        _put((byte) 5, name);
        int totalLen = data.length;
        if (type == 2) {
            totalLen += 4;
        }
        this._buf.writeInt(totalLen);
        this._buf.write(type);
        if (type == 2) {
            this._buf.writeInt(totalLen - 4);
        }
        int before = this._buf.getPosition();
        this._buf.write(data);
        int after = this._buf.getPosition();
        MyAsserts.assertEquals(after - before, data.length);
    }

    protected void putUUID(String name, UUID val) {
        _put((byte) 5, name);
        this._buf.writeInt(16);
        this._buf.write(3);
        this._buf.writeLong(val.getMostSignificantBits());
        this._buf.writeLong(val.getLeastSignificantBits());
    }

    protected void putSymbol(String name, Symbol s) {
        _putString(name, s.getSymbol(), BSON.SYMBOL);
    }

    protected void putString(String name, String s) {
        _putString(name, s, (byte) 2);
    }

    private void _putString(String name, String s, byte type) {
        _put(type, name);
        _putValueString(s);
    }

    protected void putObjectId(String name, ObjectId oid) {
        _put((byte) 7, name);
        this._buf.writeIntBE(oid._time());
        this._buf.writeIntBE(oid._machine());
        this._buf.writeIntBE(oid._inc());
    }

    private void putPattern(String name, Pattern p) {
        _put(BSON.REGEX, name);
        _put(p.pattern());
        _put(BSON.regexFlags(p.flags()));
    }

    private void putMinKey(String name) {
        _put((byte) -1, name);
    }

    private void putMaxKey(String name) {
        _put(BSON.MAXKEY, name);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void _put(byte type, String name) {
        this._buf.write(type);
        _put(name);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void _putValueString(String s) {
        int lenPos = this._buf.getPosition();
        this._buf.writeInt(0);
        int strLen = _put(s);
        this._buf.writeInt(lenPos, strLen);
    }

    void _reset(Buffer b) {
        b.position(0);
        b.limit(b.capacity());
    }

    protected int _put(String str) {
        int len = str.length();
        int total = 0;
        int i = 0;
        while (i < len) {
            int c = Character.codePointAt(str, i);
            if (c < 128) {
                this._buf.write((byte) c);
                total++;
            } else if (c < 2048) {
                this._buf.write((byte) ((c >> 6) + 192));
                this._buf.write((byte) ((c & 63) + 128));
                total += 2;
            } else if (c < 65536) {
                this._buf.write((byte) ((c >> 12) + ChargeActivity.CODE_CHARGEING));
                this._buf.write((byte) (((c >> 6) & 63) + 128));
                this._buf.write((byte) ((c & 63) + 128));
                total += 3;
            } else {
                this._buf.write((byte) ((c >> 18) + 240));
                this._buf.write((byte) (((c >> 12) & 63) + 128));
                this._buf.write((byte) (((c >> 6) & 63) + 128));
                this._buf.write((byte) ((c & 63) + 128));
                total += 4;
            }
            i += Character.charCount(c);
        }
        this._buf.write(0);
        return total + 1;
    }

    public void writeInt(int x) {
        this._buf.writeInt(x);
    }

    public void writeLong(long x) {
        this._buf.writeLong(x);
    }

    public void writeCString(String s) {
        _put(s);
    }
}
