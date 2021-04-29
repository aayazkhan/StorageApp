package com.easierphone.storaageapp.common;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.easierphone.storaageapp.R;
import com.easierphone.storaageapp.fragment.RootsFragment;

public class RootsCommonFragment extends RootsFragment {

    public static void show(FragmentManager fm, Intent includeApps) {
        final Bundle args = new Bundle();
        args.putParcelable(EXTRA_INCLUDE_APPS, includeApps);

        final com.easierphone.storaageapp.common.RootsCommonFragment fragment = new com.easierphone.storaageapp.common.RootsCommonFragment();
        fragment.setArguments(args);

        final FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container_roots, fragment);
        ft.commitAllowingStateLoss();
    }

    public static com.easierphone.storaageapp.common.RootsCommonFragment get(FragmentManager fm) {
        return (com.easierphone.storaageapp.common.RootsCommonFragment) fm.findFragmentById(R.id.container_roots);
    }
}