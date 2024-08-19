package com.express.user.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "ticket"
)
public class
Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticket_number")
    private String ticketNumber;

    @Column(name = "male_count")
    private int maleCount;

    @Column(name="female_count")
    private int femaleCount;

    @Column(name="to_where") // COL->JAF  JAF->COL
    private String toWhere;

    @ManyToOne
    @JoinColumn(name = "pickup_point_id")
    private Address pickupPoint;

    @ManyToOne
    @JoinColumn(name = "drop_point_id")
    private Address dropPoint;

    @Column(name="date")
    private LocalDate date;

    @Column(name="status")  // CONFIRMED, CANCELLED, PENDING, EXPIRED ,DELETED
    private String status;

    @Column(name="msg")
    private String msg;

    @Column(name="description")
    private String description;

    @Column(name="created_at")
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "issuer_id")
    private User issuer;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
    }


}
