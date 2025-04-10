package com.vinfast.rental_service.mapper;

import com.vinfast.rental_service.dtos.request.CarModelCreateRequest;
import com.vinfast.rental_service.dtos.request.CarModelUpdateRequest;
import com.vinfast.rental_service.dtos.response.CarModelDetailResponse;
import com.vinfast.rental_service.model.CarImage;
import com.vinfast.rental_service.model.CarModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CarModelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "images", ignore = true)
    CarModel toEntity(CarModelCreateRequest request);

    void updateCarModel(CarModelUpdateRequest request, @MappingTarget CarModel carModel);

    @Mapping(source = "images", target = "imageUrls", qualifiedByName = "mapImagesToUrls")
    CarModelDetailResponse toDTO(CarModel request);

    @Named("mapImagesToUrls")
    default List<String> mapImagesToUrls(List<CarImage> images){
        return images != null ? images.stream().map(CarImage::getImageUrl).collect(Collectors.toList()) : null;
    }
}
