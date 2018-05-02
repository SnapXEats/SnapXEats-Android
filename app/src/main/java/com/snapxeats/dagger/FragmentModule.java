package com.snapxeats.dagger;

import com.snapxeats.BaseFragment;
import com.snapxeats.ui.home.fragment.snapnshare.SnapShareFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Prajakta Patil on 26/4/18.
 */
@Module
public abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract BaseFragment contributeBaseFragmentInjector();

    @ContributesAndroidInjector
    abstract SnapShareFragment contributeSnapShareFragmentInjector();
}