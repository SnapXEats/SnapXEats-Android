package com.snapxeats;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.snapxeats.common.Router;
import com.snapxeats.common.utilities.SnapXDialog;
import com.snapxeats.dagger.AppContract;
import javax.inject.Inject;
import dagger.android.DaggerFragment;

/**
 * Created by Snehal Tembare on 16/2/18.
 */

public class BaseFragment extends DaggerFragment {

    private static final String TAG = "BaseFragment";

    @Inject
    Router router;

    @Inject
    SnapXDialog mSnapXDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSnapXDialog.setContext(getActivity());
        router.setActivity(getActivity());
    }

    public void showProgressDialog() {
        mSnapXDialog.createProgressDialog();
    }

    public void dismissProgressDialog() {
        mSnapXDialog.dismissProgressSialog();
    }

    public void showResetDialog(DialogInterface.OnClickListener negativeClick,
                                DialogInterface.OnClickListener positiveClick) {
        mSnapXDialog.showResetDialog(negativeClick, positiveClick);
    }

    public void showNetworkErrorDialog(DialogInterface.OnClickListener click) {
        mSnapXDialog.showNetworkErrorDialog(click);
    }

    public void showSnackBar(View view, View.OnClickListener click) {
        mSnapXDialog.showSnackBar(view, click);
    }

    public View.OnClickListener setClickListener(AppContract.DialogListenerAction button) {
        return v -> button.action();
    }

}
