package com.urjc.plushotel.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Check(constraints = "rating >= 1 AND rating <= 5")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_id", unique = true, nullable = false)
    private Reservation reservation;
    @Column(length = 1000, nullable = false)
    private String message;
    @Column(nullable = false)
    private Double rating;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Review(User user, Reservation reservation, String message, Double rating) {
        this.user = user;
        this.reservation = reservation;
        this.message = message;
        this.rating = rating;
    }

    public Review(User user, Reservation reservation, String message, Double rating, LocalDateTime createdAt) {
        this.user = user;
        this.reservation = reservation;
        this.message = message;
        this.rating = rating;
        this.createdAt = createdAt;
    }
}
