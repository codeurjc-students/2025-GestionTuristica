package com.urjc.plushotel.controllers;

import com.urjc.plushotel.dtos.request.ReviewCreationRequest;
import com.urjc.plushotel.dtos.request.ReviewUpdateRequest;
import com.urjc.plushotel.dtos.response.ReviewDTO;
import com.urjc.plushotel.services.ReviewService;
import com.urjc.plushotel.utils.EndpointConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(EndpointConstants.ReviewsEndpoints.REVIEWS_BASE_URL)
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewCreationRequest request,
                                                  Authentication authentication) {
        ReviewDTO review = reviewService.createReview(request, authentication);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/reservation/{reservationIdentifier}")
                .buildAndExpand(review.getReservationIdentifier())
                .toUri();

        return ResponseEntity.created(location).body(review);
    }

    @GetMapping(EndpointConstants.ReviewsEndpoints.REVIEWS_ROOM_URL)
    public ResponseEntity<List<ReviewDTO>> getReviewsByRoom(@PathVariable Long roomId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByRoom(roomId);
        return ResponseEntity.ok(reviews);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(EndpointConstants.ReviewsEndpoints.REVIEW_RESERVATION_IDENTIFIER_URL)
    public ResponseEntity<ReviewDTO> getReviewByReservationIdentifier(@PathVariable String reservationIdentifier) {
        ReviewDTO review = reviewService.getReviewByReservationIdentifier(reservationIdentifier);
        return ResponseEntity.ok(review);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping(EndpointConstants.ReviewsEndpoints.REVIEW_RESERVATION_IDENTIFIER_URL)
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable String reservationIdentifier,
                                                  @RequestBody ReviewUpdateRequest request) {
        ReviewDTO review = reviewService.updateReview(reservationIdentifier, request);
        return ResponseEntity.ok(review);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping(EndpointConstants.ReviewsEndpoints.REVIEW_RESERVATION_IDENTIFIER_URL)
    public ResponseEntity<ReviewDTO> deleteReview(@PathVariable String reservationIdentifier) {
        reviewService.deleteReview(reservationIdentifier);
        return ResponseEntity.noContent().build();
    }
}
