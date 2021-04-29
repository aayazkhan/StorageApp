/*
 * Copyright (C) 2014 Hari Krishna Dulipudi
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.easierphone.storaageapp.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.easierphone.storaageapp.BaseActivity;
import com.easierphone.storaageapp.DocumentsActivity;
import com.easierphone.storaageapp.DocumentsApplication;
import com.easierphone.storaageapp.R;
import com.easierphone.storaageapp.adapter.CommonInfo;
import com.easierphone.storaageapp.adapter.HomeAdapter;
import com.easierphone.storaageapp.common.RecyclerFragment;
import com.easierphone.storaageapp.cursor.LimitCursorWrapper;
import com.easierphone.storaageapp.loader.RecentLoader;
import com.easierphone.storaageapp.misc.IconHelper;
import com.easierphone.storaageapp.misc.RootsCache;
import com.easierphone.storaageapp.misc.Utils;
import com.easierphone.storaageapp.model.DirectoryResult;
import com.easierphone.storaageapp.model.DocumentInfo;
import com.easierphone.storaageapp.model.RootInfo;
import com.easierphone.storaageapp.setting.SettingsActivity;

import java.util.ArrayList;

import static com.easierphone.storaageapp.BaseActivity.State.MODE_GRID;
import static com.easierphone.storaageapp.DocumentsApplication.isTelevision;
import static com.easierphone.storaageapp.DocumentsApplication.isWatch;
import static com.easierphone.storaageapp.adapter.HomeAdapter.TYPE_MAIN;
import static com.easierphone.storaageapp.adapter.HomeAdapter.TYPE_RECENT;
import static com.easierphone.storaageapp.adapter.HomeAdapter.TYPE_SHORTCUT;

/**
 * Display home.
 */
public class HomeFragment extends RecyclerFragment implements HomeAdapter.OnItemClickListener {
    public static final String TAG = "HomeFragment";
    private static final int MAX_RECENT_COUNT = isTelevision() ? 20 : 10;

    private final int mLoaderId = 42;
    private RootsCache roots;
    private LoaderManager.LoaderCallbacks<DirectoryResult> mCallbacks;
    private RootInfo mHomeRoot;
    private BaseActivity mActivity;
    private IconHelper mIconHelper;
    private ArrayList<CommonInfo> mainData;
    //private ArrayList<CommonInfo> shortcutsData;
    private HomeAdapter mAdapter;
    //private RootInfo processRoot;
    private int totalSpanSize;

    public static void show(FragmentManager fm) {
        final HomeFragment fragment = new HomeFragment();
        final FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container_directory, fragment, TAG);
        ft.commitAllowingStateLoss();
    }

    public static HomeFragment get(FragmentManager fm) {
        return (HomeFragment) fm.findFragmentByTag(TAG);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        totalSpanSize = getResources().getInteger(R.integer.home_span);
        mActivity = ((BaseActivity) getActivity());
        mIconHelper = new IconHelper(mActivity, MODE_GRID);
        ArrayList<CommonInfo> data = new ArrayList<>();
        if (null == mAdapter) {
            mAdapter = new HomeAdapter(getActivity(), data, mIconHelper);
            mAdapter.setOnItemClickListener(this);
        }

        roots = DocumentsApplication.getRootsCache(getActivity());
        mHomeRoot = roots.getHomeRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        showData();
    }

    public void showData() {
        roots = DocumentsApplication.getRootsCache(getActivity());
        mIconHelper.setThumbnailsEnabled(mActivity.getDisplayState().showThumbnail);
        getMainData();
//        getShortcutsData();
//        getRecentsData();
        ArrayList<CommonInfo> data = new ArrayList<>();
        data.addAll(mainData);
//        data.addAll(shortcutsData);
        mAdapter.setData(data);
    }

    private void getMainData() {
        mainData = new ArrayList<>();
        final RootInfo primaryRoot = roots.getPrimaryRoot();
        final RootInfo secondaryRoot = roots.getSecondaryRoot();
        final RootInfo usbRoot = roots.getUSBRoot();
//        processRoot = roots.getProcessRoot();
        int type = !isWatch() ? TYPE_MAIN : TYPE_SHORTCUT;
        if (null != primaryRoot) {
            mainData.add(CommonInfo.from(primaryRoot, type));
        }
        if (null != secondaryRoot) {
            mainData.add(CommonInfo.from(secondaryRoot, type));
        }
        if (null != usbRoot) {
            mainData.add(CommonInfo.from(usbRoot, type));
        }
//        if(null != processRoot){
//            mainData.add(CommonInfo.from(processRoot, type));
//        }
    }

