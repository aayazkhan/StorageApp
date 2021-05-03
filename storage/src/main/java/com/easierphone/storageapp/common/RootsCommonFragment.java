package com.easierphone.storageapp.common;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.easierphone.storageapp.R;
import com.easierphone.storageapp.fragment.RootsFragment;

public class RootsCommonFragment extends RootsFragment {

    public static void show(FragmentManager fm, Intent includeApps) {
        final Bundle args = new Bundle();
        args.putParcelable(EXTRA_INCLUDE_APPS, includeApps);

        final com.easierphone.storageapp.common.RootsCommonFragment fragment = new com.easierphone.storageapp.common.RootsCommonFragment();
        fragment.setArguments(args);

        final FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container_roots, fragment);
        ft.commitAllowingStateLoss();
    }

    public static com.easierphone.storageapp.common.RootsCommonFragment get(FragmentManager fm) {
        return (com.easierphone.storageapp.common.RootsCommonFragment) fm.findFragmentById(R.id.container_roots);
    }
}