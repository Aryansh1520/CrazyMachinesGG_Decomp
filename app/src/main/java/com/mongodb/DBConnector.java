package com.mongodb;

/* loaded from: classes.dex */
public interface DBConnector {
    Response call(DB db, DBCollection dBCollection, OutMessage outMessage, ServerAddress serverAddress, int i);

    Response call(DB db, DBCollection dBCollection, OutMessage outMessage, ServerAddress serverAddress, int i, ReadPreference readPreference, DBDecoder dBDecoder);

    Response call(DB db, DBCollection dBCollection, OutMessage outMessage, ServerAddress serverAddress, DBDecoder dBDecoder);

    boolean isOpen();

    void requestDone();

    void requestEnsureConnection();

    void requestStart();

    WriteResult say(DB db, OutMessage outMessage, WriteConcern writeConcern);

    WriteResult say(DB db, OutMessage outMessage, WriteConcern writeConcern, ServerAddress serverAddress);
}
