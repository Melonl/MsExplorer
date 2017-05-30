package com.melonl.msexplorer.model;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.melonl.msexplorer.MainActivity;
import com.melonl.msexplorer.adapter.FileListAdapter;
import com.melonl.msexplorer.fragment.FileListFragment;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Melonl on 17-5-28.
 */

public class FileOperator {

    private final static int FLAG_DELETE_FINISH = 0x154;
    private final static int FLAG_CLEAR_CONTEXT = 0x158;
    private final static int FLAG_REFREAH_LIST = 0x163;
    private final static String FLAG_TIP_KEY = "KEY";
    private static FileOperator mOperator;
    private ExecutorService mSingleThreadPool;
    private MaterialDialog mDeletingDialog;
    private Handler mHandler;
    private Context mCurrentContext;


    private FileOperator() {
        mSingleThreadPool = Executors.newSingleThreadExecutor();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case FLAG_DELETE_FINISH: //delete
                        if (mDeletingDialog == null) {
                            throw new RuntimeException("mDeletingDialog = null !");
                        }
                        mDeletingDialog.dismiss();
                        String str = msg.getData().getString(FLAG_TIP_KEY);
                        if (str != null) {
                            ((MainActivity) mCurrentContext).Snackbar("Delete completed");
                        }
                        break;
                    case FLAG_CLEAR_CONTEXT:
                        mCurrentContext = null;
                        System.gc();
                        break;
                    case FLAG_REFREAH_LIST:
                        Fragment f = ((MainActivity) mCurrentContext).getCurrentfragment();
                        if (f instanceof FileListFragment) {
                            refreshList((FileListFragment) f);
                        }
                        break;
                }
            }
        };
    }

    public static FileOperator getInstance() {
        if (mOperator == null) {
            mOperator = new FileOperator();
        }

        return mOperator;
    }

    private void refreshList(FileListFragment f) {
        f.refreshList();
    }

    public void deleteFile(Context context, File... opFiles) {
        mCurrentContext = context;
        if (opFiles == null || opFiles.length == 0) {
            throw new RuntimeException("opFiles is null or size is 0!");
        }
        String name;
        if (opFiles.length > 1) {
            name = opFiles.length + " Files";
        } else {
            name = opFiles[0].getName();
        }
        askDelete(context, opFiles);
    }

    private void askDelete(final Context context, final File[] opFiles) {
        final String name = (opFiles.length > 1 ? opFiles.length + "Files" : opFiles[0].getName());
        new MaterialDialog.Builder(context)
                .title("Delete")
                .content("Are you sure you want to delete " + name)
                .positiveText("Delete")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        deleteFileLogic(context, opFiles, name);
                    }
                })
                .negativeText("Cancel")
                .show();
    }

    private void deleteFileLogic(final Context context, final File[] opFiles, String name) {
        mDeletingDialog = new MaterialDialog.Builder(context)
                .title("Delete")
                .content("Deleting " + name)
                .progressIndeterminateStyle(false)
                .build();
        mDeletingDialog.show();
        mSingleThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                for (final File f : opFiles) {
                    FileUtil.deleteFile(f);

                }
                Message msg = new Message();
                msg.what = FLAG_DELETE_FINISH;
                Bundle b = new Bundle();
                b.putString(FLAG_TIP_KEY, "Delete completed");
                msg.setData(b);
                mHandler.sendMessage(msg);

                ((Activity) mCurrentContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Fragment frag = ((MainActivity) mCurrentContext).getCurrentfragment();

                        FileListFragment fragment = (FileListFragment) frag;
                        for (File f : opFiles) {
                            ((FileListAdapter) fragment.getAdapter()).DeletingAnimation(f);
                        }
                    }
                });

                mHandler.sendEmptyMessage(FLAG_CLEAR_CONTEXT);
            }
        });
    }
}
