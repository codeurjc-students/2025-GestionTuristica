package com.urjc.plushotel.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String reservationIdentifier;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.ACTIVE;
    @Column(nullable = false)
    private boolean reviewed;
    @Column(nullable = false)
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDate startDate;
    private LocalDate endDate;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Reservation(String reservationIdentifier, Room room, User user, BigDecimal price) {
        this.reservationIdentifier = reservationIdentifier;
        this.room = room;
        this.user = user;
        this.price = price;
    }

    public Reservation(String reservationIdentifier, Room room, User user, BigDecimal price, boolean reviewed, LocalDateTime createdAt, LocalDate startDate, LocalDate endDate) {
        this.reservationIdentifier = reservationIdentifier;
        this.room = room;
        this.user = user;
        this.price = price;
        this.reviewed = reviewed;
        this.createdAt = createdAt;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
