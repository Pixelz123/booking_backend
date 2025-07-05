package com.booking.application.entites;

import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "guest")
public class GuestEntity {
    @Id
    public String guestId;
    @PrePersist
    public void generateId() {
        if (guestId == null || guestId.isBlank()) {
            this.guestId = UUID.randomUUID().toString();  // ✅ UUID as string
        }
    }

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public int age;

    @Enumerated(EnumType.STRING)
    private PersonType catagory;

    @PrePersist
    @PreUpdate
    public void preAssignCatagory() {
        this.catagory = PersonType.fromAge(this.age);
    }

    // owning side for booking-guest relation 
    // name of FK is bookingId PK of booking Entity
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "bookingId", nullable = false)
    private BookingEntity booking;

}
