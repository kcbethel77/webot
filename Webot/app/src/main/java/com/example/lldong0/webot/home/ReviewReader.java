package com.example.lldong0.webot.home;

import android.util.Log;

import com.example.lldong0.webot.model.Review;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReviewReader {
    private DatabaseReference postReference = FirebaseDatabase.getInstance().getReference();
    private List<Review> reviewArrayList;

    public void postReviews(final ReviewListener reviewListener) {
        reviewArrayList = new ArrayList<>();

        Query query = postReference.child("reviews").orderByChild("writtenTime");
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Review review = data.getValue(Review.class);
                    try {
                        Review newReview = new Review(review.getUserUid(), review.getUserEmail()
                                , review.getContents(), review.getWrittenTime());
                        reviewArrayList.add(newReview);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                reviewListener.runListener(reviewArrayList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("fail", databaseError.toException());
            }
        });
    }
}
