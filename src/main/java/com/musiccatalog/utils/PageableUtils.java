package com.musiccatalog.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtils {

    private PageableUtils(){}

    public static Pageable toPageable(int page, int size, String sortBy) {
        Sort sort = Sort.by("nome").ascending();

        if (sortBy != null && !sortBy.isBlank()) {
            String[] orders = sortBy.split(";");
            Sort.Order[] sortOrders = new Sort.Order[orders.length];

            for (int i = 0; i < orders.length; i++) {
                String[] parts = orders[i].split(",");
                String property = parts[0];
                Sort.Direction direction = (parts.length > 1) ? Sort.Direction.fromString(parts[1]) : Sort.Direction.ASC;
                sortOrders[i] = new Sort.Order(direction, property);
            }

            sort = Sort.by(sortOrders);
        }

        return PageRequest.of(page, size, sort);
    }

}
