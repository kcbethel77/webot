package com.example.lldong0.webot.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lldong0.webot.R;
import com.example.lldong0.webot.model.Review;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviewArrayList;

    public ReviewAdapter(List<Review> reviewArrayList) {
        this.reviewArrayList = reviewArrayList;
    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewArrayList.get(position);
        holder.setData(review);
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.review_userId)
        TextView review_userEmail;
        @BindView(R.id.review_contents)
        TextView review_contents;
        @BindView(R.id.review_writtenTime)
        TextView review_writtenTime;

        private ReviewViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        private void setData(Review review) {
            review_userEmail.setText(review.getUserEmail());
            review_contents.setText(review.getContents());
            review_writtenTime.setText(review.getWrittenTime());
        }
    }
}
