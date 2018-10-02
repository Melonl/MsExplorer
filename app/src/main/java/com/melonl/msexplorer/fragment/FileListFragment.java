package com.melonl.msexplorer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.github.rubensousa.floatingtoolbar.FloatingToolbar;
import com.melonl.msexplorer.MainActivity;
import com.melonl.msexplorer.R;
import com.melonl.msexplorer.adapter.FileListAdapter;
import com.melonl.msexplorer.model.FileUtil;

import java.io.File;
import java.util.List;

/**
 * Created by Melonl on 17-4-30.
 */

public class FileListFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FileListAdapter mAdapter;
    private BottomSheetDialog mSheet;

    private File mClickedFile;
    private File mLongClickFile;
    private String mCurrentPath;


    private boolean isSelecting = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_file_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.file_list_rv);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);
        mRecyclerView.setLayoutAnimation(animation);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mCurrentPath = FileUtil.getInnerSdcardPath();
        mAdapter = new FileListAdapter(getActivity(), this, addUpperItem(FileUtil.getFileList(mCurrentPath)));
        mAdapter.setItemListener(new FileListAdapter.itemListener() {

            @Override
            public void onItemClick(View v, int pos) {
                mClickedFile = mAdapter.getItemFromPos(pos);

                if (isSelecting) {
                    if (mAdapter.isSelectedFileContains(mClickedFile)) {
                        mAdapter.removeSelectedFile(mClickedFile);
                        if (mAdapter.getSelectedFileCount() == 0) {
                            exitSeclectingMode();
                        }
                    } else {
                        mAdapter.addSelectedFile(mClickedFile);
                    }
                    mAdapter.notifyDataSetChanged();

                    return;
                }


                if (!mClickedFile.canRead()) {
                    getMainActivity().Snackbar("Permission denied");
                    return;
                }
                if (mClickedFile.isDirectory()) {
                    mCurrentPath = mClickedFile.getAbsolutePath();
                    refreshList();
                } else {
                    FileUtil.openFile(getActivity(), mClickedFile);
                }
            }

            @Override
            public boolean onItemLongClick(View v, final int pos) {
                if (pos == 0 || isSelecting) {
                    return true;
                }
                mLongClickFile = mAdapter.getItemFromPos(pos);
                getMainActivity().showFloatingToolbar();

                isSelecting = true;
                mAdapter.addSelectedFile(mLongClickFile);
                mAdapter.notifyDataSetChanged();


                /*
                new MaterialDialog.Builder(getActivity())
                        .title(mLongClickFile.getName())
                        .items("Copy", "Cut", "Delete", "Rename", "As new page")
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                switch (position) {
                                    case 0: //copy case

                                        break;
                                    case 1: //cut case

                                        break;
                                    case 2: //delete case
                                        FileOperator.getInstance().deleteFile(getActivity(), mLongClickFile);
                                        break;
                                    case 3: //rename case

                                        break;
                                    case 4: //as new page

                                        break;
                                }
                            }
                        }).show();

                    */
                return true;
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()),DividerItemDecoration.VERTICAL));

    }


    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public void refreshList() {
        mAdapter.setFileList(addUpperItem(FileUtil.getFileList(mCurrentPath)));
        getMainActivity().setSubText(mCurrentPath);
    }

    public List<File> addUpperItem(List<File> oldList) {
        if (TextUtils.isEmpty(mCurrentPath)) {
            throw new RuntimeException("mCurrentPath is null!");
        }
        File upperFile = new File(mCurrentPath).getParentFile();
        oldList.add(0, upperFile);
        return oldList;
    }

    public String getCurrentPath() {
        return mCurrentPath;
    }

    public void setCurrentPath(String path) {
        if (!TextUtils.isEmpty(path)) {
            mCurrentPath = path;
            refreshList();
        }

    }

    public FloatingToolbar.ItemClickListener getListener() {

        return new FloatingToolbar.ItemClickListener() {
            @Override
            public void onItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.toolbar_copy:

                        break;

                    case R.id.toolbar_delete:

                        break;


                    case R.id.toolbar_close:
                        exitSeclectingMode();
                        break;

                }
            }

            @Override
            public void onItemLongClick(MenuItem item) {

            }
        };
    }

    public void showSheets() {
        if (mSheet == null) {
            View view = View.inflate(getActivity(), R.layout.sheet_dialog_file_option, null);
            mSheet = new BottomSheetDialog(getActivity());
            mSheet.setContentView(view);
            mSheet.show();
        } else {
            mSheet.show();
        }
    }

    public void closeSheets() {
        mSheet.cancel();
    }

    private MainActivity getMainActivity() {
        return ((MainActivity) getActivity());
    }

    public void setIsSelecting(boolean b) {
        isSelecting = b;
    }

    public boolean isSelecting() {
        return isSelecting;
    }

    public void exitSeclectingMode() {
        mAdapter.clearSelectedFile();
        mAdapter.notifyDataSetChanged();
        getMainActivity().hideFloatingToolbar();
        isSelecting = false;
    }


}
