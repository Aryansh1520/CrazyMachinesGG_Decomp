package com.mongodb.gridfs;

import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.Util;
import java.io.File;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Iterator;

/* loaded from: classes.dex */
public class CLI {
    private static GridFS _gridfs;
    private static String host = "127.0.0.1";
    private static String db = "test";
    private static Mongo _mongo = null;

    private static void printUsage() {
        System.out.println("Usage : [--bucket bucketname] action");
        System.out.println("  where  action is one of:");
        System.out.println("      list                      : lists all files in the store");
        System.out.println("      put filename              : puts the file filename into the store");
        System.out.println("      get filename1 filename2   : gets filename1 from store and sends to filename2");
        System.out.println("      md5 filename              : does an md5 hash on a file in the db (for testing)");
    }

    private static Mongo getMongo() throws Exception {
        if (_mongo == null) {
            _mongo = new Mongo(host);
        }
        return _mongo;
    }

    private static GridFS getGridFS() throws Exception {
        if (_gridfs == null) {
            _gridfs = new GridFS(getMongo().getDB(db));
        }
        return _gridfs;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            printUsage();
            return;
        }
        for (int i = 0; i < args.length; i = i + 1 + 1) {
            String s = args[i];
            if (s.equals("--db")) {
                db = args[i + 1];
            } else if (s.equals("--host")) {
                host = args[i + 1];
            } else {
                if (s.equals("help")) {
                    printUsage();
                    return;
                }
                if (s.equals("list")) {
                    GridFS fs = getGridFS();
                    System.out.printf("%-60s %-10s\n", "Filename", "Length");
                    Iterator i$ = fs.getFileList().iterator();
                    while (i$.hasNext()) {
                        DBObject o = i$.next();
                        System.out.printf("%-60s %-10d\n", o.get("filename"), Long.valueOf(((Number) o.get("length")).longValue()));
                    }
                    return;
                }
                if (s.equals("get")) {
                    GridFS fs2 = getGridFS();
                    String fn = args[i + 1];
                    GridFSDBFile f = fs2.findOne(fn);
                    if (f == null) {
                        System.err.println("can't find file: " + fn);
                        return;
                    } else {
                        f.writeTo(f.getFilename());
                        return;
                    }
                }
                if (s.equals("put")) {
                    GridFS fs3 = getGridFS();
                    GridFSInputFile f2 = fs3.createFile(new File(args[i + 1]));
                    f2.save();
                    f2.validate();
                    return;
                }
                if (s.equals("md5")) {
                    GridFS fs4 = getGridFS();
                    String fn2 = args[i + 1];
                    GridFSDBFile f3 = fs4.findOne(fn2);
                    if (f3 == null) {
                        System.err.println("can't find file: " + fn2);
                        return;
                    }
                    MessageDigest md5 = MessageDigest.getInstance("MD5");
                    md5.reset();
                    DigestInputStream is = new DigestInputStream(f3.getInputStream(), md5);
                    int read = 0;
                    while (is.read() >= 0) {
                        read++;
                        int r = is.read(new byte[17]);
                        if (r < 0) {
                            break;
                        } else {
                            read += r;
                        }
                    }
                    byte[] digest = md5.digest();
                    System.out.println("length: " + read + " md5: " + Util.toHex(digest));
                    return;
                }
                System.err.println("unknown option: " + s);
                return;
            }
        }
    }
}
