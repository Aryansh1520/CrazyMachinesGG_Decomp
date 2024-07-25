package com.amazon.ags.api;

/* loaded from: classes.dex */
public class ServiceResponse {
    private final ErrorCode error;
    private final AmazonGamesStatus status;

    public ServiceResponse(AmazonGamesStatus status, ErrorCode error) {
        if (status == null) {
            throw new IllegalArgumentException("No status reported");
        }
        this.status = status;
        this.error = error;
    }

    public final AmazonGamesStatus getStatus() {
        return this.status;
    }

    public final ErrorCode getError() {
        return this.error;
    }

    public String toString() {
        return "ServiceResponse{status=" + this.status + ", error=" + this.error + "}";
    }
}
