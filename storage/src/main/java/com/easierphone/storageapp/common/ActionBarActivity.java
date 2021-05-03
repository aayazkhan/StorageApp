package com.easierphone.storageapp.common;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.easierphone.storageapp.misc.Utils;

/**
 * Created by Ayyaz on 18-Oct-14.
 */
public abstract class ActionBarActivity extends BaseCommonActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Utils.changeThemeStyle(getDelegate());
        super.onCreate(savedInstanceState);
    }

    @Override
    public ActionBar getSupportActionBar() {
        return super.getSupportActionBar();
    }

    @Override
    public void recreate() {
        Utils.changeThemeStyle(getDelegate());
        super.recreate();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public abstract String getTag();
}
