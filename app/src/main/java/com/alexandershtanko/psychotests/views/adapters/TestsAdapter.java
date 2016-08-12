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
import com.alexandershtanko.psychotests.utils.Animate;
import com.alexandershtanko.psychotests.utils.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;

/**
 * Created by aleksandr on 12.06.16.
 */
public class TestsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_TEST = 0;
    private static final int TYPE_TEST_OF_DAY = 1;
    static int[] imgBgs = {R.drawable.circle_gray, R.drawable.circle_white, R.drawable.circle_red, R.drawable.circle_orange};
    SortedList<TestInfo> list = new SortedList<>(TestInfo.class, null);
    private OnItemClickListener onItemClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TEST:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tests, parent, false));
            case TYPE_TEST_OF_DAY:
                return new ViewHolderTOD(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tests_tod, parent, false));
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).isTestOfDay())
            return TYPE_TEST_OF_DAY;
        else
            return TYPE_TEST;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_TEST:
                ((ViewHolder) holder).populate(position, list.get(position), onItemClickListener);
                break;
            case TYPE_TEST_OF_DAY:
                ((ViewHolderTOD) holder).populate(list.get(position), onItemClickListener);
                break;
        }

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        notifyDataSetChanged();
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
        @BindView(R.id.image_done)
        ImageView done;

        @BindView(R.id.like)
        TextView like;
        @BindView(R.id.dislike)
        TextView dislike;

        @BindView(R.id.image_like)
        ImageView likeImage;
        @BindView(R.id.image_dislike)
        ImageView dislikeImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void populate(int position, TestInfo testInfo, OnItemClickListener onItemClickListener) {
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(testInfo.getTestId());
            });
            name.setText(StringUtils.capitalizeSentences(testInfo.getName()));
            like.setText(testInfo.getLikeCount() + "");
            dislike.setText(testInfo.getDislikeCount() + "");


            if (testInfo.getLikeStatus() != null) {
                if (testInfo.getLikeStatus()) {
                    likeImage.setImageResource(R.drawable.ic_thumb_up_orange_24dp);
                    dislikeImage.setImageResource(R.drawable.ic_thumb_down_black_24dp);
                } else {
                    likeImage.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                    dislikeImage.setImageResource(R.drawable.ic_thumb_down_orange_24dp);
                }
            } else {
                likeImage.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                dislikeImage.setImageResource(R.drawable.ic_thumb_down_black_24dp);
            }


            category.setText(StringUtils.capitalizeSentences(testInfo.getCategory()));
            if (testInfo.getImage() != null && !testInfo.getImage().equals(""))
                Glide.with(itemView.getContext()).load(testInfo.getImage()).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .bitmapTransform(new CropCircleTransformation(itemView.getContext())).into(image);
            else
                Glide.with(itemView.getContext()).load(R.drawable.tree_bg).bitmapTransform(new SepiaFilterTransformation(itemView.getContext()), new CropCircleTransformation(itemView.getContext())).into(image);

            //image.setBackgroundResource(imgBgs[position % imgBgs.length]);

            if (testInfo.isDone()) {
                done.setVisibility(View.VISIBLE);
            } else
                done.setVisibility(View.GONE);
        }
    }

    public static class ViewHolderTOD extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.image_done)
        ImageView done;

        @BindView(R.id.like)
        TextView like;
        @BindView(R.id.dislike)
        TextView dislike;

        @BindView(R.id.image_like)
        ImageView likeImage;
        @BindView(R.id.image_dislike)
        ImageView dislikeImage;



        public ViewHolderTOD(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void populate(TestInfo testInfo, OnItemClickListener onItemClickListener) {

            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(testInfo.getTestId());
            });
            name.setText(StringUtils.capitalizeSentences(testInfo.getName()));
            like.setText(testInfo.getLikeCount() + "");
            dislike.setText(testInfo.getDislikeCount() + "");


            if (testInfo.getLikeStatus() != null) {
                if (testInfo.getLikeStatus()) {
                    likeImage.setImageResource(R.drawable.ic_thumb_up_orange_24dp);
                    dislikeImage.setImageResource(R.drawable.ic_thumb_down_white_24dp);
                } else {
                    likeImage.setImageResource(R.drawable.ic_thumb_up_white_24dp);
                    dislikeImage.setImageResource(R.drawable.ic_thumb_down_orange_24dp);
                }
            } else {
                likeImage.setImageResource(R.drawable.ic_thumb_up_white_24dp);
                dislikeImage.setImageResource(R.drawable.ic_thumb_down_white_24dp);
            }

            if (testInfo.getImage() != null && !testInfo.getImage().equals("")) {
                Glide.with(itemView.getContext()).load(testInfo.getImage()).bitmapTransform(new CropCircleTransformation(itemView.getContext())).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(image);
            } else
                Glide.with(itemView.getContext()).load(R.drawable.tree_bg).into(image);
            animateImage(image);

            if (testInfo.isDone()) {
                done.setVisibility(View.VISIBLE);
            } else
                done.setVisibility(View.GONE);

        }

        public void animateImage(ImageView imageView) {
            Animate.animateImageView(imageView);
        }
    }


}
