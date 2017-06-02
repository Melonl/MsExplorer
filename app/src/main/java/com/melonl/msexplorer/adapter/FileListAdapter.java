package com.melonl.msexplorer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.melonl.msexplorer.R;
import com.melonl.msexplorer.fragment.FileListFragment;
import com.melonl.msexplorer.model.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 17-5-5.
 */

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.viewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private Context mContext;
    private List<File> mList;
    private itemListener mListener;
    private FileListFragment mCurrentFragment;

    public FileListAdapter(Context context, FileListFragment fragment, List<File> list) {
        mContext = context;
        mList = list;
        mCurrentFragment = fragment;
    }

    @Override
    public FileListAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.item_file_list, parent, false);

        viewHolder holder = new viewHolder(v);
        holder.tv = (TextView) v.findViewById(R.id.item_filelist_tv);
        holder.subtv = (TextView) v.findViewById(R.id.item_filelist_subtv);
        holder.iv = (ImageView) v.findViewById(R.id.item_filelist_iv);
        holder.layout = (LinearLayout) v.findViewById(R.id.item_filelist_layout);

        holder.layout.setOnClickListener(this);
        holder.layout.setOnLongClickListener(this);

        return holder;
    }

    @Override
    public void onBindViewHolder(FileListAdapter.viewHolder holder, int position) {
        File f = mList.get(position);
        holder.layout.setTag(R.string.mark, position);
        holder.tv.setText(f.getName());
        if (f.isDirectory()) {
            holder.subtv.setText("Folder");
        } else {
            holder.subtv.setText(FileUtil.getSizeStr(f));
        }

        if (position == 0 && mCurrentFragment.getCurrentPath().length() > f.getAbsolutePath().length()) {
            holder.tv.setText("..");
            holder.subtv.setText("Upper");
        }

    }

    public File getItemFromPos(int pos) {
        return mList.get(pos);
    }

    public int getItemPosition(File file) {
        return mList.indexOf(file);
    }

    public void setFileList(List<File> newList) {
        mList.clear();
        mList.addAll(newList);
        notifyDataSetChanged();
    }

    public void deletingAnimation(File f) {
        int pos = getItemPosition(f);
        mList.remove(f);
        notifyItemRemoved(pos);

        if (pos != mList.size()) {
            notifyItemRangeChanged(pos, mList.size() - pos);
        }
    }

    public void addingAnimation(File f) {
        mList.remove(0);
        mList.add(f);
        FileUtil.customSort((ArrayList<File>) mList);
        mCurrentFragment.addUpperItem(mList);
        int pos = mList.indexOf(f);
        mList.remove(f);
        mList.add(pos, f);
        notifyItemInserted(pos);

        if (pos != mList.size()) {
            notifyItemRangeChanged(pos, mList.size() - pos);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setItemListener(itemListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag(R.string.mark);
        if (mListener != null) {
            mListener.onItemClick(v, pos);
        }

    }

    @Override
    public boolean onLongClick(View v) {
        int pos = (int) v.getTag(R.string.mark);
        boolean b = false;
        if (mListener != null) {
            b = mListener.onItemLongClick(v, pos);
        }
        return b;
    }

    public interface itemListener {
        void onItemClick(View v, int pos);

        boolean onItemLongClick(View v, int pos);
    }

    class viewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout;
        TextView tv;
        TextView subtv;
        ImageView iv;

        public viewHolder(View v) {
            super(v);
        }

    }
}
