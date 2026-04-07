package com.urjc.plushotel.services;

import com.urjc.plushotel.dtos.request.ReviewCreationRequest;
import com.urjc.plushotel.dtos.request.ReviewUpdateRequest;
import com.urjc.plushotel.dtos.response.ReviewDTO;
import com.urjc.plushotel.entities.*;
import com.urjc.plushotel.exceptions.ReviewNotFoundException;
import com.urjc.plushotel.repositories.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private ReservationService reservationService;

    @Mock
    private Authentication authentication;

    @Test
    void createReviewTest() {
        User user = User.builder().email("john@example.com").build();

        Room room = Room.builder().name("Room1").build();

        Reservation reservation = new Reservation(1L, "RSV-123", room, user, ReservationStatus.ACTIVE, false,
                BigDecimal.TEN, LocalDateTime.now(), LocalDate.parse("2025-12-24"), LocalDate.parse("2025-12-26"));

        ReviewCreationRequest request = new ReviewCreationRequest("message", "RSV-123", 4.5);

        Review review = new Review(1L, user, reservation, "message", 4.5, LocalDateTime.now());

        when(userDetailsService.loadUserByUsername("john@example.com")).thenReturn(user);
        when(reservationService.getReservationEntityByIdentifier("RSV-123")).thenReturn(reservation);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(authentication.getName()).thenReturn("john@example.com");

        ReviewDTO response = reviewService.createReview(request, authentication);

        assertNotNull(response);
        assertEquals(user.getEmail(), response.getUserEmail());
        assertEquals(reservation.getRoom().getName(), response.getRoomName());
        assertEquals(request.getMessage(), response.getMessage());
        assertEquals(request.getRating(), response.getRating());
        assertEquals(reservation.getReservationIdentifier(), response.getReservationIdentifier());
    }

    @Test
    void getReviewsByRoomTest() {
        User user1 = User.builder().email("john@example.com").build();
        User user2 = User.builder().email("mark@example.com").build();

        Room room = Room.builder().name("Room1").build();

        Reservation reservation1 = new Reservation(1L, "RSV-123", room, user1, ReservationStatus.ACTIVE, false,
                BigDecimal.TEN, LocalDateTime.now(), LocalDate.parse("2025-12-24"), LocalDate.parse("2025-12-26"));

        Reservation reservation2 = new Reservation(2L, "RSV-1234", room, user1, ReservationStatus.ACTIVE, false,
                BigDecimal.TEN, LocalDateTime.now(), LocalDate.parse("2025-12-24"), LocalDate.parse("2025-12-26"));

        Review review1 = new Review(1L, user1, reservation1, "message1", 4.5, LocalDateTime.now());
        Review review2 = new Review(2L, user2, reservation2, "message2", 4.0, LocalDateTime.now());
        List<Review> reviews = List.of(review1, review2);

        when(reviewRepository.findByReservationRoomId(1L)).thenReturn(reviews);

        List<ReviewDTO> reviewsByRoom = reviewService.getReviewsByRoom(1L);

        assertNotNull(reviewsByRoom);
        assertEquals(review1.getReservation().getReservationIdentifier(),
                reviewsByRoom.getFirst().getReservationIdentifier());
        assertEquals(review2.getReservation().getReservationIdentifier(),
                reviewsByRoom.getLast().getReservationIdentifier());
        assertEquals(review1.getReservation().getRoom().getName(),
                reviewsByRoom.getFirst().getRoomName());
        assertEquals(review2.getReservation().getRoom().getName(),
                reviewsByRoom.getLast().getRoomName());
        assertEquals(review1.getUser().getEmail(),
                reviewsByRoom.getFirst().getUserEmail());
        assertEquals(review2.getUser().getEmail(),
                reviewsByRoom.getLast().getUserEmail());
    }

    @Test
    void getReviewByReservationIdentifierSuccessfulTest() {
        User user = User.builder().email("john@example.com").build();

        Room room = Room.builder().name("Room1").build();

        Reservation reservation = new Reservation(1L, "RSV-123", room, user, ReservationStatus.ACTIVE, false,
                BigDecimal.TEN, LocalDateTime.now(), LocalDate.parse("2025-12-24"), LocalDate.parse("2025-12-26"));

        Review review = new Review(1L, user, reservation, "message", 4.5, LocalDateTime.now());

        when(reviewRepository.findByReservationReservationIdentifier("RSV-123")).thenReturn(Optional.of(review));

        ReviewDTO response = reviewService.getReviewByReservationIdentifier("RSV-123");

        assertNotNull(response);
        assertEquals(review.getMessage(), response.getMessage());
        assertEquals(review.getRating(), response.getRating());
        assertEquals(review.getReservation().getReservationIdentifier(), response.getReservationIdentifier());
        assertEquals(reservation.getReservationIdentifier(), response.getReservationIdentifier());
        assertEquals(user.getEmail(), response.getUserEmail());
    }

    @Test
    void getReviewByReservationIdentifierNotFoundTest() {

        when(reviewRepository.findByReservationReservationIdentifier("RSV-123")).thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundException.class, () -> reviewService.getReviewByReservationIdentifier("RSV-123"));

        verify(reviewRepository, times(1)).findByReservationReservationIdentifier("RSV-123");
    }


    @Test
    void updateReviewSuccessfulTest() {
        User user = User.builder().email("john@example.com").build();

        Room room = Room.builder().name("Room1").build();

        Reservation reservation = new Reservation(1L, "RSV-123", room, user, ReservationStatus.ACTIVE, false,
                BigDecimal.TEN, LocalDateTime.now(), LocalDate.parse("2025-12-24"), LocalDate.parse("2025-12-26"));

        Review review = new Review(1L, user, reservation, "Old message", 4.5, LocalDateTime.now());

        ReviewUpdateRequest request = new ReviewUpdateRequest("New message", 3.5);

        when(reviewRepository.findByReservationReservationIdentifier("RSV-123")).thenReturn(Optional.of(review));

        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        ReviewDTO response = reviewService.updateReview("RSV-123", request);

        assertNotNull(response);
        assertEquals(request.getMessage(), response.getMessage());
        assertEquals(request.getRating(), response.getRating());

        verify(reviewRepository, times(1)).findByReservationReservationIdentifier("RSV-123");
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void updateReviewNotFoundTest() {

        ReviewUpdateRequest request = new ReviewUpdateRequest("New message", 3.5);

        when(reviewRepository.findByReservationReservationIdentifier("RSV-123")).thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundException.class, () -> reviewService.updateReview("RSV-123", request));

        verify(reviewRepository, times(1)).findByReservationReservationIdentifier("RSV-123");
    }

    @Test
    void deleteReviewSucessfulTest() {
        User user = User.builder().email("john@example.com").build();

        Room room = Room.builder().name("Room1").build();

        Reservation reservation = new Reservation(1L, "RSV-123", room, user, ReservationStatus.ACTIVE, false,
                BigDecimal.TEN, LocalDateTime.now(), LocalDate.parse("2025-12-24"), LocalDate.parse("2025-12-26"));

        Review review = new Review(1L, user, reservation, "message", 4.5, LocalDateTime.now());

        when(reviewRepository.findByReservationReservationIdentifier("RSV-123")).thenReturn(Optional.of(review));

        reviewService.deleteReview("RSV-123");

        verify(reviewRepository, times(1)).findByReservationReservationIdentifier("RSV-123");
        verify(reviewRepository, times(1)).delete(review);
    }

    @Test
    void deleteReviewNotFoundTest() {

        when(reviewRepository.findByReservationReservationIdentifier("RSV-123")).thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundException.class, () -> reviewService.deleteReview("RSV-123"));

        verify(reviewRepository, times(1)).findByReservationReservationIdentifier("RSV-123");
    }
}
