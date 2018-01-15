package com.example.synerzip.snapxeats;

import com.example.synerzip.snapxeats.dagger.AppContract;

/**
 * Created by Prajakta Patil on 28/12/17.
 */
public interface BasePresenter<T> extends AppContract.SnapXResults{

    /**
     * Binds presenter with a view when resumed. The Presenter will perform initialization here.
     *
     * @param view the view associated with this presenter
     */
    void addView(T view);

    /**
     * Drops the reference to the view when destroyed
     */
    void dropView();

}
