package com.snapxeats.ui.home;

import com.snapxeats.common.Router;
import com.snapxeats.dagger.FragmentScoped;
import com.snapxeats.ui.home.fragment.checkin.CheckInContract;
import com.snapxeats.ui.home.fragment.checkin.CheckInFragment;
import com.snapxeats.ui.home.fragment.checkin.CheckInInteractor;
import com.snapxeats.ui.home.fragment.checkin.CheckInPresenterImpl;
import com.snapxeats.ui.home.fragment.checkin.CheckInRouterImpl;
import com.snapxeats.ui.home.fragment.foodjourney.FoodJourneyContract;
import com.snapxeats.ui.home.fragment.foodjourney.FoodJourneyFragment;
import com.snapxeats.ui.home.fragment.foodjourney.FoodJourneyInteractor;
import com.snapxeats.ui.home.fragment.foodjourney.FoodJourneyPresenterImpl;
import com.snapxeats.ui.home.fragment.foodjourney.FoodJourneyRouterImpl;
import com.snapxeats.ui.home.fragment.home.HomeFgmtContract;
import com.snapxeats.ui.home.fragment.home.HomeFgmtInteractor;
import com.snapxeats.ui.home.fragment.home.HomeFgmtPresenterImpl;
import com.snapxeats.ui.home.fragment.home.HomeFgmtRouterImpl;
import com.snapxeats.ui.home.fragment.home.HomeFragment;
import com.snapxeats.ui.home.fragment.navpreference.NavPrefContract;
import com.snapxeats.ui.home.fragment.navpreference.NavPrefFragment;
import com.snapxeats.ui.home.fragment.navpreference.NavPrefInteractor;
import com.snapxeats.ui.home.fragment.navpreference.NavPrefPresenterImpl;
import com.snapxeats.ui.home.fragment.navpreference.NavPrefRouterImpl;
import com.snapxeats.ui.home.fragment.rewards.RewardsContract;
import com.snapxeats.ui.home.fragment.rewards.RewardsFragment;
import com.snapxeats.ui.home.fragment.rewards.RewardsInteractor;
import com.snapxeats.ui.home.fragment.rewards.RewardsPresenterImpl;
import com.snapxeats.ui.home.fragment.rewards.RewardsRouterImpl;
import com.snapxeats.ui.home.fragment.smartphotos.SmartPhotoContract;
import com.snapxeats.ui.home.fragment.smartphotos.SmartPhotoFragment;
import com.snapxeats.ui.home.fragment.smartphotos.SmartPhotoInteractor;
import com.snapxeats.ui.home.fragment.smartphotos.SmartPhotoPresenterImpl;
import com.snapxeats.ui.home.fragment.smartphotos.SmartPhotoRouterImpl;
import com.snapxeats.ui.home.fragment.snapnshare.SnapShareContract;
import com.snapxeats.ui.home.fragment.snapnshare.SnapShareFragment;
import com.snapxeats.ui.home.fragment.snapnshare.SnapShareInteractor;
import com.snapxeats.ui.home.fragment.snapnshare.SnapSharePresenterImpl;
import com.snapxeats.ui.home.fragment.snapnshare.SnapShareRouterImpl;
import com.snapxeats.ui.home.fragment.wishlist.WishlistContract;
import com.snapxeats.ui.home.fragment.wishlist.WishlistFragment;
import com.snapxeats.ui.home.fragment.wishlist.WishlistInteractor;
import com.snapxeats.ui.home.fragment.wishlist.WishlistPresenterImpl;
import com.snapxeats.ui.home.fragment.wishlist.WishlistRouterImpl;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Snehal Tembare on 3/1/18.
 */

