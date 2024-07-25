package com.mongodb;

import com.mongodb.Bytes;
import com.mongodb.MapReduceCommand;
import com.mongodb.ReflectionDBObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bson.types.ObjectId;

/* loaded from: classes.dex */
public abstract class DBCollection {
    final DB _db;
    private DBDecoderFactory _decoderFactory;
    private DBEncoderFactory _encoderFactory;
    protected final String _fullName;
    protected List<DBObject> _hintFields;
    protected final String _name;
    final Bytes.OptionHolder _options;
    private WriteConcern _concern = null;
    private ReadPreference _readPref = null;
    protected Class _objectClass = null;
    private Map<String, Class> _internalClass = Collections.synchronizedMap(new HashMap());
    private ReflectionDBObject.JavaWrapper _wrapper = null;
    private final Set<String> _createdIndexes = new HashSet();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract Iterator<DBObject> __find(DBObject dBObject, DBObject dBObject2, int i, int i2, int i3, int i4, ReadPreference readPreference, DBDecoder dBDecoder);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract Iterator<DBObject> __find(DBObject dBObject, DBObject dBObject2, int i, int i2, int i3, int i4, ReadPreference readPreference, DBDecoder dBDecoder, DBEncoder dBEncoder);

    public abstract void createIndex(DBObject dBObject, DBObject dBObject2, DBEncoder dBEncoder);

    protected abstract void doapply(DBObject dBObject);

    public abstract WriteResult insert(DBObject[] dBObjectArr, WriteConcern writeConcern, DBEncoder dBEncoder);

    public abstract WriteResult remove(DBObject dBObject, WriteConcern writeConcern, DBEncoder dBEncoder);

    public abstract WriteResult update(DBObject dBObject, DBObject dBObject2, boolean z, boolean z2, WriteConcern writeConcern, DBEncoder dBEncoder);

    public WriteResult insert(DBObject[] arr, WriteConcern concern) {
        return insert(arr, concern, getDBEncoder());
    }

    public WriteResult insert(DBObject o, WriteConcern concern) {
        return insert(new DBObject[]{o}, concern);
    }

    public WriteResult insert(DBObject... arr) {
        return insert(arr, getWriteConcern());
    }

    public WriteResult insert(WriteConcern concern, DBObject... arr) {
        return insert(arr, concern);
    }

    public WriteResult insert(List<DBObject> list) {
        return insert(list, getWriteConcern());
    }

    public WriteResult insert(List<DBObject> list, WriteConcern concern) {
        return insert((DBObject[]) list.toArray(new DBObject[list.size()]), concern);
    }

    public WriteResult update(DBObject q, DBObject o, boolean upsert, boolean multi, WriteConcern concern) {
        return update(q, o, upsert, multi, concern, getDBEncoder());
    }

    public WriteResult update(DBObject q, DBObject o, boolean upsert, boolean multi) {
        return update(q, o, upsert, multi, getWriteConcern());
    }

    public WriteResult update(DBObject q, DBObject o) {
        return update(q, o, false, false);
    }

    public WriteResult updateMulti(DBObject q, DBObject o) {
        return update(q, o, false, true);
    }

    public WriteResult remove(DBObject o, WriteConcern concern) {
        return remove(o, concern, getDBEncoder());
    }

    public WriteResult remove(DBObject o) {
        return remove(o, getWriteConcern());
    }

    @Deprecated
    public DBCursor find(DBObject query, DBObject fields, int numToSkip, int batchSize, int options) {
        return find(query, fields, numToSkip, batchSize).addOption(options);
    }

    @Deprecated
    public DBCursor find(DBObject query, DBObject fields, int numToSkip, int batchSize) {
        DBCursor cursor = find(query, fields).skip(numToSkip).batchSize(batchSize);
        return cursor;
    }

    public DBObject findOne(Object obj) {
        return findOne(obj, (DBObject) null);
    }

