package org.bson.types;

import java.io.Serializable;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import android.os.Process;

public class ObjectId implements Comparable<ObjectId>, Serializable {
    private static final int _genmachine;
    private static final long serialVersionUID = -4415279469780082174L;
    final int _inc;
    final int _machine;
    boolean _new;
    final int _time;
    static final Logger LOGGER = Logger.getLogger(ObjectId.class.getName());
    private static final AtomicInteger _nextInc = new AtomicInteger(new Random().nextInt());

    static {
        int machinePiece;
        try {
            // Obtain a unique machine identifier
            StringBuilder sb = new StringBuilder();
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface ni = e.nextElement();
                sb.append(ni.toString());
            }
            machinePiece = sb.toString().hashCode() << 16;
            LOGGER.fine("machine piece post: " + Integer.toHexString(machinePiece));

            // Obtain process ID and class loader ID
            int processId = Process.myPid(); // Use Process.myPid() for Android
            int loaderId = System.identityHashCode(ObjectId.class.getClassLoader());
            int processPiece = (Integer.toHexString(processId) + Integer.toHexString(loaderId)).hashCode() & 65535;
            LOGGER.fine("process piece: " + Integer.toHexString(processPiece));

            // Combine machinePiece and processPiece to form the unique machine identifier
            _genmachine = machinePiece | processPiece;
            LOGGER.fine("machine : " + Integer.toHexString(_genmachine));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ObjectId get() {
        return new ObjectId();
    }

    public static boolean isValid(String s) {
        int len;
        if (s == null || (len = s.length()) != 24) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if ((c < '0' || c > '9') && ((c < 'a' || c > 'f') && (c < 'A' || c > 'F'))) {
                return false;
            }
        }
        return true;
    }

    public static ObjectId massageToObjectId(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof ObjectId) {
            return (ObjectId) o;
        }
        if (o instanceof String) {
            String s = o.toString();
            if (isValid(s)) {
                return new ObjectId(s);
            }
        }
        return null;
    }

    public ObjectId(Date time) {
        this(time, _genmachine, _nextInc.getAndIncrement());
    }

    public ObjectId(Date time, int inc) {
        this(time, _genmachine, inc);
    }

    public ObjectId(Date time, int machine, int inc) {
        this._time = (int) (time.getTime() / 1000);
        this._machine = machine;
        this._inc = inc;
        this._new = false;
    }

    public ObjectId(String s) {
        this(s, false);
    }

    public ObjectId(String s, boolean babble) {
        if (!isValid(s)) {
            throw new IllegalArgumentException("invalid ObjectId [" + s + "]");
        }
        s = babble ? babbleToMongod(s) : s;
        byte[] b = new byte[12];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) Integer.parseInt(s.substring(i * 2, (i * 2) + 2), 16);
        }
        ByteBuffer bb = ByteBuffer.wrap(b);
        this._time = bb.getInt();
        this._machine = bb.getInt();
        this._inc = bb.getInt();
        this._new = false;
    }

    public ObjectId(byte[] b) {
        if (b.length != 12) {
            throw new IllegalArgumentException("need 12 bytes");
        }
        ByteBuffer bb = ByteBuffer.wrap(b);
        this._time = bb.getInt();
        this._machine = bb.getInt();
        this._inc = bb.getInt();
        this._new = false;
    }

    public ObjectId(int time, int machine, int inc) {
        this._time = time;
        this._machine = machine;
        this._inc = inc;
        this._new = false;
    }

    public ObjectId() {
        this._time = (int) (System.currentTimeMillis() / 1000);
        this._machine = _genmachine;
        this._inc = _nextInc.getAndIncrement();
        this._new = true;
    }

    @Override
    public int hashCode() {
        int x = this._time;
        return x + (this._machine * 111) + (this._inc * 17);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        ObjectId other = massageToObjectId(o);
        if (other == null) {
            return false;
        }
        return this._time == other._time && this._machine == other._machine && this._inc == other._inc;
    }

    public String toStringBabble() {
        return babbleToMongod(toStringMongod());
    }

    public String toStringMongod() {
        byte[] b = toByteArray();
        StringBuilder buf = new StringBuilder(24);
        for (byte b2 : b) {
            int x = b2 & 0xFF;
            String s = Integer.toHexString(x);
            if (s.length() == 1) {
                buf.append("0");
            }
            buf.append(s);
        }
        return buf.toString();
    }

    public byte[] toByteArray() {
        byte[] b = new byte[12];
        ByteBuffer bb = ByteBuffer.wrap(b);
        bb.putInt(this._time);
        bb.putInt(this._machine);
        bb.putInt(this._inc);
        return b;
    }

    static String _pos(String s, int p) {
        return s.substring(p * 2, (p * 2) + 2);
    }

    public static String babbleToMongod(String b) {
        if (!isValid(b)) {
            throw new IllegalArgumentException("invalid object id: " + b);
        }
        StringBuilder buf = new StringBuilder(24);
        for (int i = 7; i >= 0; i--) {
            buf.append(_pos(b, i));
        }
        for (int i2 = 11; i2 >= 8; i2--) {
            buf.append(_pos(b, i2));
        }
        return buf.toString();
    }

    @Override
    public String toString() {
        return toStringMongod();
    }

    int _compareUnsigned(int i, int j) {
        long li = 0xFFFFFFFFL & i;
        long lj = 0xFFFFFFFFL & j;
        long diff = li - lj;
        if (diff < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        if (diff > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) diff;
    }

    @Override
    public int compareTo(ObjectId id) {
        if (id == null) {
            return -1;
        }
        int x = _compareUnsigned(this._time, id._time);
        if (x == 0) {
            int x2 = _compareUnsigned(this._machine, id._machine);
            return x2 == 0 ? _compareUnsigned(this._inc, id._inc) : x2;
        }
        return x;
    }

    public int getMachine() {
        return this._machine;
    }

    public long getTime() {
        return this._time * 1000L;
    }

    public int getTimeSecond() {
        return this._time;
    }

    public int getInc() {
        return this._inc;
    }

    public int _time() {
        return this._time;
    }

    public int _machine() {
        return this._machine;
    }

    public int _inc() {
        return this._inc;
    }

    public boolean isNew() {
        return this._new;
    }

    public void notNew() {
        this._new = false;
    }

    public static int getGenMachineId() {
        return _genmachine;
    }

    public static int getCurrentInc() {
        return _nextInc.get();
    }

    public static int _flip(int x) {
        int z = 0 | ((x << 24) & 0xFF000000);
        return z | ((x << 8) & 0x00FF0000) | ((x >> 8) & 0x0000FF00) | ((x >> 24) & 0x000000FF);
    }
}