@Module
public abstract class HomeModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract HomeFragment homeFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract NavPrefFragment navPrefFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract WishlistFragment wishlistFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract CheckInFragment checkInFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract FoodJourneyFragment foodJourneyFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract RewardsFragment rewardsFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract SnapShareFragment snapShareFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract SmartPhotoFragment smartPhotoFragment();

    @Provides
    static HomeContract.HomePresenter provideHomePresenter(HomeInteractor interactor,
                                                           HomeRouterImpl router) {
        HomeContract.HomePresenter homePresenter =
                new HomePresenterImpl(interactor, router);
        interactor.setHomePresenter(homePresenter);
        return homePresenter;
    }

    @Provides
    static HomeFgmtContract.HomeFgmtPresenter providePresenter(HomeFgmtInteractor interactor,
                                                               HomeFgmtRouterImpl router) {
        HomeFgmtContract.HomeFgmtPresenter homeFgmtPresenter =
                new HomeFgmtPresenterImpl(interactor, router);
        interactor.setHomeFgmtPresenter(homeFgmtPresenter);
        return homeFgmtPresenter;
    }

    @Provides
    static HomeFgmtRouterImpl providePreferenceRouter(Router router) {
        HomeFgmtRouterImpl preferenceRouter = new HomeFgmtRouterImpl(router);
        return preferenceRouter;
    }

    @Provides
    static NavPrefContract.NavPrefPresenter provideNavPrefPresenter(NavPrefInteractor interactor,
                                                                    NavPrefRouterImpl router) {
        NavPrefContract.NavPrefPresenter navPrefPresenter =
                new NavPrefPresenterImpl(interactor, router);
        interactor.setPreferencePresenter(navPrefPresenter);
        return navPrefPresenter;
    }

    @Provides
    static NavPrefRouterImpl provideNavPrefRouter(Router router) {
        NavPrefRouterImpl navPrefRouter = new NavPrefRouterImpl(router);
        return navPrefRouter;
    }

    @Provides
    static WishlistContract.WishlistPresenter provideWishlistPresenter(WishlistInteractor interactor,
                                                                       WishlistRouterImpl router) {
        WishlistContract.WishlistPresenter wishlistPresenter =
                new WishlistPresenterImpl(interactor, router);
        interactor.setWishlistPresenter(wishlistPresenter);
        return wishlistPresenter;
    }

    @Provides
    static WishlistRouterImpl provideWishlistRouter(Router router) {
        WishlistRouterImpl wishlistRouter = new WishlistRouterImpl(router);
        return wishlistRouter;
    }

    @Provides
    static CheckInContract.CheckInPresenter provideCheckInPresenter(CheckInInteractor interactor,
                                                                    CheckInRouterImpl router) {
        CheckInContract.CheckInPresenter checkInPresenter = new CheckInPresenterImpl(interactor, router);
        interactor.setCheckInPresenter(checkInPresenter);
        return checkInPresenter;
    }

    @Provides
    static CheckInRouterImpl provideCheckInRouter(Router router) {
        CheckInRouterImpl checkInRouter = new CheckInRouterImpl(router);
        return checkInRouter;
    }


    @Provides
    static RewardsContract.RewardsPresenter provideRewardsPresenter(RewardsInteractor interactor,
                                                                    RewardsRouterImpl router) {
        RewardsContract.RewardsPresenter rewardsPresenter = new RewardsPresenterImpl(interactor, router);
        interactor.setRewardsPresenter(rewardsPresenter);
        return rewardsPresenter;
    }

    @Provides
    static RewardsRouterImpl provideRewardsRouter(Router router) {
        RewardsRouterImpl rewardsRouter = new RewardsRouterImpl(router);
        return rewardsRouter;
    }

    @Provides
    static FoodJourneyContract.FoodJourneyPresenter provideFoodJpurneyPresenter(FoodJourneyInteractor interactor,
                                                                                FoodJourneyRouterImpl router) {
        FoodJourneyContract.FoodJourneyPresenter foodJourneyPresenter =
                new FoodJourneyPresenterImpl(interactor, router);
        interactor.setFoodJourneyPresenter(foodJourneyPresenter);
        return foodJourneyPresenter;
    }

    @Provides
    static FoodJourneyRouterImpl provideFoodJourneyRouter(Router router) {
        FoodJourneyRouterImpl foodJourneyRouter = new FoodJourneyRouterImpl(router);
        return foodJourneyRouter;
    }


    @Provides
    static SmartPhotoContract.SmartPhotoPresenter provideSmartPhotoPresenter(SmartPhotoInteractor interactor,
                                                                             SmartPhotoRouterImpl router) {
        SmartPhotoContract.SmartPhotoPresenter smartPhotoPresenter = new SmartPhotoPresenterImpl(interactor, router);
        interactor.setSmartPhotoPresenter(smartPhotoPresenter);
        return smartPhotoPresenter;
    }

    @Provides
    static SmartPhotoRouterImpl provideSmartPhotoRouter(Router router) {
        SmartPhotoRouterImpl smartPhotoRouter = new SmartPhotoRouterImpl(router);
        return smartPhotoRouter;
    }

    @Provides
    static SnapShareContract.SnapSharePresenter provideSnapSharePresenter(SnapShareInteractor interactor,
                                                                          SnapShareRouterImpl router) {
        SnapShareContract.SnapSharePresenter snapSharePresenter = new SnapSharePresenterImpl(interactor, router);
        interactor.setSnapSharePresenter(snapSharePresenter);
        return snapSharePresenter;
    }

    @Provides
    static SnapShareRouterImpl provideSnapShareRouter(Router router) {
        SnapShareRouterImpl snapShareRouter = new SnapShareRouterImpl(router);
        return snapShareRouter;
    }
}
