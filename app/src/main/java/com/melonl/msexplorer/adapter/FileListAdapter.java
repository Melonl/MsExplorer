package com.melonl.msexplorer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.melonl.msexplorer.MainActivity;
import com.melonl.msexplorer.R;
import com.melonl.msexplorer.model.FileUtil;

import java.io.File;
import java.util.List;

/**
 * Created by root on 17-5-5.
 */

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.viewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private Context mContext;
    private List<File> mList;

    public FileListAdapter(Context context, List<File> list) {
        mContext = context;
        mList = list;

    }

    @Override
    public FileListAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.item_file_list, parent, false);
        View layout = v.findViewById(R.id.item_filelist_layout);
        layout.setOnClickListener(this);
        layout.setOnLongClickListener(this);
        viewHolder holder = new viewHolder(v);
        holder.tv = (TextView) v.findViewById(R.id.item_filelist_tv);
        holder.subtv = (TextView) v.findViewById(R.id.item_filelist_subtv);
        holder.iv = (ImageView) v.findViewById(R.id.item_filelist_iv);

        return holder;
    }

    @Override
    public void onBindViewHolder(FileListAdapter.viewHolder holder, int position) {
        File f = mList.get(position);
        holder.tv.setText(f.getName());
        if (f.isDirectory()) {
            holder.subtv.setText("Folder");
        } else {
            holder.subtv.setText(FileUtil.getSizeStr(f));
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        ((MainActivity) mContext).toast("you tap!");
    }

    @Override
    public boolean onLongClick(View v) {
        return true;
    }


    class viewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        TextView subtv;
        ImageView iv;

        public viewHolder(View v) {
            super(v);
        }

    }
}
