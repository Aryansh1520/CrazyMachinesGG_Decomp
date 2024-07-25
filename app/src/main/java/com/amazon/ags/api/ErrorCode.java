package com.amazon.ags.api;

import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public enum ErrorCode {
    NONE(false),
    UNRECOVERABLE(true),
    SERVICE_NOT_READY(true),
    IO_ERROR(true),
    CONNECTION_ERROR(true),
    AUTHENTICATION_ERROR(true),
    DATA_VALIDATION_ERROR(true);

    public static final Map<Integer, ErrorCode> CODE_TO_ERROR = new HashMap<Integer, ErrorCode>() { // from class: com.amazon.ags.api.ErrorCode.1
        private static final long serialVersionUID = -4762006087722277344L;

        {
            put(9, ErrorCode.AUTHENTICATION_ERROR);
            put(11, ErrorCode.CONNECTION_ERROR);
            put(12, ErrorCode.UNRECOVERABLE);
            put(21, ErrorCode.UNRECOVERABLE);
            put(19, ErrorCode.UNRECOVERABLE);
            put(23, ErrorCode.UNRECOVERABLE);
            put(26, ErrorCode.DATA_VALIDATION_ERROR);
        }
    };
    private final boolean isError;

    ErrorCode(boolean isError) {
        this.isError = isError;
    }

    public boolean isError() {
        return this.isError;
    }

    public static ErrorCode fromServiceResponseCode(int responseCode) {
        ErrorCode error = CODE_TO_ERROR.get(Integer.valueOf(responseCode));
        if (error == null) {
            return UNRECOVERABLE;
        }
        return error;
    }
}
