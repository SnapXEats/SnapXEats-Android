package com.snapxeats.ui.restaurant;

import javax.inject.Inject;

/**
 * Created by Prajakta Patil on 5/2/18.
 */
public class RestaurantDetailsInteractor {

    private RestaurantDetailsContract.RestaurantDetailsPresenter mRestaurantDetailsPresenter;

    @Inject
    public RestaurantDetailsInteractor() {

    }

    public void setRestaurantDetailsPresenter(RestaurantDetailsContract.RestaurantDetailsPresenter presenter) {
        this.mRestaurantDetailsPresenter = presenter;
    }

}
