package org.bson;

import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public interface BSONObject {
    boolean containsField(String str);

    @Deprecated
    boolean containsKey(String str);

    Object get(String str);

    Set<String> keySet();

    Object put(String str, Object obj);

    void putAll(Map map);

    void putAll(BSONObject bSONObject);

    Object removeField(String str);

    Map toMap();
}
