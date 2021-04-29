package com.easierphone.storaageapp.directory;

import android.content.Context;
import android.view.ViewGroup;

import com.easierphone.storaageapp.R;
import com.easierphone.storaageapp.common.RecyclerFragment.RecyclerItemClickListener.OnItemClickListener;
import com.easierphone.storaageapp.directory.DocumentsAdapter.Environment;

public class GridDocumentHolder extends ListDocumentHolder {

    public GridDocumentHolder(Context context, ViewGroup parent,
                              OnItemClickListener onItemClickListener, Environment environment) {
        super(context, parent, R.layout.item_doc_grid, onItemClickListener, environment);
    }

}
