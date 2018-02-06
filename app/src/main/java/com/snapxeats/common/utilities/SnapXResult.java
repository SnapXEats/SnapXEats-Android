package com.snapxeats.common.utilities;

/**
 * Created by Prajakta Patil on 15/1/18.
 */
public enum SnapXResult {

    SUCCESS("Success"), FAILURE("Failure"), NONETWORK("No network"), NETWORKERROR("Network error"),
    CANCEL("Cancel"), ERROR("Error");

    private Object value;

    SnapXResult(String result) {
    }

    public Object setValue(Object object) {
        return value = object;
    }

    public Object getValue() {
        return value;
    }
}
