package com.mongodb;

import com.mokredit.payment.StringUtils;
import com.mongodb.Bytes;
import com.mongodb.DBApiLayer;
import com.mongodb.util.Util;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import org.bson.BSONObject;

/* loaded from: classes.dex */
public abstract class DB {
    private static final Set<String> _obedientCommands = new HashSet();
    private WriteConcern _concern;
    final Mongo _mongo;
    final String _name;
    final Bytes.OptionHolder _options;
    private ReadPreference _readPref;
    protected boolean _readOnly = false;
    private AtomicReference<AuthenticationCredentials> authenticationCredentialsReference = new AtomicReference<>();

    public abstract void cleanCursors(boolean z);

    protected abstract DBCollection doGetCollection(String str);

    public abstract void requestDone();

    public abstract void requestEnsureConnection();

    public abstract void requestStart();

    static {
        _obedientCommands.add("group");
        _obedientCommands.add("aggregate");
        _obedientCommands.add("collStats");
        _obedientCommands.add("dbStats");
        _obedientCommands.add("count");
        _obedientCommands.add("distinct");
        _obedientCommands.add("geoNear");
        _obedientCommands.add("geoSearch");
        _obedientCommands.add("geoWalk");
    }

    public DB(Mongo mongo, String name) {
        this._mongo = mongo;
        this._name = name;
        this._options = new Bytes.OptionHolder(this._mongo._netOptions);
    }

    ReadPreference getCommandReadPreference(DBObject command, ReadPreference requestedPreference) {
        boolean primaryRequired = true;
        String comString = command.keySet().iterator().next();
        if (comString.equals("getnonce") || comString.equals("authenticate")) {
            ReadPreference requestedPreference2 = ReadPreference.primaryPreferred();
            return requestedPreference2;
        }
        if (comString.equals("mapreduce")) {
            Object out = command.get("out");
            if (out instanceof BSONObject) {
                BSONObject outMap = (BSONObject) out;
                if (outMap.get("inline") != null) {
                    primaryRequired = false;
                }
            } else {
                primaryRequired = true;
            }
        } else if (_obedientCommands.contains(comString)) {
            primaryRequired = false;
        }
        if (primaryRequired) {
            ReadPreference requestedPreference3 = ReadPreference.primary();
            return requestedPreference3;
        }
        return requestedPreference;
    }

    public DBCollection getCollection(String name) {
        DBCollection c = doGetCollection(name);
        return c;
    }

    public DBCollection createCollection(String name, DBObject options) {
        if (options != null) {
            DBObject createCmd = new BasicDBObject("create", name);
            createCmd.putAll(options);
            CommandResult result = command(createCmd);
            result.throwOnError();
        }
        return getCollection(name);
    }

    public DBCollection getCollectionFromString(String s) {
        DBCollection foo = null;
        int idx = s.indexOf(".");
        while (idx >= 0) {
            String b = s.substring(0, idx);
            s = s.substring(idx + 1);
            if (foo == null) {
                foo = getCollection(b);
            } else {
                foo = foo.getCollection(b);
            }
            idx = s.indexOf(".");
        }
        return foo != null ? foo.getCollection(s) : getCollection(s);
    }

    public CommandResult command(DBObject cmd) {
        return command(cmd, 0);
    }

    public CommandResult command(DBObject cmd, DBEncoder encoder) {
        return command(cmd, 0, encoder);
    }

    public CommandResult command(DBObject cmd, int options, DBEncoder encoder) {
        return command(cmd, options, getReadPreference(), encoder);
    }

    public CommandResult command(DBObject cmd, int options, ReadPreference readPrefs) {
        return command(cmd, options, readPrefs, DefaultDBEncoder.FACTORY.create());
    }

    public CommandResult command(DBObject cmd, int options, ReadPreference readPrefs, DBEncoder encoder) {
        Iterator<DBObject> i = getCollection("$cmd").__find(cmd, new BasicDBObject(), 0, -1, 0, options, getCommandReadPreference(cmd, readPrefs), DefaultDBDecoder.FACTORY.create(), encoder);
        if (i == null || !i.hasNext()) {
            return null;
        }
        DBObject res = i.next();
        ServerAddress sa = i instanceof DBApiLayer.Result ? ((DBApiLayer.Result) i).getServerAddress() : null;
        CommandResult cr = new CommandResult(cmd, sa);
        cr.putAll(res);
        return cr;
    }

