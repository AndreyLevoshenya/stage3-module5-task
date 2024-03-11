package com.mjc.school.service.dto;

import java.util.List;
import java.util.Objects;

public class PageDtoResponse<T> {
    private List<T> entityDtoList;
    private int pageNumber;
    private long pageCount;

    public PageDtoResponse() {
    }

    public PageDtoResponse(List<T> entityDtoList, int pageNumber, long pageCount) {
        this.entityDtoList = entityDtoList;
        this.pageNumber = pageNumber;
        this.pageCount = pageCount;
    }

    public List<T> getEntityDtoList() {
        return entityDtoList;
    }

    public void setEntityDtoList(List<T> entityDtoList) {
        this.entityDtoList = entityDtoList;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    @Override
    public String toString() {
        return "PageDtoResponse{" +
                "entityDtoList=" + entityDtoList +
                ", pageNumber=" + pageNumber +
                ", pageCount=" + pageCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageDtoResponse<?> that = (PageDtoResponse<?>) o;
        return pageNumber == that.pageNumber && pageCount == that.pageCount && Objects.equals(entityDtoList, that.entityDtoList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityDtoList, pageNumber, pageCount);
    }
}
