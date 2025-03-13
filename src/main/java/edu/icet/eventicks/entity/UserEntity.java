package edu.icet.eventicks.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username", length = 200, nullable = false, unique = true)
    private String username;

    @Column(name = "email", length = 200, nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", length = 255, nullable = false)
    private String passwordHash;

    @Column(name = "role", length = 50, nullable = false)
    private String role;

    @Column(name = "is_email_verified")
    private Boolean isEmailVerified;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    // Relationships
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    private Set<EventEntity> createdEvents = new HashSet<>();

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private Set<TicketEntity> sellingTickets = new HashSet<>();

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private Set<PaymentEntity> payments = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<BidEntity> bids = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<FraudDetectionEntity> fraudDetections = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        registeredAt = LocalDateTime.now();
        isEmailVerified = false;
    }
}