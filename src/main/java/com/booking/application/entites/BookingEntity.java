package com.booking.application.entites;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class BookingEntity {
    @Id
    public String bookingId;
    @PrePersist
    public void generateId() {
        if (bookingId == null || bookingId.isBlank()) {
            this.bookingId = UUID.randomUUID().toString(); 
        }
    }
    // (host)
    @ManyToOne(fetch = FetchType.EAGER) // this is the owning side of booking-user relation
    @JsonIgnore
    @JoinColumn(name = "user_id") // name of the FK PK of user
    private UserEntity user;

    @ManyToOne(fetch =FetchType.EAGER)
    @JoinColumn(name = "property_id") // owning side of Booking-property relation and the name of FK
    private PropertyEntity property;

    // in the guest class we try to map this to "booking" member
    @OneToMany(mappedBy = "booking",cascade=CascadeType.ALL) // non owning side of Booking-guest relation
    public List<GuestEntity> guests; // following the OCP priniple

    public Date cheakIn;
    public Date cheakOut;

    @Enumerated(EnumType.STRING)
    public BookingStatus status;
}
