package com.snapxeats.ui.location;

import com.snapxeats.common.model.location.Prediction;

import java.util.List;

/**
 * Created by Snehal Tembare on 12/2/18.
 */

public interface OnTaskCompleted {
    void onTaskCompleted(List<Prediction> response);
}
