package org.bson.types;

import java.io.Serializable;
import java.util.Date;

/* loaded from: classes.dex */
public class BSONTimestamp implements Comparable<BSONTimestamp>, Serializable {
    static final boolean D = Boolean.getBoolean("DEBUG.DBTIMESTAMP");
    private static final long serialVersionUID = -3268482672267936464L;
    final int _inc;
    final Date _time;

    public BSONTimestamp() {
        this._inc = 0;
        this._time = null;
    }

    public BSONTimestamp(int time, int inc) {
        this._time = new Date(time * 1000);
        this._inc = inc;
    }

    public int getTime() {
        if (this._time == null) {
            return 0;
        }
        return (int) (this._time.getTime() / 1000);
    }

    public int getInc() {
        return this._inc;
    }

    public String toString() {
        return "TS time:" + this._time + " inc:" + this._inc;
    }

    @Override // java.lang.Comparable
    public int compareTo(BSONTimestamp ts) {
        return getTime() != ts.getTime() ? getTime() - ts.getTime() : getInc() - ts.getInc();
    }

    public int hashCode() {
        int result = this._inc + 31;
        return (result * 31) + getTime();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BSONTimestamp)) {
            return false;
        }
        BSONTimestamp t2 = (BSONTimestamp) obj;
        return getTime() == t2.getTime() && getInc() == t2.getInc();
    }
}
