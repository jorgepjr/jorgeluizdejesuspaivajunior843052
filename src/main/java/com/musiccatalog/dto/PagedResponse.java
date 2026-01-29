package com.musiccatalog.dto;

import java.util.List;

public class PagedResponse<T> extends Response<List<T>>{
    private long totalElements;
    private int totalPages;

    public PagedResponse(List<T> data, long totalElements, int totalPages) {
        super(data);
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
