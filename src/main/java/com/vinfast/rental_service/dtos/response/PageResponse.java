package com.vinfast.rental_service.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class PageResponse<T> implements Serializable {
    private long page;
    private long size;
    private long totalPage;
    private T items;
}
