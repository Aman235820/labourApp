package com.example.labourApp.Models;

import com.example.labourApp.EnumClass;

public class PaginationRequestDTO {
    private Integer pageNumber = EnumClass.PAGE_NUMBER;
    private Integer pageSize = EnumClass.PAGE_SIZE;
    private String sortBy = EnumClass.SORT_BY;
    private String sortOrder = EnumClass.SORT_ORDER;

    public PaginationRequestDTO() {
    }

    public PaginationRequestDTO(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
