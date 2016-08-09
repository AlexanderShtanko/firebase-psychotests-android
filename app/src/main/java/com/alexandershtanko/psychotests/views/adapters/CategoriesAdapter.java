package com.alexandershtanko.psychotests.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexandershtanko.psychotests.R;
import com.alexandershtanko.psychotests.models.Category;
import com.alexandershtanko.psychotests.utils.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.gpu.SketchFilterTransformation;

/**
 * Created by aleksandr on 12.06.16.
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    List<Category> list = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public void add(Category category)
    {
        if(!list.contains(category)) {
            if (list.add(category)) {
                notifyItemInserted(list.size() - 1);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categories,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.populate(list.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        notifyDataSetChanged();
    }

    public void populate(List<Category> categories) {
        list = categories;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(String category);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.image)
        ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void populate(Category category, OnItemClickListener onItemClickListener) {
            itemView.setOnClickListener(v->{if(onItemClickListener!=null) onItemClickListener.onItemClick(category.getName());});
            name.setText(StringUtils.capitalizeSentences(category.getName()));

            if (category.getImage() != null && !category.getImage().equals("")) {
                Glide.with(itemView.getContext()).load(category.getImage()).bitmapTransform(new SketchFilterTransformation(itemView.getContext()),new CropCircleTransformation(itemView.getContext())).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(image);
            }
            else
                Glide.with(itemView.getContext()).load(R.drawable.tree_bg).into(image);
        }
    }
}
