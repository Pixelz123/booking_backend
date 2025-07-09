package com.booking.application.entites;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    public UserEntity user;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public String description;

    @Column(nullable = true)
    public String city;

    @Column(nullable = true)
    public String state;

    @Column(nullable = true)
    public String country;

    @Column(nullable = true)
    public int postal_code;

    @Column(nullable = true)
    public String address;

    @Column(nullable = true)
    public String heroImageSrc;

    @Column(nullable = true)
    public int guests;
    @Column(nullable = true)
    public int bedroom;
    @Column(nullable = true)
    public int bathroom;
    @Column(nullable = true)
    public int beds;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    public List<ImageEntity> image;

    @Column
    public double price_per_night;
}
