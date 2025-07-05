package com.booking.application.entites;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    public String userId;
    @PrePersist
    public void generateId() {
        if (userId == null || userId.isBlank()) {
            this.userId = UUID.randomUUID().toString();  
        }
    }

    @Column(nullable = false)
    public String username;

    @Column(nullable = false)
    public String password;

    @OneToMany(mappedBy="user")
    private List<PropertyEntity> properties;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "roles")
    public Set<Role> roles;

    @OneToMany(mappedBy = "user")
    // non owning side of booking user relation
    private List<BookingEntity> booking;

}
