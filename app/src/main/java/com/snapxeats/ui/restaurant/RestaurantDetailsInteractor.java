package com.snapxeats.ui.restaurant;

import android.content.Context;

import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.model.googleDirections.GeocodedWaypoints;
import com.snapxeats.common.model.googleDirections.LocationGoogleDir;
import com.snapxeats.common.model.googleDirections.RootGoogleDir;
import com.snapxeats.common.model.restaurantDetails.RootRestaurantDetails;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXResult;
import com.snapxeats.network.ApiClient;
import com.snapxeats.network.ApiHelper;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.snapxeats.common.constants.WebConstants.BASE_URL;
import static com.snapxeats.common.constants.WebConstants.GOOGLE_BASE_URL;

/**
 * Created by Prajakta Patil on 5/2/18.
 */
public class RestaurantDetailsInteractor {

    private RestaurantDetailsContract.RestaurantDetailsPresenter mRestaurantDetailsPresenter;
    private Context mContext;

    @Inject
    public RestaurantDetailsInteractor() {
    }

    public void setRestaurantDetailsPresenter(RestaurantDetailsContract.RestaurantDetailsPresenter presenter) {
        this.mRestaurantDetailsPresenter = presenter;
    }

    /**
     * get restaurant details
     *
     * @param restaurantId
     */

    public void getRestDetails(String restaurantId) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<RootRestaurantDetails> snapXUserCall = apiHelper.getRestDetails(restaurantId);
            snapXUserCall.enqueue(new Callback<RootRestaurantDetails>() {
                @Override
                public void onResponse(Call<RootRestaurantDetails> call, Response<RootRestaurantDetails> response) {
                    if (response.isSuccessful() && null!=response.body()) {
                        RootRestaurantDetails rootRestaurantDetails = response.body();
                        mRestaurantDetailsPresenter.response(SnapXResult.SUCCESS, rootRestaurantDetails);
                    }
                }

                @Override
                public void onFailure(Call<RootRestaurantDetails> call, Throwable t) {
                    mRestaurantDetailsPresenter.response(SnapXResult.FAILURE, null);
                }
            });
        } else {
            mRestaurantDetailsPresenter.response(SnapXResult.NONETWORK, null);
        }
    }

    public void setContext(RestaurantDetailsContract.RestaurantDetailsView view) {
        this.mContext = view.getActivity();
    }

    //get google directions
    public void getGoogleDirections(LocationGoogleDir locationGoogleDir) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, GOOGLE_BASE_URL).create(ApiHelper.class);
            Call<List<GeocodedWaypoints>> snapXUserCall =
                    apiHelper.getGoogleDir(locationGoogleDir.getGoogleDirOrigin().getOriginLat()
                                    + "," + locationGoogleDir.getGoogleDirOrigin().getOriginLng(),
                            locationGoogleDir.getGoogleDirDest().getDestinationLat() + ","
                                    + locationGoogleDir.getGoogleDirDest().getDestinationLng());
            snapXUserCall.enqueue(new Callback<List<GeocodedWaypoints>>() {
                @Override
                public void onResponse(Call<List<GeocodedWaypoints>> call, Response<List<GeocodedWaypoints>> response) {
                    if (response.isSuccessful() && null!=response.body()) {
                        SnapXToast.showToast(mContext,"Yass");
                        List<GeocodedWaypoints> rootGoogleDir = response.body();
                        mRestaurantDetailsPresenter.response(SnapXResult.SUCCESS, rootGoogleDir);
                    }
                }

                @Override
                public void onFailure(Call<List<GeocodedWaypoints>> call, Throwable t) {
                }
            });
        }
    }
}