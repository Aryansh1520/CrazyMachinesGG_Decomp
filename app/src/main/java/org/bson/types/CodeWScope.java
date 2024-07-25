package org.bson.types;

import org.bson.BSONObject;

/* loaded from: classes.dex */
public class CodeWScope extends Code {
    private static final long serialVersionUID = -6284832275113680002L;
    final BSONObject _scope;

    public CodeWScope(String code, BSONObject scope) {
        super(code);
        this._scope = scope;
    }

    public BSONObject getScope() {
        return this._scope;
    }

    @Override // org.bson.types.Code
    public boolean equals(Object o) {
        if (!(o instanceof CodeWScope)) {
            return false;
        }
        CodeWScope c = (CodeWScope) o;
        return this._code.equals(c._code) && this._scope.equals(c._scope);
    }

    @Override // org.bson.types.Code
    public int hashCode() {
        return this._code.hashCode() ^ this._scope.hashCode();
    }
}
