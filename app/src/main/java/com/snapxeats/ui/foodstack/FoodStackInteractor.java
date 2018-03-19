package com.snapxeats.ui.foodstack;

import android.app.Activity;

import com.snapxeats.SnapXApplication;
import com.snapxeats.common.model.DaoSession;
import com.snapxeats.common.model.RootCuisinePhotos;
import com.snapxeats.common.model.SelectedCuisineList;
import com.snapxeats.common.model.SnapxData;
import com.snapxeats.common.model.SnapxDataDao;
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

    private Activity mContext;

    private FoodStackContract.FoodStackPresenter mFoodStackPresenter;

    private FoodStackContract.FoodStackView mFoodStackView;
    private SnapxDataDao snapxDataDao;
    private DaoSession daoSession;
    private SnapxData snapxData;

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
        daoSession = ((SnapXApplication) mContext.getApplicationContext()).getDaoSession();
        snapxDataDao = daoSession.getSnapxDataDao();
        snapxData = new SnapxData();
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
            Call<RootCuisinePhotos> listCuisineCall = apiHelper.getCuisinePhotos(
                    utility.getAuthToken(mContext),
                    lat, lng, selectedCuisineList.getRestaurant_rating(),
                    selectedCuisineList.getRestaurant_price(),
                    selectedCuisineList.getRestaurant_distance(),
                    selectedCuisineList.getSort_by_distance(),
                    selectedCuisineList.getSort_by_rating(),
                    selectedCuisineList.getSelectedCuisineList(),
                    selectedCuisineList.getSelectedFoodList());

            listCuisineCall.enqueue(new Callback<RootCuisinePhotos>() {
                @Override
                public void onResponse(Call<RootCuisinePhotos> call, Response<RootCuisinePhotos> response) {
                    if (response.isSuccessful() && response.body() != null) {
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

    public void saveGesturesToDb(String count, RootFoodGestures rootFoodGestures) {
        snapxData.setFoodWishlistCount(count);
        if (snapxDataDao.loadAll().size() > 0) {
            List<SnapxData> snapxDataList = snapxDataDao.loadAll();
            snapxDataList.get(0).setFoodWishlistCount(count);
            snapxDataDao.update(snapxDataList.get(0));
        }
    }
}