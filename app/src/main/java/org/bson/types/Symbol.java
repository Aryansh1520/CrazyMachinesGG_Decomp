package org.bson.types;

import java.io.Serializable;

/* loaded from: classes.dex */
public class Symbol implements Serializable {
    private static final long serialVersionUID = 1326269319883146072L;
    private final String _symbol;

    public Symbol(String s) {
        this._symbol = s;
    }

    public String getSymbol() {
        return this._symbol;
    }

    public boolean equals(Object o) {
        String otherSymbol;
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o instanceof Symbol) {
            otherSymbol = ((Symbol) o)._symbol;
        } else {
            if (!(o instanceof String)) {
                return false;
            }
            otherSymbol = (String) o;
        }
        if (this._symbol != null) {
            if (this._symbol.equals(otherSymbol)) {
                return true;
            }
        } else if (otherSymbol == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this._symbol != null) {
            return this._symbol.hashCode();
        }
        return 0;
    }

    public String toString() {
        return this._symbol;
    }
}
