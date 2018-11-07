package com.example.lldong0.webot.home;

import com.example.lldong0.webot.model.Review;

import java.util.List;

public interface ReviewListener {
    void runListener(List<Review> reviewList);
}
