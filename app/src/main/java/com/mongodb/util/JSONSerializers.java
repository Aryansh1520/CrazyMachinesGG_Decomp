package com.mongodb.util;

import com.mongodb.BasicDBObject;
import com.mongodb.Bytes;
import com.mongodb.DBObject;
import com.mongodb.DBRefBase;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.UUID;
import java.util.regex.Pattern;
import org.bson.types.BSONTimestamp;
import org.bson.types.Binary;
import org.bson.types.Code;
import org.bson.types.CodeWScope;
import org.bson.types.MaxKey;
import org.bson.types.MinKey;
import org.bson.types.ObjectId;

/* loaded from: classes.dex */
public class JSONSerializers {
    private JSONSerializers() {
    }

    public static ObjectSerializer getLegacy() {
        ClassMapBasedObjectSerializer serializer = addCommonSerializers();
        serializer.addObjectSerializer(Date.class, new LegacyDateSerializer(serializer));
        serializer.addObjectSerializer(BSONTimestamp.class, new LegacyBSONTimestampSerializer(serializer));
        serializer.addObjectSerializer(Binary.class, new LegacyBinarySerializer());
        serializer.addObjectSerializer(byte[].class, new LegacyBinarySerializer());
        return serializer;
    }

    public static ObjectSerializer getStrict() {
        ClassMapBasedObjectSerializer serializer = addCommonSerializers();
        serializer.addObjectSerializer(Date.class, new DateSerializer(serializer));
        serializer.addObjectSerializer(BSONTimestamp.class, new BSONTimestampSerializer(serializer));
        serializer.addObjectSerializer(Binary.class, new BinarySerializer(serializer));
        serializer.addObjectSerializer(byte[].class, new ByteArraySerializer(serializer));
        return serializer;
    }

