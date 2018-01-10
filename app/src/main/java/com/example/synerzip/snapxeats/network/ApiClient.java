package com.example.synerzip.snapxeats.network;

import android.content.Context;

import com.example.synerzip.snapxeats.common.constants.SnapXToast;
import com.example.synerzip.snapxeats.common.constants.WebConstants;
import com.example.synerzip.snapxeats.common.utilities.NetworkUtility;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Prajakta Patil on 28/12/17.
 */
public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {

        if (NetworkUtility.isNetworkAvailable(context)) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                    .readTimeout(180, TimeUnit.SECONDS)
                    .connectTimeout(180, TimeUnit.SECONDS)
                    .build();

            if (null == retrofit) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(WebConstants.BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        } else {
            SnapXToast.showLongToast(context, "");
            return null;
        }
    }
}

