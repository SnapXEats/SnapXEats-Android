package com.snapxeats.ui.home.fragment.wishlist;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.snapxeats.BaseActivity;
import com.snapxeats.BaseFragment;
import com.snapxeats.R;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.OnRecyclerItemClickListener;
import com.snapxeats.common.model.foodGestures.RootFoodGestures;
import com.snapxeats.common.model.foodGestures.RootWishlist;
import com.snapxeats.common.model.foodGestures.Wishlist;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.ui.foodstack.FoodStackDbHelper;
import com.snapxeats.ui.home.HomeDbHelper;
import com.snapxeats.ui.restaurant.RestaurantDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Snehal Tembare on 22/3/18.
 */

public class WishlistFragment extends BaseFragment implements WishlistContract.WishlistView,
        AppContract.SnapXResults {

    @BindView(R.id.layout_parent)
    protected LinearLayout mParentLayout;

    @Inject
    WishlistContract.WishlistPresenter wishlistPresenter;

    @BindView(R.id.recyclerview_wishlist)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.txt_wishlist_edit)
    protected TextView mTxtWishlistEdit;

    @Inject
    WishlistDbHelper wishlistDbHelper;

    @Inject
    AppUtility utility;

    @Inject
    FoodStackDbHelper foodStackDbHelper;

    @Inject
    DbHelper dbHelper;

    private RootFoodGestures mRootFoodGestures;

    private Activity activity;
    protected NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    private List<Wishlist> mWishlist;
    private WishlistAdapter mAdapter;

    @Inject
    HomeDbHelper homeDbHelper;
    private ActionBarDrawerToggle toggle;
    private Toolbar mToolbar;
    public static boolean isMultipleDeleted;

    @Inject
    public WishlistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_wishlist, container, false);
            ButterKnife.bind(this, view);
        } catch (InflateException e) {
            e.printStackTrace();
        }
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar = view.findViewById(R.id.toolbar);

        mNavigationView = activity.findViewById(R.id.nav_view);
        mDrawerLayout = activity.findViewById(R.id.drawer_layout);

        ((BaseActivity) getActivity()).setSupportActionBar(mToolbar);
        ((BaseActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setNavigationOnClickListener(v -> mDrawerLayout.openDrawer(Gravity.START));

        toggle = new ActionBarDrawerToggle(
                getActivity(), mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (null != getActivity() && isAdded()) {
                    setWishlistCount();
                }
            }
        };

        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        initView();

    }

    private void setWishlistCount() {
        LinearLayout linearLayout = mNavigationView.getMenu().findItem(R.id.nav_wishlist)
                .getActionView().findViewById(R.id.layout_wishlist_count);

        linearLayout.setVisibility(View.VISIBLE);

        TextView view = mNavigationView.getMenu().findItem(R.id.nav_wishlist)
                .getActionView().findViewById(R.id.txt_count_wishlist);

        view.setText(getString(R.string.zero));

        dbHelper.setContext(getActivity());
        if (0 != homeDbHelper.getWishlistCount()) {
            view.setText(String.valueOf(homeDbHelper.getWishlistCount()));
        } else {
            view.setText(getString(R.string.zero));
        }
    }

    @OnClick(R.id.txt_wishlist_edit)
    public void deleteWishlist() {
        if ((mTxtWishlistEdit.getText()).equals("Edit")) {
            mTxtWishlistEdit.setText(getString(R.string.delete));
            toggle.setDrawerIndicatorEnabled(false);
            toggle.setHomeAsUpIndicator(R.drawable.close);
            isMultipleDeleted = true;
        } else if ((mTxtWishlistEdit.getText()).equals("Delete")) {

            showWishlistDialog();
        }

       /* mTxtWishlistEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((mTxtWishlistEdit.getText()).equals("Delete")) {
                    for (int index = 0; index < mWishlist.size(); index++) {
                        wishlistDbHelper.setWishlistItemStatus(mWishlist.get(index).getRestaurant_dish_id());
                        mAdapter.wishlists.remove(index);
                        mAdapter.notifyItemRemoved(index);
                        mAdapter.notifyItemRangeChanged(index, mAdapter.getItemCount());
                    }
                    showWishlistDialog();
                }
            }
        });*/


    }

    private void showWishlistDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.wishlist));
        builder.setMessage(getString(R.string.delete_wishlist_message));

        builder.setPositiveButton(getString(R.string.ok), (dialogInterface, i) -> {
            //Clear local db
            for (int index = 0; index < mWishlist.size(); index++) {
                wishlistDbHelper.setWishlistItemStatus(mWishlist.get(index).getRestaurant_dish_id());
                mAdapter.wishlists.remove(index);
                mAdapter.notifyItemRemoved(index);
                mAdapter.notifyItemRangeChanged(index, mAdapter.getItemCount());
            }

            showProgressDialog();
            isMultipleDeleted = false;
            wishlistPresenter.sendDeletedWishlist(wishlistDbHelper.getDeletedWishlistObject());

        });

        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
            dialog.cancel();
        });

        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @Override
    public void initView() {
        wishlistPresenter.addView(this);
        foodStackDbHelper.setContext(getActivity());
        utility.setContext(getActivity());
        mWishlist = new ArrayList<>();

        mRootFoodGestures = wishlistDbHelper.getFoodGestures();

        //Send users gestures to server
        showProgressDialog();
        wishlistPresenter.sendUsersGestures(mRootFoodGestures);

        mToolbar.setNavigationOnClickListener(v -> {
            if ((mTxtWishlistEdit.getText()).equals("Delete")) {
                mTxtWishlistEdit.setText(getString(R.string.edit));
                toggle.setDrawerIndicatorEnabled(true);
                isMultipleDeleted = false;
                for (int index = 0; index < mWishlist.size(); index++) {
                    if (mWishlist.get(index).isDeleted()) {
                        mWishlist.get(index).setDeleted(false);
                        mAdapter.notifyItemChanged(index);
                    }
                }
            } else {
                mDrawerLayout.openDrawer(Gravity.START);
            }
        });


    }

    @Override
    public DialogInterface.OnClickListener setListener(AppContract.DialogListenerAction button) {
        return null;
    }

    @Override
    public void success(Object value) {
        dismissProgressDialog();
        mTxtWishlistEdit.setText(getString(R.string.edit));
        toggle.setDrawerIndicatorEnabled(true);
        isMultipleDeleted = false;

        if (value instanceof RootWishlist) {
            mWishlist = ((RootWishlist) value).getUser_wishlist();
            setupRecyclerview();
        }
    }

    private void setupRecyclerview() {

        mAdapter = new WishlistAdapter(getActivity(), mWishlist, new OnRecyclerItemClickListener() {
            @Override
            public void onClickToDelete(int position, Object object) {

                if (((Wishlist) object).isDeleted()) {
                    mWishlist.get(position).setDeleted(false);
                    mAdapter.notifyItemChanged(position);

                } else {
                    mWishlist.get(position).setDeleted(true);
                    mAdapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onClick(Object object) {
                Intent intent = new Intent(getActivity(), RestaurantDetailsActivity.class);
                intent.putExtra(activity.getString(R.string.intent_restaurant_id),
                        ((Wishlist) object).getRestaurant_info_id());
                startActivity(intent);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(layoutManager);

        SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            void onRightClicked(int position) {
                super.onRightClicked(position);
                wishlistDbHelper.setWishlistItemStatus(mWishlist.get(position).getRestaurant_dish_id());
                mAdapter.wishlists.remove(position);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
            }
        });
        swipeController.setmContext(getActivity());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void error(Object value) {
        dismissProgressDialog();
    }

    @Override
    public void noNetwork(Object value) {
        dismissProgressDialog();
        showNetworkErrorDialog((dialog, which) -> {
            if (!NetworkUtility.isNetworkAvailable(getActivity()) && null != mWishlist) {
                AppContract.DialogListenerAction click = () -> {
                    showProgressDialog();
                    wishlistPresenter.sendUsersGestures(mRootFoodGestures);
                };
                showSnackBar(mParentLayout, setClickListener(click));
            }
        });
    }

    @Override
    public void networkError(Object value) {

    }
}
