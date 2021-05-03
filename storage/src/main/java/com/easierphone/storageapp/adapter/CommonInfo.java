package com.easierphone.storageapp.adapter;

import android.database.Cursor;

import com.easierphone.storageapp.model.DocumentInfo;
import com.easierphone.storageapp.model.RootInfo;

import static com.easierphone.storageapp.adapter.HomeAdapter.TYPE_RECENT;

public class CommonInfo {

    public int type;
    public DocumentInfo documentInfo;
    public RootInfo rootInfo;

    public static CommonInfo from(RootInfo rootInfo, int type){
        CommonInfo commonInfo = new CommonInfo();
        commonInfo.type = type;
        commonInfo.rootInfo = rootInfo;
        return commonInfo;
    }

    public static CommonInfo from(DocumentInfo documentInfo, int type){
        CommonInfo commonInfo = new CommonInfo();
        commonInfo.type = type;
        commonInfo.documentInfo = documentInfo;
        return commonInfo;
    }

    public static CommonInfo from(Cursor cursor){
        DocumentInfo documentInfo = DocumentInfo.fromDirectoryCursor(cursor);
        CommonInfo commonInfo = from(documentInfo, TYPE_RECENT);
        return commonInfo;
    }
}