    public DBObject findOne(Object obj, DBObject fields) {
        Iterator<DBObject> iterator = __find(new BasicDBObject("_id", obj), fields, 0, -1, 0, getOptions(), getReadPreference(), getDecoder());
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

    public DBObject findAndModify(DBObject query, DBObject fields, DBObject sort, boolean remove, DBObject update, boolean returnNew, boolean upsert) {
        BasicDBObject cmd = new BasicDBObject("findandmodify", this._name);
        if (query != null && !query.keySet().isEmpty()) {
            cmd.append("query", (Object) query);
        }
        if (fields != null && !fields.keySet().isEmpty()) {
            cmd.append("fields", (Object) fields);
        }
        if (sort != null && !sort.keySet().isEmpty()) {
            cmd.append("sort", (Object) sort);
        }
        if (remove) {
            cmd.append("remove", (Object) Boolean.valueOf(remove));
        } else {
            if (update != null && !update.keySet().isEmpty()) {
                String key = update.keySet().iterator().next();
                if (key.charAt(0) != '$') {
                    _checkObject(update, false, false);
                }
                cmd.append("update", (Object) update);
            }
            if (returnNew) {
                cmd.append("new", (Object) Boolean.valueOf(returnNew));
            }
            if (upsert) {
                cmd.append("upsert", (Object) Boolean.valueOf(upsert));
            }
        }
        if (remove && update != null && !update.keySet().isEmpty() && !returnNew) {
            throw new MongoException("FindAndModify: Remove cannot be mixed with the Update, or returnNew params!");
        }
        CommandResult res = this._db.command(cmd);
        if (res.ok() || res.getErrorMessage().equals("No matching object found")) {
            return replaceWithObjectClass((DBObject) res.get("value"));
        }
        res.throwOnError();
        return null;
    }

    private DBObject replaceWithObjectClass(DBObject oldObj) {
        if (oldObj != null) {
            if (!((getObjectClass() == null) & this._internalClass.isEmpty())) {
                DBObject newObj = instantiateObjectClassInstance();
                for (String key : oldObj.keySet()) {
                    newObj.put(key, oldObj.get(key));
                }
                return newObj;
            }
        }
        return oldObj;
    }

    private DBObject instantiateObjectClassInstance() {
        try {
            return (DBObject) getObjectClass().newInstance();
        } catch (IllegalAccessException e) {
            throw new MongoInternalException("can't create instance of type " + getObjectClass(), e);
        } catch (InstantiationException e2) {
            throw new MongoInternalException("can't create instance of type " + getObjectClass(), e2);
        }
    }

    public DBObject findAndModify(DBObject query, DBObject sort, DBObject update) {
        return findAndModify(query, null, sort, false, update, false, false);
    }

    public DBObject findAndModify(DBObject query, DBObject update) {
        return findAndModify(query, null, null, false, update, false, false);
    }

    public DBObject findAndRemove(DBObject query) {
        return findAndModify(query, null, null, true, null, false, false);
    }

    public void createIndex(DBObject keys) {
        createIndex(keys, defaultOptions(keys));
    }

    public void createIndex(DBObject keys, DBObject options) {
        createIndex(keys, options, getDBEncoder());
    }

    public void ensureIndex(String name) {
        ensureIndex(new BasicDBObject(name, 1));
    }

    public void ensureIndex(DBObject keys) {
        ensureIndex(keys, defaultOptions(keys));
    }

    public void ensureIndex(DBObject keys, String name) {
        ensureIndex(keys, name, false);
    }

    public void ensureIndex(DBObject keys, String name, boolean unique) {
        DBObject options = defaultOptions(keys);
        if (name != null && name.length() > 0) {
            options.put("name", name);
        }
        if (unique) {
            options.put("unique", Boolean.TRUE);
        }
        ensureIndex(keys, options);
    }

    public void ensureIndex(DBObject keys, DBObject optionsIN) {
        if (!checkReadOnly(false)) {
            DBObject options = defaultOptions(keys);
            for (String k : optionsIN.keySet()) {
                options.put(k, optionsIN.get(k));
            }
            String name = options.get("name").toString();
            if (!this._createdIndexes.contains(name)) {
                createIndex(keys, options);
                this._createdIndexes.add(name);
            }
        }
    }

    public void resetIndexCache() {
        this._createdIndexes.clear();
    }

    DBObject defaultOptions(DBObject keys) {
        DBObject o = new BasicDBObject();
        o.put("name", genIndexName(keys));
        o.put("ns", this._fullName);
        return o;
    }

    public static String genIndexName(DBObject keys) {
        StringBuilder name = new StringBuilder();
        for (String s : keys.keySet()) {
            if (name.length() > 0) {
                name.append('_');
            }
            name.append(s).append('_');
            Object val = keys.get(s);
            if ((val instanceof Number) || (val instanceof String)) {
                name.append(val.toString().replace(' ', '_'));
            }
        }
        return name.toString();
    }

    public void setHintFields(List<DBObject> lst) {
        this._hintFields = lst;
    }

    public DBCursor find(DBObject ref) {
        return new DBCursor(this, ref, null, getReadPreference());
    }

    public DBCursor find(DBObject ref, DBObject keys) {
        return new DBCursor(this, ref, keys, getReadPreference());
    }

    public DBCursor find() {
        return new DBCursor(this, null, null, getReadPreference());
    }

    public DBObject findOne() {
        return findOne((DBObject) new BasicDBObject());
    }

    public DBObject findOne(DBObject o) {
        return findOne(o, null, null, getReadPreference());
    }

    public DBObject findOne(DBObject o, DBObject fields) {
        return findOne(o, fields, null, getReadPreference());
    }

    public DBObject findOne(DBObject o, DBObject fields, DBObject orderBy) {
        return findOne(o, fields, orderBy, getReadPreference());
    }

    public DBObject findOne(DBObject o, DBObject fields, ReadPreference readPref) {
        return findOne(o, fields, null, readPref);
    }

    public DBObject findOne(DBObject o, DBObject fields, DBObject orderBy, ReadPreference readPref) {
        QueryOpBuilder queryOpBuilder = new QueryOpBuilder().addQuery(o).addOrderBy(orderBy);
        if (getDB().getMongo().isMongosConnection()) {
            queryOpBuilder.addReadPreference(readPref.toDBObject());
        }
        Iterator<DBObject> i = __find(queryOpBuilder.get(), fields, 0, -1, 0, getOptions(), readPref, getDecoder());
        DBObject obj = i.hasNext() ? i.next() : null;
        if (obj != null && fields != null && fields.keySet().size() > 0) {
            obj.markAsPartialObject();
        }
        return obj;
    }

    private DBDecoder getDecoder() {
        if (getDBDecoderFactory() != null) {
            return getDBDecoderFactory().create();
        }
        return null;
    }

    private DBEncoder getDBEncoder() {
        if (getDBEncoderFactory() != null) {
            return getDBEncoderFactory().create();
        }
        return null;
    }

    public Object apply(DBObject o) {
        return apply(o, true);
    }

    public Object apply(DBObject jo, boolean ensureID) {
        Object id = jo.get("_id");
        if (ensureID && id == null) {
            id = ObjectId.get();
            jo.put("_id", id);
        }
        doapply(jo);
        return id;
    }

    public WriteResult save(DBObject jo) {
        return save(jo, getWriteConcern());
    }

    public WriteResult save(DBObject jo, WriteConcern concern) {
        if (checkReadOnly(true)) {
            return null;
        }
        _checkObject(jo, false, false);
        Object id = jo.get("_id");
        if (id == null || ((id instanceof ObjectId) && ((ObjectId) id).isNew())) {
            if (id != null && (id instanceof ObjectId)) {
                ((ObjectId) id).notNew();
            }
            return concern == null ? insert(jo) : insert(jo, concern);
        }
        DBObject q = new BasicDBObject();
        q.put("_id", id);
        if (concern == null) {
            return update(q, jo, true, false);
        }
        return update(q, jo, true, false, concern);
    }

    public void dropIndexes() {
        dropIndexes("*");
    }

    public void dropIndexes(String name) {
        DBObject cmd = BasicDBObjectBuilder.start().add("deleteIndexes", getName()).add("index", name).get();
        resetIndexCache();
        CommandResult res = this._db.command(cmd);
        if (!res.ok() && !res.getErrorMessage().equals("ns not found")) {
            res.throwOnError();
        }
    }

    public void drop() {
        resetIndexCache();
        CommandResult res = this._db.command(BasicDBObjectBuilder.start().add("drop", getName()).get());
        if (!res.ok() && !res.getErrorMessage().equals("ns not found")) {
            res.throwOnError();
        }
    }

    public long count() {
        return getCount(new BasicDBObject(), null);
    }

    public long count(DBObject query) {
        return getCount(query, null);
    }

    public long count(DBObject query, ReadPreference readPrefs) {
        return getCount(query, null, readPrefs);
    }

    public long getCount() {
        return getCount(new BasicDBObject(), null);
    }

    public long getCount(ReadPreference readPrefs) {
        return getCount(new BasicDBObject(), null, readPrefs);
    }

    public long getCount(DBObject query) {
        return getCount(query, null);
    }

    public long getCount(DBObject query, DBObject fields) {
        return getCount(query, fields, 0L, 0L);
    }

    public long getCount(DBObject query, DBObject fields, ReadPreference readPrefs) {
        return getCount(query, fields, 0L, 0L, readPrefs);
    }

    public long getCount(DBObject query, DBObject fields, long limit, long skip) {
        return getCount(query, fields, limit, skip, getReadPreference());
    }

    public long getCount(DBObject query, DBObject fields, long limit, long skip, ReadPreference readPrefs) {
        BasicDBObject cmd = new BasicDBObject();
        cmd.put("count", (Object) getName());
        cmd.put("query", (Object) query);
        if (fields != null) {
            cmd.put("fields", (Object) fields);
        }
        if (limit > 0) {
            cmd.put("limit", (Object) Long.valueOf(limit));
        }
        if (skip > 0) {
            cmd.put("skip", (Object) Long.valueOf(skip));
        }
        CommandResult res = this._db.command(cmd, getOptions(), readPrefs);
        if (!res.ok()) {
            String errmsg = res.getErrorMessage();
            if (errmsg.equals("ns does not exist") || errmsg.equals("ns missing")) {
                return 0L;
            }
            res.throwOnError();
        }
        return res.getLong("n");
    }

    CommandResult command(DBObject cmd, int options, ReadPreference readPrefs) {
        return this._db.command(cmd, getOptions(), readPrefs);
    }

    public DBCollection rename(String newName) {
        return rename(newName, false);
    }

    public DBCollection rename(String newName, boolean dropTarget) {
        CommandResult ret = this._db.getSisterDB("admin").command(BasicDBObjectBuilder.start().add("renameCollection", this._fullName).add("to", this._db._name + "." + newName).add("dropTarget", Boolean.valueOf(dropTarget)).get());
        ret.throwOnError();
        resetIndexCache();
        return this._db.getCollection(newName);
    }

    public DBObject group(DBObject key, DBObject cond, DBObject initial, String reduce) {
        return group(key, cond, initial, reduce, null);
    }

    public DBObject group(DBObject key, DBObject cond, DBObject initial, String reduce, String finalize) {
        GroupCommand cmd = new GroupCommand(this, key, cond, initial, reduce, finalize);
        return group(cmd);
    }

    public DBObject group(DBObject key, DBObject cond, DBObject initial, String reduce, String finalize, ReadPreference readPrefs) {
        GroupCommand cmd = new GroupCommand(this, key, cond, initial, reduce, finalize);
        return group(cmd, readPrefs);
    }

    public DBObject group(GroupCommand cmd) {
        return group(cmd, getReadPreference());
    }

    public DBObject group(GroupCommand cmd, ReadPreference readPrefs) {
        CommandResult res = this._db.command(cmd.toDBObject(), getOptions(), readPrefs);
        res.throwOnError();
        return (DBObject) res.get("retval");
    }

    @Deprecated
    public DBObject group(DBObject args) {
        args.put("ns", getName());
        CommandResult res = this._db.command(new BasicDBObject("group", args), getOptions(), getReadPreference());
        res.throwOnError();
        return (DBObject) res.get("retval");
    }

    public List distinct(String key) {
        return distinct(key, new BasicDBObject());
    }

    public List distinct(String key, ReadPreference readPrefs) {
        return distinct(key, new BasicDBObject(), readPrefs);
    }

    public List distinct(String key, DBObject query) {
        return distinct(key, query, getReadPreference());
    }

    public List distinct(String key, DBObject query, ReadPreference readPrefs) {
        DBObject c = BasicDBObjectBuilder.start().add("distinct", getName()).add("key", key).add("query", query).get();
        CommandResult res = this._db.command(c, getOptions(), readPrefs);
        res.throwOnError();
        return (List) res.get("values");
    }

    public MapReduceOutput mapReduce(String map, String reduce, String outputTarget, DBObject query) {
        return mapReduce(new MapReduceCommand(this, map, reduce, outputTarget, MapReduceCommand.OutputType.REPLACE, query));
    }

    public MapReduceOutput mapReduce(String map, String reduce, String outputTarget, MapReduceCommand.OutputType outputType, DBObject query) {
        return mapReduce(new MapReduceCommand(this, map, reduce, outputTarget, outputType, query));
    }

    public MapReduceOutput mapReduce(String map, String reduce, String outputTarget, MapReduceCommand.OutputType outputType, DBObject query, ReadPreference readPrefs) {
        MapReduceCommand command = new MapReduceCommand(this, map, reduce, outputTarget, outputType, query);
        command.setReadPreference(readPrefs);
        return mapReduce(command);
    }

    public MapReduceOutput mapReduce(MapReduceCommand command) {
        CommandResult res;
        DBObject cmd = command.toDBObject();
        if (command.getOutputType() == MapReduceCommand.OutputType.INLINE) {
            res = this._db.command(cmd, getOptions(), command.getReadPreference() != null ? command.getReadPreference() : getReadPreference());
        } else {
            res = this._db.command(cmd);
        }
        res.throwOnError();
        return new MapReduceOutput(this, cmd, res);
    }

    public MapReduceOutput mapReduce(DBObject command) {
        if (command.get("mapreduce") == null && command.get("mapReduce") == null) {
            throw new IllegalArgumentException("need mapreduce arg");
        }
        CommandResult res = this._db.command(command);
        res.throwOnError();
        return new MapReduceOutput(this, command, res);
    }

    public AggregationOutput aggregate(DBObject firstOp, DBObject... additionalOps) {
        if (firstOp == null) {
            throw new IllegalArgumentException("aggregate can not accept null pipeline operation");
        }
        DBObject command = new BasicDBObject("aggregate", this._name);
        List<DBObject> pipelineOps = new ArrayList<>();
        pipelineOps.add(firstOp);
        Collections.addAll(pipelineOps, additionalOps);
        command.put("pipeline", pipelineOps);
        CommandResult res = this._db.command(command);
        res.throwOnError();
        return new AggregationOutput(command, res);
    }

    public List<DBObject> getIndexInfo() {
        BasicDBObject cmd = new BasicDBObject();
        cmd.put("ns", (Object) getFullName());
        DBCursor cur = this._db.getCollection("system.indexes").find(cmd);
        List<DBObject> list = new ArrayList<>();
        while (cur.hasNext()) {
            list.add(cur.next());
        }
        return list;
    }

    public void dropIndex(DBObject keys) {
        dropIndexes(genIndexName(keys));
    }

    public void dropIndex(String name) {
        dropIndexes(name);
    }

    public CommandResult getStats() {
        return getDB().command(new BasicDBObject("collstats", getName()), getOptions());
    }

    public boolean isCapped() {
        CommandResult stats = getStats();
        Object capped = stats.get("capped");
        return capped != null && (capped.equals(1) || capped.equals(true));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DBCollection(DB base, String name) {
        this._db = base;
        this._name = name;
        this._fullName = this._db.getName() + "." + name;
        this._options = new Bytes.OptionHolder(this._db._options);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DBObject _checkObject(DBObject o, boolean canBeNull, boolean query) {
        if (o == null) {
            if (canBeNull) {
                return null;
            }
            throw new IllegalArgumentException("can't be null");
        }
        if (o.isPartialObject() && !query) {
            throw new IllegalArgumentException("can't save partial objects");
        }
        if (!query) {
            _checkKeys(o);
            return o;
        }
        return o;
    }

    private void _checkKeys(DBObject o) {
        for (String s : o.keySet()) {
            validateKey(s);
            Object inner = o.get(s);
            if (inner instanceof DBObject) {
                _checkKeys((DBObject) inner);
            } else if (inner instanceof Map) {
                _checkKeys((Map<String, Object>) inner);
            }
        }
    }

    private void _checkKeys(Map<String, Object> o) {
        for (String s : o.keySet()) {
            validateKey(s);
            Object inner = o.get(s);
            if (inner instanceof DBObject) {
                _checkKeys((DBObject) inner);
            } else if (inner instanceof Map) {
                _checkKeys((Map<String, Object>) inner);
            }
        }
    }

    private void validateKey(String s) {
        if (s.contains(".")) {
            throw new IllegalArgumentException("fields stored in the db can't have . in them. (Bad Key: '" + s + "')");
        }
        if (s.startsWith("$")) {
            throw new IllegalArgumentException("fields stored in the db can't start with '$' (Bad Key: '" + s + "')");
        }
    }

    public DBCollection getCollection(String n) {
        return this._db.getCollection(this._name + "." + n);
    }

    public String getName() {
        return this._name;
    }

    public String getFullName() {
        return this._fullName;
    }

    public DB getDB() {
        return this._db;
    }

    protected boolean checkReadOnly(boolean strict) {
        if (!this._db._readOnly) {
            return false;
        }
        if (!strict) {
            return true;
        }
        throw new IllegalStateException("db is read only");
    }

    public int hashCode() {
        return this._fullName.hashCode();
    }

    public boolean equals(Object o) {
        return o == this;
    }

    public String toString() {
        return this._name;
    }

    public void setObjectClass(Class c) {
        if (c == null) {
            this._wrapper = null;
            this._objectClass = null;
        } else {
            if (!DBObject.class.isAssignableFrom(c)) {
                throw new IllegalArgumentException(c.getName() + " is not a DBObject");
            }
            this._objectClass = c;
            if (ReflectionDBObject.class.isAssignableFrom(c)) {
                this._wrapper = ReflectionDBObject.getWrapper(c);
            } else {
                this._wrapper = null;
            }
        }
    }

    public Class getObjectClass() {
        return this._objectClass;
    }

    public void setInternalClass(String path, Class c) {
        this._internalClass.put(path, c);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Class getInternalClass(String path) {
        Class c = this._internalClass.get(path);
        if (c == null) {
            if (this._wrapper == null) {
                return null;
            }
            return this._wrapper.getInternalClass(path);
        }
        return c;
    }

    public void setWriteConcern(WriteConcern concern) {
        this._concern = concern;
    }

    public WriteConcern getWriteConcern() {
        return this._concern != null ? this._concern : this._db.getWriteConcern();
    }

    public void setReadPreference(ReadPreference preference) {
        this._readPref = preference;
    }

    public ReadPreference getReadPreference() {
        return this._readPref != null ? this._readPref : this._db.getReadPreference();
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

    public synchronized void setDBDecoderFactory(DBDecoderFactory fact) {
        this._decoderFactory = fact;
    }

    public synchronized DBDecoderFactory getDBDecoderFactory() {
        return this._decoderFactory;
    }

    public synchronized void setDBEncoderFactory(DBEncoderFactory fact) {
        this._encoderFactory = fact;
    }

    public synchronized DBEncoderFactory getDBEncoderFactory() {
        return this._encoderFactory;
    }
}
