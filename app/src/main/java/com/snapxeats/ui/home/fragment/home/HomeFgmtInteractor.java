package com.snapxeats.ui.home.fragment.home;

import android.app.Activity;
import com.snapxeats.SnapXApplication;
import com.snapxeats.common.model.DaoSession;
import com.snapxeats.common.model.LocationCuisine;
import com.snapxeats.common.model.preference.RootCuisine;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXResult;
import com.snapxeats.network.ApiClient;
import com.snapxeats.network.ApiHelper;
import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.snapxeats.common.constants.WebConstants.BASE_URL;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

@Singleton
public class HomeFgmtInteractor {

    private HomeFgmtContract.HomeFgmtPresenter homeFgmtPresenter;
    private DaoSession daoSession;

    private Activity mContext;

    @Inject
    HomeFgmtInteractor() {
    }

    @Inject
    ApiClient apiClient;

    public void setHomeFgmtPresenter(HomeFgmtContract.HomeFgmtPresenter presenter) {
        this.homeFgmtPresenter = presenter;
    }

    public void setContext(HomeFgmtContract.HomeFgmtView view) {
        this.mContext = view.getActivity();
        daoSession = ((SnapXApplication) mContext.getApplication()).getDaoSession();
    }

    /**
     * get cuisines list
     */
    public void getCuisineList(LocationCuisine locationCuisine) {

        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);

        /*    TODO latlng are hardcoded for now
        double lat = locationCuisine.getLatitude();
        double lng =locationCuisine.getLongitude();*/
            double lat = 40.4862157;
            double lng = -74.4518188;

            Call<RootCuisine> listCuisineCall = apiHelper.getCuisineList(lat, lng);
            listCuisineCall.enqueue(new Callback<RootCuisine>() {
                @Override
                public void onResponse(Call<RootCuisine> call, Response<RootCuisine> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        RootCuisine rootCuisine = response.body();
                        homeFgmtPresenter.response(SnapXResult.SUCCESS, rootCuisine);
                    }
                }

                @Override
                public void onFailure(Call<RootCuisine> call, Throwable t) {
                    homeFgmtPresenter.response(SnapXResult.ERROR, null);
                }
            });
        } else {
            homeFgmtPresenter.response(SnapXResult.NONETWORK, null);
        }
    }
}
