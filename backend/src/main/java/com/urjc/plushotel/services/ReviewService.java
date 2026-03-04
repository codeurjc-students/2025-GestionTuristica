package com.urjc.plushotel.services;

import com.urjc.plushotel.repositories.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
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

    public List<ReviewDTO> getReviewsByRoom(Long roomId) {
        List<Review> reviews = reviewRepository.findByReservationRoomId(roomId);
        return reviews.stream().map(this::convertToDTO).toList();
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
