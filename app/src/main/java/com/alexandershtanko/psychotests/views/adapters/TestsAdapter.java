package com.alexandershtanko.psychotests.views.adapters;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.models.TestInfo;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestsAdapter extends RecyclerView.Adapter<TestsAdapter.ViewHolder> {
    SortedList<TestInfo> list = new SortedList<TestInfo>(TestInfo.class, null);

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tests, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.populate(list.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void setList(SortedList<TestInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.category)
        TextView category;
        @BindView(R.id.image)
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void populate(TestInfo testInfo) {
            name.setText(testInfo.getName());
            category.setText(testInfo.getCategory());
            Picasso.with(itemView.getContext()).load(testInfo.getImage()).into(image);

        }
    }
}