    public CommandResult command(DBObject cmd, int options) {
        return command(cmd, options, getReadPreference());
    }

    public CommandResult command(String cmd) {
        return command(new BasicDBObject(cmd, Boolean.TRUE));
    }

    public CommandResult command(String cmd, int options) {
        return command(new BasicDBObject(cmd, Boolean.TRUE), options);
    }

    public CommandResult doEval(String code, Object... args) {
        return command(BasicDBObjectBuilder.start().add("$eval", code).add("args", args).get());
    }

    public Object eval(String code, Object... args) {
        CommandResult res = doEval(code, args);
        res.throwOnError();
        return res.get("retval");
    }

    public CommandResult getStats() {
        return command("dbstats");
    }

    public String getName() {
        return this._name;
    }

    public void setReadOnly(Boolean b) {
        this._readOnly = b.booleanValue();
    }

    public Set<String> getCollectionNames() {
        DBCollection namespaces = getCollection("system.namespaces");
        if (namespaces == null) {
            throw new RuntimeException("this is impossible");
        }
        Iterator<DBObject> i = namespaces.__find(new BasicDBObject(), null, 0, 0, 0, getOptions(), getReadPreference(), null);
        if (i == null) {
            return new HashSet();
        }
        List<String> tables = new ArrayList<>();
        while (i.hasNext()) {
            DBObject o = i.next();
            if (o.get("name") == null) {
                throw new MongoException("how is name null : " + o);
            }
            String n = o.get("name").toString();
            int idx = n.indexOf(".");
            String root = n.substring(0, idx);
            if (root.equals(this._name) && n.indexOf("$") < 0) {
                String table = n.substring(idx + 1);
                tables.add(table);
            }
        }
        Collections.sort(tables);
        return new LinkedHashSet(tables);
    }

