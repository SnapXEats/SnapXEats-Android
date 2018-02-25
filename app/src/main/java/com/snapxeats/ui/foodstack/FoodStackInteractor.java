package com.snapxeats.ui.foodstack;

import android.app.Activity;

import com.snapxeats.common.model.RootCuisinePhotos;
import com.snapxeats.common.model.SelectedCuisineList;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXResult;
import com.snapxeats.network.ApiClient;
import com.snapxeats.network.ApiHelper;
import com.snapxeats.ui.foodpreference.FoodPreferenceContract;

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

    private Activity mContext;

    private FoodStackContract.FoodStackPreseneter mFoodStackPreseneter;

    private FoodStackContract.FoodStackView mFoodStackView;

    @Inject
    public FoodStackInteractor() {
    }

    public void setFoodStackPresenter(FoodStackContract.FoodStackPreseneter foodStackPreseneter) {
        this.mFoodStackPreseneter = foodStackPreseneter;
    }
    public void setContext(FoodStackContract.FoodStackView view) {
        this.mFoodStackView = view;
        this.mContext = view.getActivity();
    }

    /**
     * get cuisines list
     */
    public void getCuisinePhotos( SelectedCuisineList selectedCuisineList) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            //TODO latlng are hardcoded for now
//        double lat = selectedCuisineList.getLocation().getLatitude();
//        double lng = selectedCuisineList.getLocation().getLongitude();
            double lat = 40.4862157;
            double lng = -74.4518188;
            List<String> list = selectedCuisineList.getSelectedCuisineList();

            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<RootCuisinePhotos> listCuisineCall = apiHelper.getCuisinePhotos(lat, lng, list);
            listCuisineCall.enqueue(new Callback<RootCuisinePhotos>() {
                @Override
                public void onResponse(Call<RootCuisinePhotos> call, Response<RootCuisinePhotos> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        RootCuisinePhotos rootCuisine = response.body();
                        mFoodStackPreseneter.response(SnapXResult.SUCCESS, rootCuisine);
                    }
                }

                @Override
                public void onFailure(Call<RootCuisinePhotos> call, Throwable t) {
                    mFoodStackPreseneter.response(SnapXResult.ERROR, null);
                }
            });
        } else {
            mFoodStackPreseneter.response(SnapXResult.NONETWORK, null);
        }
    }
}