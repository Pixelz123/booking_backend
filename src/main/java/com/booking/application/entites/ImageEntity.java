package com.booking.application.entites;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name ="property_images")
public class ImageEntity {
    @Id
    public String imageId;
     @PrePersist
    public void generateId() {
        if (imageId == null || imageId.isBlank()) {
            this.imageId = UUID.randomUUID().toString();
        }
    }
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="property_id")
    @JsonIgnore
    public PropertyEntity property; 
    public String image_src;
}
