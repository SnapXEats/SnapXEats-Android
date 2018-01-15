package com.example.synerzip.snapxeats.dagger;

import android.content.DialogInterface;

import com.example.synerzip.snapxeats.common.utilities.SnapXResult;

/**
 * Created by Prajakta Patil on 15/1/18.
 */
public class AppContract {
    public interface DialogListenerAction {
        void action();
    }


   public interface DialogView {

        void showProgressDialog();

        void dismissProgressDialog();

        void showNetworkErrorDialog(DialogInterface.OnClickListener click);

        void showDenyDialog(DialogInterface.OnClickListener positiveClick,
                            DialogInterface.OnClickListener negativeClick);
        DialogInterface.OnClickListener setListener(AppContract.DialogListenerAction button);

    }

    public interface SnapXResults {
        void response(SnapXResult result);
    }

    public interface SnapXResponse {
        void success();
        void error();
        void noNetwork();
        void networkError();
    }
}
