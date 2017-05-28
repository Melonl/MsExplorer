package com.melonl.msexplorer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private File mClickedFile;
    private String mCurrentPath;
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
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mCurrentPath = FileUtil.getInnerSdcardPath();
        mAdapter = new FileListAdapter(getActivity(), this, addUpperItem(FileUtil.getFileList(mCurrentPath)));
        mAdapter.setItemListener(new FileListAdapter.itemListener() {

            @Override
            public void onItemClick(View v, int pos) {
                //((MainActivity) getActivity()).toast(pos + "");
                mClickedFile = mAdapter.getItemFromPos(pos);
                if (!mClickedFile.canRead()) {
                    ((MainActivity) getActivity()).Snackbar("Permission denied");
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
            public boolean onItemLongClick(View v, int pos) {

                return false;
            }
        });
        mRecyclerView.setAdapter(mAdapter);

    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void refreshList() {
        mAdapter.setFileList(addUpperItem(FileUtil.getFileList(mCurrentPath)));
        ((MainActivity) getActivity()).setSubText(mCurrentPath);
    }

    private List<File> addUpperItem(List<File> oldList) {
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


}
