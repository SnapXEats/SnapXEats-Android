package com.snapxeats.common.model.foodJourney;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 14/5/18.
 */
@Getter
@Setter
public class RootFoodJourney {
    private List<UserCurrentWeekHistory> userCurrentWeekHistory;

    private List<UserPastHistory> userPastHistory;
}
