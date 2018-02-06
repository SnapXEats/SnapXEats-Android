package com.snapxeats.common.utilities;

import com.snapxeats.common.model.Result;

/**
 * Created by Prajakta Patil on 15/1/18.
 */
public enum SnapXResult {

    SUCCESS("Success"),FAILURE("Failure"), NONETWORK("No network"), NETWORKERROR("Network error"),
    CANCEL("Cancel"), ERROR("Error");

    private Object value;
    SnapXResult(String result) {
    }

    public void setValue(Object result) {
         value = result;
    }

    public Object getValue() {
        return value;
    }
}
