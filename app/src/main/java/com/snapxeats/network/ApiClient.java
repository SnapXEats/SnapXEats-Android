package com.snapxeats.network;

import android.content.Context;

import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.constants.WebConstants;
import com.snapxeats.common.utilities.NetworkUtility;
import com.snapxeats.dagger.AppContract;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Prajakta Patil on 28/12/17.
 */

@Singleton
public class ApiClient {

    @Inject
    ApiClient() {
    }

    private static Retrofit retrofitBaseURL = null;
    static Retrofit retrofitGOOGLEURL = null;

    public static Retrofit getClient(Context context, String base_url) {

        if (NetworkUtility.isNetworkAvailable(context)) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                    .readTimeout(180, TimeUnit.SECONDS)
                    .connectTimeout(180, TimeUnit.SECONDS)
                    .build();

            if (null == retrofitBaseURL && base_url.equals(WebConstants.BASE_URL)) {
                retrofitBaseURL = new Retrofit.Builder()
                        .baseUrl(base_url)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            } else if (null == retrofitGOOGLEURL && base_url.equals(WebConstants.GOOGLE_BASE_URL)){
                retrofitGOOGLEURL = new Retrofit.Builder()
                        .baseUrl(base_url)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return base_url.equals(WebConstants.BASE_URL) ? retrofitBaseURL : retrofitGOOGLEURL;

        } else {
            SnapXToast.showLongToast(context, "");
            return null;
        }
    }
}


