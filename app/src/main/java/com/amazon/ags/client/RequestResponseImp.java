package com.amazon.ags.client;

import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.RequestResponse;

/* loaded from: classes.dex */
public class RequestResponseImp implements RequestResponse {
    private ErrorCode errorCode;
    private int responseCode;
    private Object[] userData;

    public RequestResponseImp(int responseCode, ErrorCode errorCode) {
        if (errorCode == null) {
            throw new IllegalArgumentException("errorCode must be non-null.");
        }
        this.responseCode = responseCode;
        this.errorCode = errorCode;
    }

    public RequestResponseImp(int responseCode) {
        this.responseCode = responseCode;
        this.errorCode = ErrorCode.NONE;
    }

    @Override // com.amazon.ags.api.RequestResponse
    public String toString() {
        String text = "ResponseCode: " + this.responseCode;
        if (this.errorCode != null) {
            text = text + "\n ErrorCode: " + this.errorCode;
        }
        if (this.userData != null) {
            text = text + "\n " + this.userData.toString();
        }
        return text + "\n requestType: " + getEventType();
    }

    public int getEventType() {
        return 0;
    }

    @Override // com.amazon.ags.api.RequestResponse
    public final void setUserData(Object[] userData) {
        this.userData = userData;
    }

    @Override // com.amazon.ags.api.RequestResponse
    public final Object[] getUserData() {
        return this.userData;
    }

    public final int getResponseCode() {
        return this.responseCode;
    }

    @Override // com.amazon.ags.api.RequestResponse
    public final ErrorCode getError() {
        return this.errorCode;
    }

    @Override // com.amazon.ags.api.RequestResponse
    public final boolean isError() {
        return getError().isError();
    }

    protected final void setError(ErrorCode error) {
        this.errorCode = error;
    }

    protected final void setResponseCode(int code) {
        this.responseCode = code;
    }
}
