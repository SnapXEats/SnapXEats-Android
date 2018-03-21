package com.snapxeats.ui.location;


import android.content.Context;
import com.snapxeats.common.constants.WebConstants;
import com.snapxeats.common.model.location.PlaceDetail;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXResult;
import com.snapxeats.network.ApiClient;
import com.snapxeats.network.ApiHelper;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Snehal Tembare on 5/1/18.
 */

public class LocationInteractor {
    private LocationContract.LocationPresenter locationPresenter;
    private LocationContract.LocationView locationView;
    private Context mContext;


    @Inject
    public LocationInteractor() {
    }

    @Inject
    ApiClient apiClient;

    public void setPresenter(LocationContract.LocationPresenter presenter) {
        this.locationPresenter = presenter;
    }

    public void setLocationPresenter(LocationContract.LocationPresenter presenter) {
        this.locationPresenter = presenter;
    }


    public void getPlaceDetails(String placeId) {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = apiClient.getClient(mContext,
                    WebConstants.GOOGLE_BASE_URL).create(ApiHelper.class);
            Call<PlaceDetail> call = apiHelper.getPlaceDetails(placeId);
            call.enqueue(new Callback<PlaceDetail>() {
                @Override
                public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {
                    if (response.isSuccessful() && null != response
                            && null != response.body().getResult()) {
                        locationPresenter.response(SnapXResult.SUCCESS, response.body().getResult());
                    }
                }

                @Override
                public void onFailure(Call<PlaceDetail> call, Throwable t) {
                    locationPresenter.response(SnapXResult.ERROR, null);
                }
            });

        } else {
            locationPresenter.response(SnapXResult.NONETWORK, null);
        }
    }

    public void setContext(LocationContract.LocationView view) {
        this.mContext = view.getActivity();
    }
}
