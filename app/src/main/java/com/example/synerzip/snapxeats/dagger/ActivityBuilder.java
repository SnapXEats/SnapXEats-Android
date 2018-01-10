package com.example.synerzip.snapxeats.dagger;

import com.example.synerzip.snapxeats.ui.login.LoginActivity;
import com.example.synerzip.snapxeats.ui.login.LoginModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Prajakta Patil on 28/12/17.
 */

@Module
public abstract class ActivityBuilder {

    @ActivityScoped
    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract LoginActivity bindLoginActivity();
}