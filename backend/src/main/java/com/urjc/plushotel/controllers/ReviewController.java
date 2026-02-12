package com.urjc.plushotel.controllers;

import com.urjc.plushotel.services.ReviewService;
import org.springframework.stereotype.Controller;

@Controller
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }
}
