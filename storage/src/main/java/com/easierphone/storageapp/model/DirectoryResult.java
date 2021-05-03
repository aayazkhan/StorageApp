package com.easierphone.storageapp.model;

import android.content.ContentProviderClient;
import android.database.Cursor;

import com.easierphone.storageapp.libcore.io.IoUtils;
import com.easierphone.storageapp.misc.ContentProviderClientCompat;

import java.io.Closeable;

import static com.easierphone.storageapp.BaseActivity.State.MODE_UNKNOWN;
import static com.easierphone.storageapp.BaseActivity.State.SORT_ORDER_UNKNOWN;

public class DirectoryResult implements Closeable {
	public ContentProviderClient client;
    public Cursor cursor;
    public Exception exception;

    public int mode = MODE_UNKNOWN;
    public int sortOrder = SORT_ORDER_UNKNOWN;

    @Override
    public void close() {
        IoUtils.closeQuietly(cursor);
        ContentProviderClientCompat.releaseQuietly(client);
        cursor = null;
        client = null;
    }
}