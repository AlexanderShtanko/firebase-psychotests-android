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
import com.alexandershtanko.psychotests.views.picasso.CircleTransformation;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestsAdapter extends RecyclerView.Adapter<TestsAdapter.ViewHolder> {
    static int[] imgBgs = {R.drawable.circle_gray, R.drawable.circle_blue, R.drawable.circle_red, R.drawable.circle_green, R.drawable.circle_orange};
    SortedList<TestInfo> list = new SortedList<>(TestInfo.class, null);
    private OnItemClickListener onItemCLickListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tests, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.populate(position, list.get(position), onItemCLickListener);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemCLickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(SortedList<TestInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(String testId);
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

        public void populate(int position, TestInfo testInfo, OnItemClickListener onItemClickListener) {
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(testInfo.getTestId());
            });
            name.setText(testInfo.getName());
            category.setText(testInfo.getCategory());
            if (testInfo.getImage() != null && testInfo.getImage().length() > 0)
                Picasso.with(itemView.getContext()).load(testInfo.getImage()).transform(new CircleTransformation()).into(image);
            else
                Picasso.with(itemView.getContext()).load(R.drawable.ic_dashboard_white_24dp).transform(new CircleTransformation()).into(image);

            image.setBackgroundResource(imgBgs[position % imgBgs.length]);
        }
    }
}
