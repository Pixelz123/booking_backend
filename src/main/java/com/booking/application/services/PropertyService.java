package com.booking.application.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.booking.application.dto.PropertyDetailDTO;
import com.booking.application.dto.PropertyRequestDTO;
import com.booking.application.dto.PropertyResponseDTO;
import com.booking.application.repositories.ImageRepository;
import com.booking.application.repositories.PropertyRepository;

@Service
public class PropertyService {
    @Autowired
    PropertyRepository property_repo;
    @Autowired
    ImageRepository image_repo;

    public List<PropertyResponseDTO> getProperties(PropertyRequestDTO propertyRequest) {
        List<PropertyResponseDTO> propertyList = new ArrayList<>();
        List<Object[]> queryOutput = property_repo.getProperties(propertyRequest.getLocationQueryString(),
                propertyRequest.getCheakIn(),
                propertyRequest.getCheakOut(),
                propertyRequest.getMinPrice(),
                propertyRequest.getMaxPrice());
        for (Object[] row : queryOutput) {
            String property_id = (String) row[0];
            String hostname = (String) row[1];
            String city = (String) row[2];
            String hero_image_src = (String) row[3];
            double price_per_night = (double) row[4];
            propertyList.add(new PropertyResponseDTO(property_id,
                    hostname,
                    city,
                    hero_image_src,
                    price_per_night));

        }
        return propertyList;
    }

    public PropertyDetailDTO getPropertyDetails(String propertyId) {
        PropertyDetailDTO propertyDetail = new PropertyDetailDTO();
        List<Object[]> queryResultList = property_repo.getPropertyDetails(propertyId);
        Object[] result = queryResultList.get(0);
        List<Object[]> ImageQueryResult = image_repo.getImage((String) result[0]);
        List<String> imageList = new ArrayList<>();
        propertyDetail.setProperty_id((String) result[0]);
        propertyDetail.setUsername((String) result[1]);
        propertyDetail.setUser_id((String) result[2]);
        propertyDetail.setDescription((String) result[3]);
        propertyDetail.setCity((String) result[4]);
        propertyDetail.setState((String) result[5]);
        propertyDetail.setCountry((String) result[6]);
        propertyDetail.setPostal_code((int) result[7]);
        propertyDetail.setAddress((String) result[8]);
        propertyDetail.setPrice_per_night((double) result[9]);
        for (Object image_src : ImageQueryResult) {
            imageList.add((String) image_src);
        }
        propertyDetail.setImageList(imageList);

        return propertyDetail;

    }
}
