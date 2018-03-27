package com.snapxeats.ui.foodstack;

import com.snapxeats.BasePresenter;
import com.snapxeats.BaseView;
import com.snapxeats.common.Router;
import com.snapxeats.common.model.SelectedCuisineList;
import com.snapxeats.common.model.foodGestures.FoodDislikes;
import com.snapxeats.common.model.foodGestures.FoodLikes;
import com.snapxeats.common.model.foodGestures.FoodWishlists;
import com.snapxeats.common.model.foodGestures.RootFoodGestures;
import com.snapxeats.dagger.AppContract;

import java.util.List;

/**
 * Created by Prajakta Patil on 30/1/18.
 */
public class FoodStackContract {

    interface FoodStackView extends BaseView<FoodStackPresenter>, AppContract.SnapXResults {
    }

    interface FoodStackPresenter extends BasePresenter<FoodStackView> {

        void presentScreen(Router.Screen screen);

        void getCuisinePhotos(SelectedCuisineList selectedCuisineList);

        void saveDislikeToDb(List<FoodDislikes> foodDislikes);

        void saveLikesToDb(List<FoodLikes> foodLikes);

        void saveWishlistToDb(List<FoodWishlists> foodGestureWishlist);

        void foodstackGestures(RootFoodGestures rootFoodGestures);
    }

    public interface FoodStackRouter {

        void presentScreen(Router.Screen screen);

        void setView(FoodStackContract.FoodStackView view);
    }
}