package org.bson.types;

import java.io.Serializable;

/* loaded from: classes.dex */
public class Binary implements Serializable {
    private static final long serialVersionUID = 7902997490338209467L;
    final byte[] _data;
    final byte _type;

    public Binary(byte[] data) {
        this((byte) 0, data);
    }

    public Binary(byte type, byte[] data) {
        this._type = type;
        this._data = data;
    }

    public byte getType() {
        return this._type;
    }

    public byte[] getData() {
        return this._data;
    }

    public int length() {
        return this._data.length;
    }
}
