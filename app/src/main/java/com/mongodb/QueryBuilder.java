package com.mongodb;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class QueryBuilder {
    private String _currentKey;
    private DBObject _query = new BasicDBObject();

    public static QueryBuilder start() {
        return new QueryBuilder();
    }

    public static QueryBuilder start(String key) {
        return new QueryBuilder().put(key);
    }

    public QueryBuilder put(String key) {
        this._currentKey = key;
        if (this._query.get(key) == null) {
            this._query.put(this._currentKey, new NullObject());
        }
        return this;
    }

    public QueryBuilder and(String key) {
        return put(key);
    }

    public QueryBuilder greaterThan(Object object) {
        addOperand(QueryOperators.GT, object);
        return this;
    }

    public QueryBuilder greaterThanEquals(Object object) {
        addOperand(QueryOperators.GTE, object);
        return this;
    }

    public QueryBuilder lessThan(Object object) {
        addOperand(QueryOperators.LT, object);
        return this;
    }

    public QueryBuilder lessThanEquals(Object object) {
        addOperand(QueryOperators.LTE, object);
        return this;
    }

    public QueryBuilder is(Object object) {
        addOperand(null, object);
        return this;
    }

    public QueryBuilder notEquals(Object object) {
        addOperand(QueryOperators.NE, object);
        return this;
    }

    public QueryBuilder in(Object object) {
        addOperand(QueryOperators.IN, object);
        return this;
    }

    public QueryBuilder notIn(Object object) {
        addOperand(QueryOperators.NIN, object);
        return this;
    }

    public QueryBuilder mod(Object object) {
        addOperand(QueryOperators.MOD, object);
        return this;
    }

    public QueryBuilder all(Object object) {
        addOperand(QueryOperators.ALL, object);
        return this;
    }

    public QueryBuilder size(Object object) {
        addOperand(QueryOperators.SIZE, object);
        return this;
    }

    public QueryBuilder exists(Object object) {
        addOperand(QueryOperators.EXISTS, object);
        return this;
    }

    public QueryBuilder regex(Pattern regex) {
        addOperand(null, regex);
        return this;
    }

    public QueryBuilder withinCenter(double x, double y, double radius) {
        addOperand("$within", new BasicDBObject("$center", new Object[]{new Double[]{Double.valueOf(x), Double.valueOf(y)}, Double.valueOf(radius)}));
        return this;
    }

    public QueryBuilder near(double x, double y) {
        addOperand(QueryOperators.NEAR, new Double[]{Double.valueOf(x), Double.valueOf(y)});
        return this;
    }

    public QueryBuilder near(double x, double y, double maxDistance) {
        addOperand(QueryOperators.NEAR, new Double[]{Double.valueOf(x), Double.valueOf(y), Double.valueOf(maxDistance)});
        return this;
    }

    public QueryBuilder nearSphere(double longitude, double latitude) {
        addOperand("$nearSphere", new Double[]{Double.valueOf(longitude), Double.valueOf(latitude)});
        return this;
    }

    public QueryBuilder nearSphere(double longitude, double latitude, double maxDistance) {
        addOperand("$nearSphere", new Double[]{Double.valueOf(longitude), Double.valueOf(latitude), Double.valueOf(maxDistance)});
        return this;
    }

    public QueryBuilder withinCenterSphere(double longitude, double latitude, double maxDistance) {
        addOperand("$within", new BasicDBObject("$centerSphere", new Object[]{new Double[]{Double.valueOf(longitude), Double.valueOf(latitude)}, Double.valueOf(maxDistance)}));
        return this;
    }

    public QueryBuilder withinBox(double x, double y, double x2, double y2) {
        addOperand("$within", new BasicDBObject("$box", new Object[]{new Double[]{Double.valueOf(x), Double.valueOf(y)}, new Double[]{Double.valueOf(x2), Double.valueOf(y2)}}));
        return this;
    }

    public QueryBuilder withinPolygon(List<Double[]> points) {
        if (points == null || points.isEmpty() || points.size() < 3) {
            throw new IllegalArgumentException("Polygon insufficient number of vertices defined");
        }
        addOperand("$within", new BasicDBObject("$polygon", points));
        return this;
    }

    public QueryBuilder or(DBObject... ors) {
        List l = (List) this._query.get("$or");
        if (l == null) {
            l = new ArrayList();
            this._query.put("$or", l);
        }
        for (DBObject o : ors) {
            l.add(o);
        }
        return this;
    }

    public QueryBuilder and(DBObject... ands) {
        List l = (List) this._query.get("$and");
        if (l == null) {
            l = new ArrayList();
            this._query.put("$and", l);
        }
        for (DBObject o : ands) {
            l.add(o);
        }
        return this;
    }

    public DBObject get() {
        for (String key : this._query.keySet()) {
            if (this._query.get(key) instanceof NullObject) {
                throw new QueryBuilderException("No operand for key:" + key);
            }
        }
        return this._query;
    }

    private void addOperand(String op, Object value) {
        BasicDBObject operand;
        if (op == null) {
            this._query.put(this._currentKey, value);
            return;
        }
        Object storedValue = this._query.get(this._currentKey);
        if (!(storedValue instanceof DBObject)) {
            operand = new BasicDBObject();
            this._query.put(this._currentKey, operand);
        } else {
            operand = (BasicDBObject) this._query.get(this._currentKey);
        }
        operand.put(op, value);
    }

    /* loaded from: classes.dex */
    static class QueryBuilderException extends RuntimeException {
        QueryBuilderException(String message) {
            super(message);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class NullObject {
        private NullObject() {
        }
    }
}
