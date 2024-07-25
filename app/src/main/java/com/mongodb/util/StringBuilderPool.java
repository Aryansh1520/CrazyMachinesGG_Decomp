package com.mongodb.util;

/* loaded from: classes.dex */
public class StringBuilderPool extends SimplePool<StringBuilder> {
    public StringBuilderPool(String name, int maxToKeep) {
        super("StringBuilderPool-" + name, maxToKeep);
    }

    @Override // com.mongodb.util.SimplePool
    public StringBuilder createNew() {
        return new StringBuilder();
    }

    public boolean ok(StringBuilder buf) {
        if (buf.length() > getMaxSize()) {
            return false;
        }
        buf.setLength(0);
        return true;
    }

    protected long memSize(StringBuilder buf) {
        return buf.length() * 2;
    }
}
