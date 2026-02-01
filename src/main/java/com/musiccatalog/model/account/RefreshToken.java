package com.musiccatalog.model.account;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String token;


    @Column(name = "expiration_date", nullable = false)
    private Instant expirationDate;

    @Column(nullable = false)
    private boolean revoked = false;

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}

    public String getToken() {return token;}

    public void setToken(String token) {this.token = token;}

    public Instant getExpirationDate() {return expirationDate;}

    public void setExpirationDate(Instant expirationDate) {this.expirationDate = expirationDate;}

    public boolean getRevoked() {return revoked;}

    public void setRevoked(boolean revoked) {this.revoked = revoked;}

    public boolean expired(){
        return Instant.now().isAfter(this.expirationDate);
    }
}
