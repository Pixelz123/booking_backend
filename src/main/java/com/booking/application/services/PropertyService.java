package com.booking.application.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    public PropertyDetailDTO getPropertyDetails(String propertyId) {
        PropertyDetailDTO propertyDetail = new PropertyDetailDTO();
        List<Object[]> queryResultList = property_repo.getPropertyDetails(propertyId);
        Object[] result = queryResultList.get(0);
        List<Object[]> ImageQueryResult = image_repo.getImage(propertyId);
        List<String> imageList = new ArrayList<>();
        propertyDetail.setProperty_id((String) result[0]);
        propertyDetail.setHostname((String) result[1]);
        propertyDetail.setDescription((String) result[2]);
        propertyDetail.setCity((String) result[3]);
        propertyDetail.setState((String) result[4]);
        propertyDetail.setCountry((String) result[5]);
        propertyDetail.setPostal_code((int) result[6]);
        propertyDetail.setAddress((String) result[7]);
        propertyDetail.setPrice_per_night((double) result[8]);
        propertyDetail.setName((String) result[9]);
        propertyDetail.setHero_image_src((String) result[10]);
        propertyDetail.setBeds((int)result[11]);
        propertyDetail.setBathroom((int)result[12]);
        propertyDetail.setGuests((int)result[13]);
        for (Object[] image_src : ImageQueryResult) {
            imageList.add((String) image_src[0]);
        }
        propertyDetail.setImageList(imageList);

        return propertyDetail;
    }

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

    public List<PropertyResponseDTO> getHostProperties() {
        String hostname = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Object[]> queryResult = property_repo.getHostProperties(hostname);
        List<PropertyResponseDTO> hostProperties = queryResult
                .stream()
                .map(
                        row -> {
                            PropertyResponseDTO property = new PropertyResponseDTO();
                            property.setPropertyId((String) row[0]);
                            property.setHostname((String) row[1]);
                            property.setCity((String) row[2]);
                            property.setHeroImageSrc((String) row[3]);
                            property.setPrice_per_night((double) row[4]);
                            property.setName((String) row[5]);

                            return property;
                        })
                .collect(Collectors.toList());
        return hostProperties;

    }
}
