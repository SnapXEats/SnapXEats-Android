package com.snapxeats.ui.maps;

import android.content.Context;
import android.util.Log;

import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.model.preference.RootUserPreferences;
import com.snapxeats.common.model.preference.SnapXPreference;
import com.snapxeats.common.utilities.AppUtility;
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
 * Created by Prajakta Patil on 27/3/18.
 */
@Singleton
public class MapsInteractor {
    private Context mContext;
    private MapsContract.MapsPresenter mPresenter;
    private MapsContract.MapsView mView;

    @Inject
    public MapsInteractor() {
    }

    @Inject
    AppUtility utility;

    public void setMapsPresenter(MapsContract.MapsPresenter presenter) {
        this.mPresenter = presenter;
    }

    public void setContext(MapsContract.MapsView view) {
        mView = view;
        mContext = view.getActivity();
        utility.setContext(mContext);
    }

    /**
     * Get user preferences
     */
    public void getUserPreferences() {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<SnapXPreference> userPreferenceCall = apiHelper.getUserPreferences(utility.getAuthToken(mContext));

            userPreferenceCall.enqueue(new Callback<SnapXPreference>() {
                @Override
                public void onResponse(Call<SnapXPreference> call, Response<SnapXPreference> response) {
                    if (response.isSuccessful() && null != response.body()) {
                        mPresenter.response(SnapXResult.SUCCESS, response.body());
                    }
                }

                @Override
                public void onFailure(Call<SnapXPreference> call, Throwable t) {
                    mPresenter.response(SnapXResult.ERROR, null);
                }
            });
        } else {
            mPresenter.response(SnapXResult.NONETWORK, null);
        }
    }
}

