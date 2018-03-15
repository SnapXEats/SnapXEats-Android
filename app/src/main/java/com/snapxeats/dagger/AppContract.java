package com.snapxeats.dagger;

import android.content.DialogInterface;

import com.snapxeats.common.utilities.SnapXResult;

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

        void showResetDialog(DialogInterface.OnClickListener positiveClick,
                            DialogInterface.OnClickListener negativeClick);

        DialogInterface.OnClickListener setListener(AppContract.DialogListenerAction button);

    }

    public interface SnapXResponse {
        void response(SnapXResult result,Object value);
    }

    public interface SnapXResults {
        void success(Object value);
        void error(Object value);
        void noNetwork(Object value);
        void networkError(Object value);
    }
}
