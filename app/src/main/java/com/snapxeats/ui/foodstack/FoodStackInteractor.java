package com.snapxeats.ui.foodstack;

import android.content.Context;
import android.content.SharedPreferences;

import com.snapxeats.R;
import com.snapxeats.common.model.RootCuisinePhotos;
import com.snapxeats.common.model.SelectedCuisineList;
import com.snapxeats.common.model.foodGestures.FoodDislikes;
import com.snapxeats.common.model.foodGestures.FoodLikes;
import com.snapxeats.common.model.foodGestures.FoodWishlists;
import com.snapxeats.common.model.foodGestures.RootFoodGestures;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXResult;
import com.snapxeats.network.ApiClient;
import com.snapxeats.network.ApiHelper;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.snapxeats.common.constants.WebConstants.BASE_URL;

/**
 * Created by Prajakta Patil on 30/1/18.
 */
@Singleton
public class FoodStackInteractor {

    private Context mContext;

    private FoodStackContract.FoodStackPresenter mFoodStackPresenter;

    private FoodStackContract.FoodStackView mFoodStackView;

    @Inject
    FoodStackDbHelper foodStackDbHelper;

    @Inject
    public FoodStackInteractor() {
    }

    @Inject
    AppUtility utility;

    public void setFoodStackPresenter(FoodStackContract.FoodStackPresenter foodStackPresenter) {
        this.mFoodStackPresenter = foodStackPresenter;
    }

    public void setContext(FoodStackContract.FoodStackView view) {
        this.mFoodStackView = view;
        this.mContext = view.getActivity();
        foodStackDbHelper.setContext(mContext);

    }

    /**
     * get cuisines list
     */
    public void getCuisinePhotos(SelectedCuisineList selectedCuisineList) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            //TODO latlng are hardcoded for now
             /*locationCuisine.setLatitude(lat);
            locationCuisine.setLongitude(lng);
            SelectedCuisineList selectedCuisineList=new SelectedCuisineList(locationCuisine,null);*/
            double lat = 40.4862157;
            double lng = -74.4518188;
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            SharedPreferences preferences = utility.getSharedPreferences();
            String userId = preferences.getString(mContext.getString(R.string.user_id), "");

            Call<RootCuisinePhotos> listCuisineCall;
            if (!userId.isEmpty()) {
                listCuisineCall = apiHelper.getCuisinePhotos(
                        utility.getAuthToken(mContext),
                        lat, lng, null, null, null,
                        null, null,
                        selectedCuisineList.getSelectedCuisineList(),
                        null);

            } else {
                listCuisineCall = apiHelper.getCuisinePhotos(
                        utility.getAuthToken(mContext),
                        lat, lng, selectedCuisineList.getRestaurant_rating(),
                        selectedCuisineList.getRestaurant_price(),
                        selectedCuisineList.getRestaurant_distance(),
                        selectedCuisineList.getSort_by_distance(),
                        selectedCuisineList.getSort_by_rating(),
                        selectedCuisineList.getSelectedCuisineList(),
                        selectedCuisineList.getSelectedFoodList());
            }

            listCuisineCall.enqueue(new Callback<RootCuisinePhotos>() {
                @Override
                public void onResponse(Call<RootCuisinePhotos> call, Response<RootCuisinePhotos> response) {
                    if (response.isSuccessful() && null != response.body()) {
                        RootCuisinePhotos rootCuisine = response.body();
                        mFoodStackPresenter.response(SnapXResult.SUCCESS, rootCuisine);
                    }
                }

                @Override
                public void onFailure(Call<RootCuisinePhotos> call, Throwable t) {
                    mFoodStackPresenter.response(SnapXResult.ERROR, null);
                }
            });
        } else {
            mFoodStackPresenter.response(SnapXResult.NONETWORK, null);
        }
    }

    public void saveDislikesToDb(List<FoodDislikes> foodDislikes) {
        List<FoodDislikes> foodGestureDislikes = foodDislikes;
        foodStackDbHelper.saveFoodDislikes(foodGestureDislikes);
    }

    public void saveWishlistToDb(List<FoodWishlists> foodWishlists) {
        List<FoodWishlists> foodGestureWishlists = foodWishlists;
        foodStackDbHelper.saveFoodWishlist(foodGestureWishlists);
    }

    public void saveLikesToDb(List<FoodLikes> foodLikes) {
        List<FoodLikes> foodGestureLikes = foodLikes;
        foodStackDbHelper.saveFoodLikes(foodGestureLikes);
    }

    /**
     * food gestures api
     *
     * @param rootFoodGestures
     */
    public void foodstackGestures(RootFoodGestures rootFoodGestures) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<RootFoodGestures> call = apiHelper.foodstackGestures(utility.getAuthToken(mContext), rootFoodGestures);
            call.enqueue(new Callback<RootFoodGestures>() {
                @Override
                public void onResponse(Call<RootFoodGestures> call, Response<RootFoodGestures> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        //success message response
                    }
                }

                @Override
                public void onFailure(Call<RootFoodGestures> call, Throwable t) {
                    mFoodStackPresenter.response(SnapXResult.ERROR, null);
                }
            });
        } else {
            mFoodStackPresenter.response(SnapXResult.NONETWORK, null);
        }
    }
}