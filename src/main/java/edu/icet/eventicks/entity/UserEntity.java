package edu.icet.eventicks.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "email", length = 200, nullable = false, unique = true)
    private String email;

    @ToString.Exclude
    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "role", length = 50, nullable = false)
    private String role;

    @Column(name = "is_email_verified")
    private Boolean isEmailVerified;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @ToString.Exclude
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<EventEntity> createdEvents = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TicketEntity> sellingTickets = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PaymentEntity> payments = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<BidEntity> bids = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<FraudDetectionEntity> fraudDetections = new HashSet<>();

}