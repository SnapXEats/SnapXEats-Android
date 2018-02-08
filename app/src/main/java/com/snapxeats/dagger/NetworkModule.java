package com.snapxeats.dagger;

import com.snapxeats.common.constants.WebConstants;
import com.snapxeats.network.LocationHelper;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Prajakta Patil on 28/12/17.
 */
@Module
public class NetworkModule {
    private static final String NAME_BASE_URL = "";

    @Provides
    @Named(NAME_BASE_URL)
    String provideBaseUrlString() {
        return WebConstants.BASE_URL;
    }

    @Provides
    @Singleton
    Converter.Factory provideGsonConverter() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Converter.Factory converter, @Named(NAME_BASE_URL) String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converter)
                .build();
    }

    @Provides
    @Singleton
    LocationHelper provideNetworkApi(Retrofit retrofit) {
        return retrofit.create(LocationHelper.class);
    }
}
