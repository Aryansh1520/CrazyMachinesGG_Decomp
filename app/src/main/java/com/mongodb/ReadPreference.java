package com.mongodb;

import com.mongodb.ReplicaSetStatus;
import com.mongodb.TaggableReadPreference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public abstract class ReadPreference {

    @Deprecated
    public static final ReadPreference PRIMARY;
    private static final ReadPreference _PRIMARY;

    @Deprecated
    public static final ReadPreference SECONDARY = new TaggableReadPreference.SecondaryPreferredReadPreference();
    private static final ReadPreference _SECONDARY = new TaggableReadPreference.SecondaryReadPreference();
    private static final ReadPreference _SECONDARY_PREFERRED = new TaggableReadPreference.SecondaryPreferredReadPreference();
    private static final ReadPreference _PRIMARY_PREFERRED = new TaggableReadPreference.PrimaryPreferredReadPreference();
    private static final ReadPreference _NEAREST = new TaggableReadPreference.NearestReadPreference();

    public abstract String getName();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract ReplicaSetStatus.ReplicaSetNode getNode(ReplicaSetStatus.ReplicaSet replicaSet);

    public abstract boolean isSlaveOk();

    public abstract DBObject toDBObject();

    /* loaded from: classes.dex */
    private static class PrimaryReadPreference extends ReadPreference {
        private PrimaryReadPreference() {
        }

        @Override // com.mongodb.ReadPreference
        public boolean isSlaveOk() {
            return false;
        }

        public String toString() {
            return getName();
        }

        public boolean equals(Object o) {
            return o != null && getClass() == o.getClass();
        }

        public int hashCode() {
            return getName().hashCode();
        }

        @Override // com.mongodb.ReadPreference
        ReplicaSetStatus.ReplicaSetNode getNode(ReplicaSetStatus.ReplicaSet set) {
            return set.getMaster();
        }

        @Override // com.mongodb.ReadPreference
        public DBObject toDBObject() {
            return new BasicDBObject("mode", getName());
        }

        @Override // com.mongodb.ReadPreference
        public String getName() {
            return "primary";
        }
    }

    @Deprecated
    /* loaded from: classes.dex */
    public static class TaggedReadPreference extends ReadPreference {
        private final ReadPreference _pref;
        private final DBObject _tags;

        public TaggedReadPreference(Map<String, String> tags) {
            if (tags == null || tags.size() == 0) {
                throw new IllegalArgumentException("tags can not be null or empty");
            }
            this._tags = new BasicDBObject(tags);
            List<DBObject> maps = splitMapIntoMultipleMaps(this._tags);
            this._pref = new TaggableReadPreference.SecondaryReadPreference(maps.get(0), getRemainingMaps(maps));
        }

        public TaggedReadPreference(DBObject tags) {
            if (tags == null || tags.keySet().size() == 0) {
                throw new IllegalArgumentException("tags can not be null or empty");
            }
            this._tags = tags;
            List<DBObject> maps = splitMapIntoMultipleMaps(this._tags);
            this._pref = new TaggableReadPreference.SecondaryReadPreference(maps.get(0), getRemainingMaps(maps));
        }

        public DBObject getTags() {
            DBObject tags = new BasicDBObject();
            for (String key : this._tags.keySet()) {
                tags.put(key, this._tags.get(key));
            }
            return tags;
        }

        @Override // com.mongodb.ReadPreference
        public boolean isSlaveOk() {
            return this._pref.isSlaveOk();
        }

        @Override // com.mongodb.ReadPreference
        ReplicaSetStatus.ReplicaSetNode getNode(ReplicaSetStatus.ReplicaSet set) {
            return this._pref.getNode(set);
        }

        @Override // com.mongodb.ReadPreference
        public DBObject toDBObject() {
            return this._pref.toDBObject();
        }

        @Override // com.mongodb.ReadPreference
        public String getName() {
            return this._pref.getName();
        }

        private static List<DBObject> splitMapIntoMultipleMaps(DBObject tags) {
            List<DBObject> tagList = new ArrayList<>(tags.keySet().size());
            for (String key : tags.keySet()) {
                tagList.add(new BasicDBObject(key, tags.get(key).toString()));
            }
            return tagList;
        }

        private DBObject[] getRemainingMaps(List<DBObject> maps) {
            return maps.size() <= 1 ? new DBObject[0] : (DBObject[]) maps.subList(1, maps.size() - 1).toArray(new DBObject[maps.size() - 1]);
        }
    }

    public static ReadPreference primary() {
        return _PRIMARY;
    }

    public static ReadPreference primaryPreferred() {
        return _PRIMARY_PREFERRED;
    }

    public static TaggableReadPreference primaryPreferred(DBObject firstTagSet, DBObject... remainingTagSets) {
        return new TaggableReadPreference.PrimaryPreferredReadPreference(firstTagSet, remainingTagSets);
    }

    public static ReadPreference secondary() {
        return _SECONDARY;
    }

    public static TaggableReadPreference secondary(DBObject firstTagSet, DBObject... remainingTagSets) {
        return new TaggableReadPreference.SecondaryReadPreference(firstTagSet, remainingTagSets);
    }

    public static ReadPreference secondaryPreferred() {
        return _SECONDARY_PREFERRED;
    }

    public static TaggableReadPreference secondaryPreferred(DBObject firstTagSet, DBObject... remainingTagSets) {
        return new TaggableReadPreference.SecondaryPreferredReadPreference(firstTagSet, remainingTagSets);
    }

    public static ReadPreference nearest() {
        return _NEAREST;
    }

    public static ReadPreference valueOf(String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        String name2 = name.toLowerCase();
        if (name2.equals(_PRIMARY.getName().toLowerCase())) {
            return _PRIMARY;
        }
        if (name2.equals(_SECONDARY.getName().toLowerCase())) {
            return _SECONDARY;
        }
        if (name2.equals(_SECONDARY_PREFERRED.getName().toLowerCase())) {
            return _SECONDARY_PREFERRED;
        }
        if (name2.equals(_PRIMARY_PREFERRED.getName().toLowerCase())) {
            return _PRIMARY_PREFERRED;
        }
        if (name2.equals(_NEAREST.getName().toLowerCase())) {
            return _NEAREST;
        }
        throw new IllegalArgumentException("No match for read preference of " + name2);
    }

    public static TaggableReadPreference valueOf(String name, DBObject firstTagSet, DBObject... remainingTagSets) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        String name2 = name.toLowerCase();
        if (name2.equals(_SECONDARY.getName().toLowerCase())) {
            return new TaggableReadPreference.SecondaryReadPreference(firstTagSet, remainingTagSets);
        }
        if (name2.equals(_SECONDARY_PREFERRED.getName().toLowerCase())) {
            return new TaggableReadPreference.SecondaryPreferredReadPreference(firstTagSet, remainingTagSets);
        }
        if (name2.equals(_PRIMARY_PREFERRED.getName().toLowerCase())) {
            return new TaggableReadPreference.PrimaryPreferredReadPreference(firstTagSet, remainingTagSets);
        }
        if (name2.equals(_NEAREST.getName().toLowerCase())) {
            return new TaggableReadPreference.NearestReadPreference(firstTagSet, remainingTagSets);
        }
        throw new IllegalArgumentException("No match for read preference of " + name2);
    }

    public static TaggableReadPreference nearest(DBObject firstTagSet, DBObject... remainingTagSets) {
        return new TaggableReadPreference.NearestReadPreference(firstTagSet, remainingTagSets);
    }

    static {
        PRIMARY = new PrimaryReadPreference();
        _PRIMARY = new PrimaryReadPreference();
    }

    @Deprecated
    public static ReadPreference withTags(Map<String, String> tags) {
        return new TaggedReadPreference(tags);
    }

    @Deprecated
    public static ReadPreference withTags(DBObject tags) {
        return new TaggedReadPreference(tags);
    }
}
