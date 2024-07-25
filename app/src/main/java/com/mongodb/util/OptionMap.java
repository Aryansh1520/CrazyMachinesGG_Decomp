package com.mongodb.util;

import java.util.TreeMap;

/* loaded from: classes.dex */
public class OptionMap extends TreeMap<String, String> {
    private static final long serialVersionUID = -4415279469780082174L;

    public int getInt(String name, int def) {
        return StringParseUtil.parseIfInt(get(name), def);
    }
}
