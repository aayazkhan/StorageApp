package com.easierphone.storaageapp.directory;

import com.easierphone.storaageapp.directory.DocumentsAdapter.Environment;

public class LoadingFooter extends Footer {

    public LoadingFooter(Environment environment, int type) {
        super(type);
        mEnv = environment;
        mIcon = 0;
        mMessage = "";
    }
}