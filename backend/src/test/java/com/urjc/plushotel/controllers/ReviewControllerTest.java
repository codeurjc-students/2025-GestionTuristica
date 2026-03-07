package com.urjc.plushotel.controllers;

import com.urjc.plushotel.config.SecurityConfig;
import com.urjc.plushotel.dtos.response.ReviewDTO;
import com.urjc.plushotel.services.CustomUserDetailsService;
import com.urjc.plushotel.services.JwtService;
import com.urjc.plushotel.services.ReviewService;
import com.urjc.plushotel.utils.EndpointConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
@Import(SecurityConfig.class)
class ReviewControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private ReviewService reviewService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "USER")
    void createReviewTest() throws Exception {

        String reviewRequest = """
                {
                    "message": "Test message",
                    "reservationIdentifier": "RSV-123",
                    "rating": 4.5
                }
                """;

        ReviewDTO reviewDTO = new ReviewDTO("john@example.com", "Room1", "Test message", 4.5, "RSV-123", "2026-03-07");

        when(reviewService.createReview(any(), any())).thenReturn(reviewDTO);

        mockMvc.perform(post("/api/v1" + EndpointConstants.ReviewsEndpoints.REVIEWS_BASE_URL)
                        .content(reviewRequest)
                        .header("Content-Type", "application/json"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/reviews/reservation/RSV-123")))
                .andExpect(jsonPath("$.message").value("Test message"))
                .andExpect(jsonPath("$.reservationIdentifier").value("RSV-123"))
                .andExpect(jsonPath("$.rating").value(4.5));
    }

    @Test
    @WithMockUser
    void getReviewsByRoomTest() throws Exception {

        ReviewDTO review1 = new ReviewDTO("john@example.com", "Room1", "message1", 4.5, "RSV-123",
                "2026-03-07 09:00:00");

        ReviewDTO review2 = new ReviewDTO("mark@example.com", "Room2", "message2", 3.5, "RSV-1234",
                "2026-03-07 10:30:00");

        List<ReviewDTO> reviews = List.of(review1, review2);

        when(reviewService.getReviewsByRoom(1L)).thenReturn(reviews);

        mockMvc.perform(get("/api/v1" + EndpointConstants.ReviewsEndpoints.REVIEWS_ROOM_URL, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].reservationIdentifier").value("RSV-123"))
                .andExpect(jsonPath("$[0].rating").value(4.5))
                .andExpect(jsonPath("$[0].message").value("message1"))
                .andExpect(jsonPath("$[1].reservationIdentifier").value("RSV-1234"))
                .andExpect(jsonPath("$[1].rating").value(3.5))
                .andExpect(jsonPath("$[1].message").value("message2"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getReviewByReviewIdentifierTest() throws Exception {

        ReviewDTO review = new ReviewDTO("john@example.com", "Room1", "message1", 4.5, "RSV-123",
                "2026-03-07 09:00:00");

        when(reviewService.getReviewByReservationIdentifier("RSV-123")).thenReturn(review);

        mockMvc.perform(get("/api/v1" + EndpointConstants.ReviewsEndpoints.REVIEW_RESERVATION_IDENTIFIER_URL, "RSV" +
                        "-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationIdentifier").value("RSV-123"))
                .andExpect(jsonPath("$.rating").value(4.5))
                .andExpect(jsonPath("$.message").value("message1"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateReviewTest() throws Exception {

        String request = """
                {
                    "message": "Updated message",
                    "rating": 4.0
                }
                """;

        ReviewDTO review = new ReviewDTO("john@example.com", "Room1", "Updated message", 4.0, "RSV-123",
                "2026-03-07 09:00:00");

        when(reviewService.updateReview(any(), any())).thenReturn(review);

        mockMvc.perform(patch("/api/v1" + EndpointConstants.ReviewsEndpoints.REVIEW_RESERVATION_IDENTIFIER_URL,
                        "RSV-123")
                        .content(request)
                        .header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Updated message"))
                .andExpect(jsonPath("$.reservationIdentifier").value("RSV-123"))
                .andExpect(jsonPath("$.rating").value(4.0));
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteReviewTest() throws Exception {

        mockMvc.perform(delete("/api/v1" + EndpointConstants.ReviewsEndpoints.REVIEW_RESERVATION_IDENTIFIER_URL,
                        "RSV-123"))
                .andExpect(status().isNoContent());
    }
}
