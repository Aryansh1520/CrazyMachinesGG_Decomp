package com.mongodb;

import com.mokredit.payment.StringUtils;
import com.mongodb.ReplicaSetStatus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public abstract class TaggableReadPreference extends ReadPreference {
    private static final List<DBObject> EMPTY = new ArrayList();
    final List<DBObject> _tags;

    TaggableReadPreference() {
        this._tags = EMPTY;
    }

    TaggableReadPreference(DBObject firstTagSet, DBObject... remainingTagSets) {
        if (firstTagSet == null) {
            throw new IllegalArgumentException("Must have at least one tag set");
        }
        this._tags = new ArrayList();
        this._tags.add(firstTagSet);
        Collections.addAll(this._tags, remainingTagSets);
    }

    @Override // com.mongodb.ReadPreference
    public boolean isSlaveOk() {
        return true;
    }

    @Override // com.mongodb.ReadPreference
    public DBObject toDBObject() {
        DBObject readPrefObject = new BasicDBObject("mode", getName());
        if (!this._tags.isEmpty()) {
            readPrefObject.put("tags", this._tags);
        }
        return readPrefObject;
    }

    public List<DBObject> getTagSets() {
        List<DBObject> tags = new ArrayList<>();
        for (DBObject tagSet : this._tags) {
            tags.add(tagSet);
        }
        return tags;
    }

    public String toString() {
        return getName() + printTags();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaggableReadPreference that = (TaggableReadPreference) o;
        return this._tags.equals(that._tags);
    }

    public int hashCode() {
        int result = this._tags.hashCode();
        return (result * 31) + getName().hashCode();
    }

    String printTags() {
        return this._tags.isEmpty() ? StringUtils.EMPTY : " : " + new BasicDBObject("tags", this._tags);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static List<ReplicaSetStatus.Tag> getTagListFromDBObject(DBObject curTagSet) {
        List<ReplicaSetStatus.Tag> tagList = new ArrayList<>();
        for (String key : curTagSet.keySet()) {
            tagList.add(new ReplicaSetStatus.Tag(key, curTagSet.get(key).toString()));
        }
        return tagList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SecondaryReadPreference extends TaggableReadPreference {
        /* JADX INFO: Access modifiers changed from: package-private */
        public SecondaryReadPreference() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public SecondaryReadPreference(DBObject firstTagSet, DBObject... remainingTagSets) {
            super(firstTagSet, remainingTagSets);
        }

        @Override // com.mongodb.ReadPreference
        public String getName() {
            return "secondary";
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.mongodb.ReadPreference
        public ReplicaSetStatus.ReplicaSetNode getNode(ReplicaSetStatus.ReplicaSet set) {
            if (this._tags.isEmpty()) {
                return set.getASecondary();
            }
            for (DBObject curTagSet : this._tags) {
                List<ReplicaSetStatus.Tag> tagList = TaggableReadPreference.getTagListFromDBObject(curTagSet);
                ReplicaSetStatus.ReplicaSetNode node = set.getASecondary(tagList);
                if (node != null) {
                    return node;
                }
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SecondaryPreferredReadPreference extends SecondaryReadPreference {
        /* JADX INFO: Access modifiers changed from: package-private */
        public SecondaryPreferredReadPreference() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public SecondaryPreferredReadPreference(DBObject firstTagSet, DBObject... remainingTagSets) {
            super(firstTagSet, remainingTagSets);
        }

        @Override // com.mongodb.TaggableReadPreference.SecondaryReadPreference, com.mongodb.ReadPreference
        public String getName() {
            return "secondaryPreferred";
        }

        @Override // com.mongodb.TaggableReadPreference.SecondaryReadPreference, com.mongodb.ReadPreference
        ReplicaSetStatus.ReplicaSetNode getNode(ReplicaSetStatus.ReplicaSet set) {
            ReplicaSetStatus.ReplicaSetNode node = super.getNode(set);
            return node != null ? node : set.getMaster();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class NearestReadPreference extends TaggableReadPreference {
        /* JADX INFO: Access modifiers changed from: package-private */
        public NearestReadPreference() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public NearestReadPreference(DBObject firstTagSet, DBObject... remainingTagSets) {
            super(firstTagSet, remainingTagSets);
        }

        @Override // com.mongodb.ReadPreference
        public String getName() {
            return "nearest";
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.mongodb.ReadPreference
        public ReplicaSetStatus.ReplicaSetNode getNode(ReplicaSetStatus.ReplicaSet set) {
            if (this._tags.isEmpty()) {
                return set.getAMember();
            }
            for (DBObject curTagSet : this._tags) {
                List<ReplicaSetStatus.Tag> tagList = TaggableReadPreference.getTagListFromDBObject(curTagSet);
                ReplicaSetStatus.ReplicaSetNode node = set.getAMember(tagList);
                if (node != null) {
                    return node;
                }
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class PrimaryPreferredReadPreference extends SecondaryReadPreference {
        /* JADX INFO: Access modifiers changed from: package-private */
        public PrimaryPreferredReadPreference() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public PrimaryPreferredReadPreference(DBObject firstTagSet, DBObject... remainingTagSets) {
            super(firstTagSet, remainingTagSets);
        }

        @Override // com.mongodb.TaggableReadPreference.SecondaryReadPreference, com.mongodb.ReadPreference
        public String getName() {
            return "primaryPreferred";
        }

        @Override // com.mongodb.TaggableReadPreference.SecondaryReadPreference, com.mongodb.ReadPreference
        ReplicaSetStatus.ReplicaSetNode getNode(ReplicaSetStatus.ReplicaSet set) {
            ReplicaSetStatus.ReplicaSetNode node = set.getMaster();
            return node != null ? node : super.getNode(set);
        }
    }
}
