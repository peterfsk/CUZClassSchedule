package com.asus.zhechuankebiao.mvp.course;

import android.support.annotation.NonNull;

import com.asus.zhechuankebiao.R;
import com.asus.zhechuankebiao.RecyclerBaseAdapter;

import java.util.List;

public class SelectWeekAdapter extends RecyclerBaseAdapter<String> {

    private int selectIndex = 2;

    public SelectWeekAdapter(int itemLayoutId, @NonNull List<String> data) {
        super(itemLayoutId, data);
    }

    @Override
    protected void convert(ViewHolder holder, int position) {
        holder.setText(R.id.tv_text, getData().get(position));
        if (selectIndex == position) {
            holder.itemView.setSelected(true);
        } else {
            holder.itemView.setSelected(false);
        }
    }
}
