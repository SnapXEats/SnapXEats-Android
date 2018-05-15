package com.snapxeats.ui.home.fragment.foodjourney;

import android.content.Context;

import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.model.foodJourney.RootFoodJourney;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.common.utilities.SnapXResult;
import com.snapxeats.network.ApiClient;
import com.snapxeats.network.ApiHelper;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.snapxeats.common.constants.WebConstants.BASE_URL;

/**
 * Created by Prajakta Patil on 14/5/18.
 */
public class FoodJourneyInteractor {

    private FoodJourneyContract.FoodJourneyPresenter foodJourneyPresenter;
    private FoodJourneyContract.FoodJourneyView view;
    private Context mContext;

    @Inject
    AppUtility utility;


    @Inject
    public FoodJourneyInteractor() {
    }

    public void setFoodJourneyPresenter(FoodJourneyContract.FoodJourneyPresenter presenter) {
        this.foodJourneyPresenter = presenter;
    }

    public void setContext(FoodJourneyContract.FoodJourneyView context) {
        this.view = context;
        mContext = context.getActivity();
        utility.setContext(mContext);
    }

    public void getFoodJourney() {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            ApiHelper apiHelper = ApiClient.getClient(mContext, BASE_URL).create(ApiHelper.class);
            Call<RootFoodJourney> foodJourneyCall = apiHelper.getFoodJourney(utility.getAuthToken(mContext));

            foodJourneyCall.enqueue(new Callback<RootFoodJourney>() {
                @Override
                public void onResponse(Call<RootFoodJourney> call, Response<RootFoodJourney> response) {
                    if (response.isSuccessful()) {
                        foodJourneyPresenter.response(SnapXResult.SUCCESS, response.body());
                    }
                }

                @Override
                public void onFailure(Call<RootFoodJourney> call, Throwable t) {
                    foodJourneyPresenter.response(SnapXResult.ERROR, null);
                }
            });

        } else {
            foodJourneyPresenter.response(SnapXResult.NONETWORK, null);

        }
    }
}