//    private void getShortcutsData(){
//        ArrayList<RootInfo> data = roots.getShortcutsInfo();
//        shortcutsData = new ArrayList<>();
//        for (RootInfo root: data) {
//            shortcutsData.add(CommonInfo.from(root, TYPE_SHORTCUT));
//        }
//        if(isWatch()) {
//            RootInfo rootInfo = new RootInfo();
//            rootInfo.authority = null;
//            rootInfo.rootId = "clean";
//            rootInfo.icon = R.drawable.ic_clean;
//            rootInfo.flags = DocumentsContract.Root.FLAG_LOCAL_ONLY;
//            rootInfo.title = "Clean RAM";
//            rootInfo.availableBytes = -1;
//            rootInfo.deriveFields();
//            shortcutsData.add(CommonInfo.from(rootInfo, TYPE_SHORTCUT));
//        }
//
//    }

    private void getRecentsData() {
        final BaseActivity.State state = getDisplayState(this);
        mCallbacks = new LoaderManager.LoaderCallbacks<DirectoryResult>() {

            @Override
            public Loader<DirectoryResult> onCreateLoader(int id, Bundle args) {
                return new RecentLoader(getActivity(), roots, state);
            }

            @Override
            public void onLoadFinished(Loader<DirectoryResult> loader, DirectoryResult result) {
                if (!isAdded())
                    return;
                if (null != result.cursor && result.cursor.getCount() != 0) {
                    mAdapter.setRecentData(new LimitCursorWrapper(result.cursor, MAX_RECENT_COUNT));
                }
            }

            @Override
            public void onLoaderReset(Loader<DirectoryResult> loader) {
                mAdapter.setRecentData(null);
            }
        };
        if (SettingsActivity.getDisplayRecentMedia()) {
            getLoaderManager().restartLoader(mLoaderId, null, mCallbacks);
        }
    }

    public void reloadData() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showData();
            }
        }, 500);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(HomeAdapter.ViewHolder item, View view, int position) {
        switch (item.commonInfo.type) {
            case TYPE_MAIN:
            case TYPE_SHORTCUT:
                if (item.commonInfo.rootInfo.rootId.equals("clean")) {
                    cleanRAM();
                } else {
                    openRoot(item.commonInfo.rootInfo);
                }
                break;
            case TYPE_RECENT:
                try {
                    final DocumentInfo documentInfo = ((HomeAdapter.GalleryViewHolder) item).getItem(position);
                    openDocument(documentInfo);
                } catch (Exception ignore) {
                }
                break;
        }
    }

    @Override
    public void onItemLongClick(HomeAdapter.ViewHolder item, View view, int position) {

    }

    @Override
    public void onItemViewClick(HomeAdapter.ViewHolder item, View view, int position) {
        switch (view.getId()) {
//            case R.id.recents:
//                openRoot(roots.getRecentsRoot());
//                break;

            case R.id.action:
                Bundle params = new Bundle();
                Intent intent = new Intent(Settings.ACTION_INTERNAL_STORAGE_SETTINGS);
                if (Utils.isIntentAvailable(getActivity(), intent)) {
                    getActivity().startActivity(intent);
                } else {
                    Utils.showSnackBar(getActivity(), "Coming Soon!");
                }
                break;
        }

    }

    private void cleanRAM() {
        Bundle params = new Bundle();
//        new OperationTask(processRoot).execute();
    }

//    private class OperationTask extends AsyncTask<Void, Void, Boolean> {
//
//        private Dialog progressDialog;
//        private RootInfo root;
//        private long currentAvailableBytes;
//
//        public OperationTask(RootInfo root) {
//            DialogBuilder builder = new DialogBuilder(getActivity());
//            builder.setMessage("Cleaning up RAM...");
//            builder.setIndeterminate(true);
//            progressDialog = builder.create();
//            this.root = root;
//            currentAvailableBytes = root.availableBytes;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            progressDialog.show();
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            boolean result = false;
//            cleanupMemory(getActivity());
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//            super.onPostExecute(result);
//            if (!Utils.isActivityAlive(getActivity())) {
//                return;
//            }
//            AppsProvider.notifyDocumentsChanged(getActivity(), root.rootId);
//            AppsProvider.notifyRootsChanged(getActivity());
//            RootsCache.updateRoots(getActivity(), AppsProvider.AUTHORITY);
//            roots = DocumentsApplication.getRootsCache(getActivity());
//            final Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if(currentAvailableBytes != 0) {
//                        long availableBytes = processRoot.availableBytes - currentAvailableBytes;
//                        String summaryText = availableBytes <= 0 ? "Already cleaned up!" :
//                                getActivity().getString(R.string.root_available_bytes,
//                                        Formatter.formatFileSize(getActivity(), availableBytes));
//                        Utils.showSnackBar(getActivity(), summaryText);
//                    }
//                    progressDialog.dismiss();
//                }
//            }, 500);
//        }
//    }

    private static BaseActivity.State getDisplayState(Fragment fragment) {
        return ((BaseActivity) fragment.getActivity()).getDisplayState();
    }

    private void openRoot(RootInfo rootInfo) {
        DocumentsActivity activity = ((DocumentsActivity) getActivity());
        activity.onRootPicked(rootInfo, mHomeRoot);
    }

    private void openDocument(DocumentInfo doc) {
        ((BaseActivity) getActivity()).onDocumentPicked(doc);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setListAdapter(mAdapter);
        showData();
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }

        ((GridLayoutManager) getListView().getLayoutManager()).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int spanSize = 1;
                switch (mAdapter.getItem(position).type) {
                    case TYPE_MAIN:
                        spanSize = totalSpanSize;
                        break;
                    case TYPE_SHORTCUT:
                        spanSize = isWatch() ? 1 : 2;
                        break;
                    case TYPE_RECENT:
                        spanSize = totalSpanSize;
                        break;
                }
                return spanSize;
            }
        });
    }
}