package com.booking.application.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.booking.application.dto.PropertyDetailDTO;
import com.booking.application.dto.PropertyRequestDTO;
import com.booking.application.dto.PropertyResponseDTO;
import com.booking.application.entites.ImageEntity;
import com.booking.application.entites.PropertyEntity;
import com.booking.application.entites.UserEntity;
import com.booking.application.repositories.ImageRepository;
import com.booking.application.repositories.PropertyRepository;


@Service
public class PropertyService {
    @Autowired
    PropertyRepository property_repo;
    @Autowired
    ImageRepository image_repo;
    @Autowired
    UserService user_service;


    @Transactional(readOnly = true)
    public List<PropertyResponseDTO> getProperties(PropertyRequestDTO propertyRequest) {
        List<PropertyResponseDTO> propertyList = new ArrayList<>();
        List<Object[]> queryOutput = property_repo.getProperties(propertyRequest.getLocationQueryString(),
                propertyRequest.getCheakIn(),
                propertyRequest.getCheakOut());
        for (Object[] row : queryOutput) {
            String property_id = (String) row[0];
            String hostname = (String) row[1];
            String city = (String) row[2];
            String hero_image_src = (String) row[3];
            double price_per_night = (double) row[4];
            String name = (String) row[5];
            propertyList.add(new PropertyResponseDTO(property_id,
                    hostname,
                    city,
                    hero_image_src,
                    price_per_night,
                    name));

        }
        return propertyList;
    }

    @Cacheable(value = "propertyDetails", key = "#propertyId")
    @Transactional(readOnly = true)
    public PropertyDetailDTO getPropertyDetails(String propertyId) {
        PropertyDetailDTO propertyDetail = property_repo.getPropertyDetails(propertyId);
        List<String> imageList = image_repo.getImage(propertyId);
        propertyDetail.setImageList(imageList);
        return propertyDetail;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void hostNewProperty(PropertyDetailDTO new_property) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<String> images = new_property.getImageList();
        PropertyEntity newProperty = new PropertyEntity();
        newProperty.setName(new_property.getName());
        newProperty.setCountry(new_property.getCountry());
        newProperty.setState(new_property.getState());
        newProperty.setAddress(new_property.getAddress());
        newProperty.setDescription(new_property.getDescription());
        newProperty.setPostal_code(new_property.getPostal_code());
        newProperty.setHeroImageSrc(new_property.getHero_image_src());
        newProperty.setPrice_per_night(new_property.getPrice_per_night());
        UserEntity user = user_service.loadUserByUsername(username);
        newProperty.setUser(user);
        List<ImageEntity> imageList = images.stream()
                .map(imageSrc -> {
                    ImageEntity imageEntity = new ImageEntity();
                    imageEntity.setImage_src(imageSrc);
                    imageEntity.setProperty(newProperty);
                    return imageEntity;
                })
                .collect(Collectors.toList());
        newProperty.setImage(imageList);
        property_repo.save(newProperty);
    }
    @Transactional(readOnly = true)
    public List<PropertyResponseDTO> getHostProperties() {
        String hostname = SecurityContextHolder.getContext().getAuthentication().getName();
        List<PropertyResponseDTO> hostProperties = property_repo.getHostProperties(hostname);
        return hostProperties;

    }
}
