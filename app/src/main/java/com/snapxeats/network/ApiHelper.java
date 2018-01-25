package com.snapxeats.network;

import com.snapxeats.common.constants.WebConstants;
import com.snapxeats.common.model.Cuisines;
import com.snapxeats.common.model.RootCuisine;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Prajakta Patil on 28/12/17.
 */
public interface ApiHelper {

    /**
     * get cuisine list
     * @return
     */
    @GET(WebConstants.CUISINE_LIST)
    Call<RootCuisine> getCuisineList();
}
