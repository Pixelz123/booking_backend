package com.booking.application.entites;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "properties")
public class PropertyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String propertyId;
    @PrePersist
    public void generateId() {
        if (propertyId == null || propertyId.isBlank()) {
            this.propertyId = UUID.randomUUID().toString();  // ✅ UUID as string
        }
    }
    
    @ManyToOne
    @JoinColumn(name="hostId")
    public UserEntity user; 

    @Column(nullable=false)
    public String name;

    @Column(nullable=false)
    public String description;

    @Column
    public double price_per_night;
}
