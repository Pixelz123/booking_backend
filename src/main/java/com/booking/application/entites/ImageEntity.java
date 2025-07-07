package com.booking.application.entites;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="property_id")
    @JsonIgnore
    public PropertyEntity property; 
    public String image_src;
}
