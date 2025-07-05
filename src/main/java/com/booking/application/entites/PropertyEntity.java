package com.booking.application.entites;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    public String propertyId;
    @PrePersist
    public void generateId() {
        if (propertyId == null || propertyId.isBlank()) {
            this.propertyId = UUID.randomUUID().toString();  
        }
    }
    
    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIgnore
    public UserEntity user; 

    @Column(nullable=false)
    public String name;

    @Column(nullable=false)
    public String description;

    @Column
    public double price_per_night;
}
