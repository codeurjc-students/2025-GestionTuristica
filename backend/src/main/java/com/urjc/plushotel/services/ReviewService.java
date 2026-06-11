package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.request.ReviewCreationRequest;
import com.urjc.plushotel.dtos.request.ReviewUpdateRequest;
import com.urjc.plushotel.dtos.response.ReviewDTO;
import com.urjc.plushotel.entities.Reservation;
import com.urjc.plushotel.entities.Review;
import com.urjc.plushotel.entities.User;
import com.urjc.plushotel.exceptions.ReviewNotFoundException;
import com.urjc.plushotel.repositories.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CustomUserDetailsService userDetailsService;
    private final ReservationService reservationService;

    public ReviewService(ReviewRepository reviewRepository,
                         CustomUserDetailsService userDetailsService,
                         ReservationService reservationService) {
        this.reviewRepository = reviewRepository;
        this.userDetailsService = userDetailsService;
        this.reservationService = reservationService;
    }

    public ReviewDTO createReview(ReviewCreationRequest request, Authentication authentication) {
        User user = userDetailsService.loadUserByUsername(authentication.getName());

        Reservation reservation =
                reservationService.getReservationEntityByIdentifier(request.getReservationIdentifier());

        Review review = Review.builder()
                .message(request.getMessage())
                .user(user)
                .reservation(reservation)
                .rating(request.getRating())
                .build();

        reservationService.updateReviewed(request.getReservationIdentifier(), true);

        return convertToDTO(reviewRepository.save(review));
    }

    public Page<ReviewDTO> getReviewsByRoom(Long roomId, int pageNumber) {
        Page<Review> reviews = reviewRepository.findByReservationRoomId(roomId,
                Pageable.ofSize(5).withPage(pageNumber));
        return reviews.map(this::convertToDTO);
    }

    public ReviewDTO getReviewByReservationIdentifier(String reservationIdentifier) {
        Review review = reviewRepository.findByReservationReservationIdentifier(reservationIdentifier).orElseThrow(
                () -> new ReviewNotFoundException("There's no review linked to that reservation, or there's no" +
                        " reservation with that reservation identifier")
        );
        return convertToDTO(review);
    }

    private ReviewDTO convertToDTO(Review review) {
        return new ReviewDTO(review.getUser().getEmail(),
                review.getReservation().getRoom().getName(),
                review.getMessage(),
                review.getRating(),
                review.getReservation().getReservationIdentifier(),
                review.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss"))
        );
    }

    public ReviewDTO updateReview(String reservationIdentifier, ReviewUpdateRequest request) {
        Review review = reviewRepository.findByReservationReservationIdentifier(reservationIdentifier).orElseThrow(
                () -> new ReviewNotFoundException("There's no review linked to that reservation, or there's no" +
                        " reservation with that reservation identifier")
        );
        review.setMessage(request.getMessage());
        review.setRating(request.getRating());
        return convertToDTO(reviewRepository.save(review));
    }

    public void deleteReview(String reservationIdentifier) {
        Review review = reviewRepository.findByReservationReservationIdentifier(reservationIdentifier).orElseThrow(
                () -> new ReviewNotFoundException("There's no review linked to that reservation, or there's no" +
                        " reservation with that reservation identifier")
        );

        reservationService.updateReviewed(reservationIdentifier, false);

        reviewRepository.delete(review);
    }
}
