package com.mongodb.util;

import com.mongodb.Bytes;
import java.util.List;
import org.bson.util.ClassMap;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ClassMapBasedObjectSerializer extends AbstractObjectSerializer {
    private ClassMap<ObjectSerializer> _serializers = new ClassMap<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addObjectSerializer(Class c, ObjectSerializer serializer) {
        this._serializers.put(c, serializer);
    }

    @Override // com.mongodb.util.ObjectSerializer
    public void serialize(Object obj, StringBuilder buf) {
        Object obj2 = Bytes.applyEncodingHooks(obj);
        if (obj2 == null) {
            buf.append(" null ");
            return;
        }
        ObjectSerializer serializer = null;
        List<Class<?>> ancestors = ClassMap.getAncestry(obj2.getClass());
        for (Class<?> ancestor : ancestors) {
            ObjectSerializer serializer2 = this._serializers.get(ancestor);
            serializer = serializer2;
            if (serializer != null) {
                break;
            }
        }
        if (serializer == null && obj2.getClass().isArray()) {
            ObjectSerializer serializer3 = this._serializers.get(Object[].class);
            serializer = serializer3;
        }
        if (serializer == null) {
            throw new RuntimeException("json can't serialize type : " + obj2.getClass());
        }
        serializer.serialize(obj2, buf);
    }
}
