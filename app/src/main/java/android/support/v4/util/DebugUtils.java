package android.support.v4.util;

import com.mappn.sdk.pay.chargement.ChargeActivity;

/* loaded from: classes.dex */
public class DebugUtils {
    public static void buildShortClassTag(Object cls, StringBuilder out) {
        int end;
        if (cls == null) {
            out.append(ChargeActivity.TYPE_CHARGE_TYPE_LIST);
            return;
        }
        String simpleName = cls.getClass().getSimpleName();
        if ((simpleName == null || simpleName.length() <= 0) && (end = (simpleName = cls.getClass().getName()).lastIndexOf(46)) > 0) {
            simpleName = simpleName.substring(end + 1);
        }
        out.append(simpleName);
        out.append('{');
        out.append(Integer.toHexString(System.identityHashCode(cls)));
    }
}