    public boolean collectionExists(String collectionName) {
        if (collectionName == null || StringUtils.EMPTY.equals(collectionName)) {
            return false;
        }
        Set<String> collections = getCollectionNames();
        if (collections.isEmpty()) {
            return false;
        }
        for (String collection : collections) {
            if (collectionName.equalsIgnoreCase(collection)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return this._name;
    }

    public CommandResult getLastError() {
        return command(new BasicDBObject("getlasterror", 1));
    }

    public CommandResult getLastError(WriteConcern concern) {
        return command(concern.getCommand());
    }

    public CommandResult getLastError(int w, int wtimeout, boolean fsync) {
        return command(new WriteConcern(w, wtimeout, fsync).getCommand());
    }

    public void setWriteConcern(WriteConcern concern) {
        if (concern == null) {
            throw new IllegalArgumentException();
        }
        this._concern = concern;
    }

    public WriteConcern getWriteConcern() {
        return this._concern != null ? this._concern : this._mongo.getWriteConcern();
    }

    public void setReadPreference(ReadPreference preference) {
        this._readPref = preference;
    }

    public ReadPreference getReadPreference() {
        return this._readPref != null ? this._readPref : this._mongo.getReadPreference();
    }

    public void dropDatabase() {
        CommandResult res = command(new BasicDBObject("dropDatabase", 1));
        res.throwOnError();
        this._mongo._dbs.remove(getName());
    }

    public boolean isAuthenticated() {
        return this.authenticationCredentialsReference.get() != null;
    }

    public boolean authenticate(String username, char[] password) {
        if (this.authenticationCredentialsReference.get() != null) {
            throw new IllegalStateException("can't authenticate twice on the same database");
        }
        AuthenticationCredentials newCredentials = new AuthenticationCredentials(username, password);
        CommandResult res = newCredentials.authenticate();
        if (!res.ok()) {
            return false;
        }
        boolean wasNull = this.authenticationCredentialsReference.compareAndSet(null, newCredentials);
        if (!wasNull) {
            throw new IllegalStateException("can't authenticate twice on the same database");
        }
        return true;
    }

    public synchronized CommandResult authenticateCommand(String username, char[] password) {
        CommandResult res;
        if (this.authenticationCredentialsReference.get() != null) {
            throw new IllegalStateException("can't authenticate twice on the same database");
        }
        AuthenticationCredentials newCredentials = new AuthenticationCredentials(username, password);
        res = newCredentials.authenticate();
        res.throwOnError();
        boolean wasNull = this.authenticationCredentialsReference.compareAndSet(null, newCredentials);
        if (!wasNull) {
            throw new IllegalStateException("can't authenticate twice on the same database");
        }
        return res;
    }

    public WriteResult addUser(String username, char[] passwd) {
        return addUser(username, passwd, false);
    }

    public WriteResult addUser(String username, char[] passwd, boolean readOnly) {
        DBCollection c = getCollection("system.users");
        DBObject o = c.findOne((DBObject) new BasicDBObject("user", username));
        if (o == null) {
            o = new BasicDBObject("user", username);
        }
        o.put("pwd", _hash(username, passwd));
        o.put("readOnly", Boolean.valueOf(readOnly));
        return c.save(o);
    }

    public WriteResult removeUser(String username) {
        DBCollection c = getCollection("system.users");
        return c.remove(new BasicDBObject("user", username));
    }

    String _hash(String username, char[] passwd) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream(username.length() + 20 + passwd.length);
        try {
            bout.write(username.getBytes());
            bout.write(":mongo:".getBytes());
            for (int i = 0; i < passwd.length; i++) {
                if (passwd[i] >= 128) {
                    throw new IllegalArgumentException("can't handle non-ascii passwords yet");
                }
                bout.write((byte) passwd[i]);
            }
            return Util.hexMD5(bout.toByteArray());
        } catch (IOException ioe) {
            throw new RuntimeException("impossible", ioe);
        }
    }

    public CommandResult getPreviousError() {
        return command(new BasicDBObject("getpreverror", 1));
    }

    public void resetError() {
        command(new BasicDBObject("reseterror", 1));
    }

    public void forceError() {
        command(new BasicDBObject("forceerror", 1));
    }

    public Mongo getMongo() {
        return this._mongo;
    }

    public DB getSisterDB(String name) {
        return this._mongo.getDB(name);
    }

    @Deprecated
    public void slaveOk() {
        addOption(4);
    }

    public void addOption(int option) {
        this._options.add(option);
    }

    public void setOptions(int options) {
        this._options.set(options);
    }

    public void resetOptions() {
        this._options.reset();
    }

    public int getOptions() {
        return this._options.get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AuthenticationCredentials getAuthenticationCredentials() {
        return this.authenticationCredentialsReference.get();
    }

    /* loaded from: classes.dex */
    class AuthenticationCredentials {
        private final byte[] authHash;
        private final String userName;

        private AuthenticationCredentials(String userName, char[] password) {
            if (userName == null) {
                throw new IllegalArgumentException("userName can not be null");
            }
            if (password == null) {
                throw new IllegalArgumentException("password can not be null");
            }
            this.userName = userName;
            this.authHash = createHash(userName, password);
        }

        CommandResult authenticate() {
            DB.this.requestStart();
            try {
                CommandResult res = DB.this.command(getNonceCommand());
                res.throwOnError();
                return DB.this.command(getAuthCommand(res.getString("nonce")));
            } finally {
                DB.this.requestDone();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public DBObject getAuthCommand(String nonce) {
            String key = nonce + this.userName + new String(this.authHash);
            BasicDBObject cmd = new BasicDBObject();
            cmd.put("authenticate", (Object) 1);
            cmd.put("user", (Object) this.userName);
            cmd.put("nonce", (Object) nonce);
            cmd.put("key", (Object) Util.hexMD5(key.getBytes()));
            return cmd;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public BasicDBObject getNonceCommand() {
            return new BasicDBObject("getnonce", 1);
        }

        private byte[] createHash(String userName, char[] password) {
            ByteArrayOutputStream bout = new ByteArrayOutputStream(userName.length() + 20 + password.length);
            try {
                bout.write(userName.getBytes());
                bout.write(":mongo:".getBytes());
                for (char ch : password) {
                    if (ch >= 128) {
                        throw new IllegalArgumentException("can't handle non-ascii passwords yet");
                    }
                    bout.write((byte) ch);
                }
                return Util.hexMD5(bout.toByteArray()).getBytes();
            } catch (IOException ioe) {
                throw new RuntimeException("impossible", ioe);
            }
        }
    }
}
