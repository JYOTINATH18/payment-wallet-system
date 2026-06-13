package com.jyotinath.wallet.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaginationResponse<T> {

    private int pageNumber;
    private int pageSize;
    private long totalRecords;
    private int totalPages;
    private boolean hasNextPage;
    private List<T> records;
    public PaginationResponse(
            int pageNumber,
            int pageSize,
            long totalRecords,
            int totalPages,
            boolean hasNextPage, List<T> records) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalRecords = totalRecords;
        this.totalPages = totalPages;
        this.hasNextPage = hasNextPage;
        this.records = records;
    }
}
