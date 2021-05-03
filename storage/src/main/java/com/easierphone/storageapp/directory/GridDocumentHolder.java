package com.easierphone.storageapp.directory;

import android.content.Context;
import android.view.ViewGroup;

import com.easierphone.storageapp.R;
import com.easierphone.storageapp.common.RecyclerFragment.RecyclerItemClickListener.OnItemClickListener;
import com.easierphone.storageapp.directory.DocumentsAdapter.Environment;

public class GridDocumentHolder extends ListDocumentHolder {

    public GridDocumentHolder(Context context, ViewGroup parent,
                              OnItemClickListener onItemClickListener, Environment environment) {
        super(context, parent, R.layout.item_doc_grid, onItemClickListener, environment);
    }

}
