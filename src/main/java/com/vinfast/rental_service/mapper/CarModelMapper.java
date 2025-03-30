package com.vinfast.rental_service.mapper;

import com.vinfast.rental_service.dtos.request.CarModelCreateRequest;
import com.vinfast.rental_service.model.CarModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarModelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "images", ignore = true)
    CarModel toEntity(CarModelCreateRequest request);

//    @Mapping(target = "images", source = "images", qualifiedByName = "mapImages")
//    CarModelResponse toResponse(CarModel carModel);
//
//    @Named("mapImages")
//    default List<String> mapImages(List<CarImage> images) {
//        return images.stream()
//                .sorted(Comparator.comparingInt(CarImage::getDisplayOrder))
//                .map(CarImage::getImageUrl)
//                .toList();
//    }
}