    static ClassMapBasedObjectSerializer addCommonSerializers() {
        ClassMapBasedObjectSerializer serializer = new ClassMapBasedObjectSerializer();
        serializer.addObjectSerializer(Object[].class, new ObjectArraySerializer(serializer));
        serializer.addObjectSerializer(Boolean.class, new ToStringSerializer());
        serializer.addObjectSerializer(Code.class, new CodeSerializer(serializer));
        serializer.addObjectSerializer(CodeWScope.class, new CodeWScopeSerializer(serializer));
        serializer.addObjectSerializer(DBObject.class, new DBObjectSerializer(serializer));
        serializer.addObjectSerializer(DBRefBase.class, new DBRefBaseSerializer(serializer));
        serializer.addObjectSerializer(Iterable.class, new IterableSerializer(serializer));
        serializer.addObjectSerializer(Map.class, new MapSerializer(serializer));
        serializer.addObjectSerializer(MaxKey.class, new MaxKeySerializer(serializer));
        serializer.addObjectSerializer(MinKey.class, new MinKeySerializer(serializer));
        serializer.addObjectSerializer(Number.class, new ToStringSerializer());
        serializer.addObjectSerializer(ObjectId.class, new ObjectIdSerializer(serializer));
        serializer.addObjectSerializer(Pattern.class, new PatternSerializer(serializer));
        serializer.addObjectSerializer(String.class, new StringSerializer());
        serializer.addObjectSerializer(UUID.class, new UUIDSerializer(serializer));
        return serializer;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static abstract class CompoundObjectSerializer extends AbstractObjectSerializer {
        protected final ObjectSerializer serializer;

        CompoundObjectSerializer(ObjectSerializer serializer) {
            this.serializer = serializer;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class LegacyBinarySerializer extends AbstractObjectSerializer {
        private LegacyBinarySerializer() {
        }

        @Override // com.mongodb.util.ObjectSerializer
        public void serialize(Object obj, StringBuilder buf) {
            buf.append("<Binary Data>");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ObjectArraySerializer extends CompoundObjectSerializer {
        ObjectArraySerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override // com.mongodb.util.ObjectSerializer
        public void serialize(Object obj, StringBuilder buf) {
            buf.append("[ ");
            for (int i = 0; i < Array.getLength(obj); i++) {
                if (i > 0) {
                    buf.append(" , ");
                }
                this.serializer.serialize(Array.get(obj, i), buf);
            }
            buf.append("]");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ToStringSerializer extends AbstractObjectSerializer {
        private ToStringSerializer() {
        }

        @Override // com.mongodb.util.ObjectSerializer
        public void serialize(Object obj, StringBuilder buf) {
            buf.append(obj.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class LegacyBSONTimestampSerializer extends CompoundObjectSerializer {
        LegacyBSONTimestampSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override // com.mongodb.util.ObjectSerializer
        public void serialize(Object obj, StringBuilder buf) {
            BSONTimestamp t = (BSONTimestamp) obj;
            BasicDBObject temp = new BasicDBObject();
            temp.put("$ts", (Object) Integer.valueOf(t.getTime()));
            temp.put("$inc", (Object) Integer.valueOf(t.getInc()));
            this.serializer.serialize(temp, buf);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CodeSerializer extends CompoundObjectSerializer {
        CodeSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override // com.mongodb.util.ObjectSerializer
        public void serialize(Object obj, StringBuilder buf) {
            Code c = (Code) obj;
            BasicDBObject temp = new BasicDBObject();
            temp.put("$code", (Object) c.getCode());
            this.serializer.serialize(temp, buf);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CodeWScopeSerializer extends CompoundObjectSerializer {
        CodeWScopeSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override // com.mongodb.util.ObjectSerializer
        public void serialize(Object obj, StringBuilder buf) {
            CodeWScope c = (CodeWScope) obj;
            BasicDBObject temp = new BasicDBObject();
            temp.put("$code", (Object) c.getCode());
            temp.put("$scope", (Object) c.getScope());
            this.serializer.serialize(temp, buf);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class LegacyDateSerializer extends CompoundObjectSerializer {
        LegacyDateSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override // com.mongodb.util.ObjectSerializer
        public void serialize(Object obj, StringBuilder buf) {
            Date d = (Date) obj;
            SimpleDateFormat format = new SimpleDateFormat(JSONCallback._msDateFormat);
            format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
            this.serializer.serialize(new BasicDBObject("$date", format.format(d)), buf);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class DBObjectSerializer extends CompoundObjectSerializer {
        DBObjectSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override // com.mongodb.util.ObjectSerializer
        public void serialize(Object obj, StringBuilder buf) {
            boolean first = true;
            buf.append("{ ");
            DBObject dbo = (DBObject) obj;
            for (String s : dbo.keySet()) {
                if (first) {
                    first = false;
                } else {
                    buf.append(" , ");
                }
                JSON.string(buf, s);
                buf.append(" : ");
                this.serializer.serialize(dbo.get(s), buf);
            }
            buf.append("}");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class DBRefBaseSerializer extends CompoundObjectSerializer {
        DBRefBaseSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override // com.mongodb.util.ObjectSerializer
        public void serialize(Object obj, StringBuilder buf) {
            DBRefBase ref = (DBRefBase) obj;
            BasicDBObject temp = new BasicDBObject();
            temp.put("$ref", (Object) ref.getRef());
            temp.put("$id", ref.getId());
            this.serializer.serialize(temp, buf);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class IterableSerializer extends CompoundObjectSerializer {
        IterableSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override // com.mongodb.util.ObjectSerializer
        public void serialize(Object obj, StringBuilder buf) {
            boolean first = true;
            buf.append("[ ");
            for (Object o : (Iterable) obj) {
                if (first) {
                    first = false;
                } else {
                    buf.append(" , ");
                }
                this.serializer.serialize(o, buf);
            }
            buf.append("]");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class MapSerializer extends CompoundObjectSerializer {
        MapSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override // com.mongodb.util.ObjectSerializer
        public void serialize(Object obj, StringBuilder buf) {
            boolean first = true;
            buf.append("{ ");
            Map m = (Map) obj;
            for (Object o : m.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                if (first) {
                    first = false;
                } else {
                    buf.append(" , ");
                }
                JSON.string(buf, entry.getKey().toString());
                buf.append(" : ");
                this.serializer.serialize(entry.getValue(), buf);
            }
            buf.append("}");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class MaxKeySerializer extends CompoundObjectSerializer {
        MaxKeySerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override // com.mongodb.util.ObjectSerializer
        public void serialize(Object obj, StringBuilder buf) {
            this.serializer.serialize(new BasicDBObject("$maxKey", 1), buf);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class MinKeySerializer extends CompoundObjectSerializer {
        MinKeySerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override // com.mongodb.util.ObjectSerializer
        public void serialize(Object obj, StringBuilder buf) {
            this.serializer.serialize(new BasicDBObject("$minKey", 1), buf);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ObjectIdSerializer extends CompoundObjectSerializer {
        ObjectIdSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override // com.mongodb.util.ObjectSerializer
        public void serialize(Object obj, StringBuilder buf) {
            this.serializer.serialize(new BasicDBObject("$oid", obj.toString()), buf);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class PatternSerializer extends CompoundObjectSerializer {
        PatternSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override // com.mongodb.util.ObjectSerializer
        public void serialize(Object obj, StringBuilder buf) {
            DBObject externalForm = new BasicDBObject();
            externalForm.put("$regex", obj.toString());
            if (((Pattern) obj).flags() != 0) {
                externalForm.put("$options", Bytes.regexFlags(((Pattern) obj).flags()));
            }
            this.serializer.serialize(externalForm, buf);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class StringSerializer extends AbstractObjectSerializer {
        private StringSerializer() {
        }

        @Override // com.mongodb.util.ObjectSerializer
        public void serialize(Object obj, StringBuilder buf) {
            JSON.string(buf, (String) obj);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class UUIDSerializer extends CompoundObjectSerializer {
        UUIDSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override // com.mongodb.util.ObjectSerializer
        public void serialize(Object obj, StringBuilder buf) {
            UUID uuid = (UUID) obj;
            BasicDBObject temp = new BasicDBObject();
            temp.put("$uuid", (Object) uuid.toString());
            this.serializer.serialize(temp, buf);
        }
    }

    /* loaded from: classes.dex */
    private static class BSONTimestampSerializer extends CompoundObjectSerializer {
        BSONTimestampSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override // com.mongodb.util.ObjectSerializer
        public void serialize(Object obj, StringBuilder buf) {
            BSONTimestamp t = (BSONTimestamp) obj;
            BasicDBObject temp = new BasicDBObject();
            temp.put("$t", (Object) Integer.valueOf(t.getTime()));
            temp.put("$i", (Object) Integer.valueOf(t.getInc()));
            BasicDBObject timestampObj = new BasicDBObject();
            timestampObj.put("$timestamp", (Object) temp);
            this.serializer.serialize(timestampObj, buf);
        }
    }

    /* loaded from: classes.dex */
    private static class DateSerializer extends CompoundObjectSerializer {
        DateSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override // com.mongodb.util.ObjectSerializer
        public void serialize(Object obj, StringBuilder buf) {
            Date d = (Date) obj;
            this.serializer.serialize(new BasicDBObject("$date", Long.valueOf(d.getTime())), buf);
        }
    }

    /* loaded from: classes.dex */
    private static abstract class BinarySerializerBase extends CompoundObjectSerializer {
        BinarySerializerBase(ObjectSerializer serializer) {
            super(serializer);
        }

        protected void serialize(byte[] bytes, byte type, StringBuilder buf) {
            DBObject temp = new BasicDBObject();
            temp.put("$binary", new Base64Codec().encode(bytes));
            temp.put("$type", Byte.valueOf(type));
            this.serializer.serialize(temp, buf);
        }
    }

    /* loaded from: classes.dex */
    private static class BinarySerializer extends BinarySerializerBase {
        BinarySerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override // com.mongodb.util.ObjectSerializer
        public void serialize(Object obj, StringBuilder buf) {
            Binary bin = (Binary) obj;
            serialize(bin.getData(), bin.getType(), buf);
        }
    }

    /* loaded from: classes.dex */
    private static class ByteArraySerializer extends BinarySerializerBase {
        ByteArraySerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override // com.mongodb.util.ObjectSerializer
        public void serialize(Object obj, StringBuilder buf) {
            serialize((byte[]) obj, (byte) 0, buf);
        }
    }
}
