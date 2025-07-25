package com.example.rentalcarsystem.dto.response.other;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

@Data
public class PagingResponse<T> {
    private List<T> content;
    private boolean last;
    private int  totalPages;
    private long  totalElements;
    private int  size;
    private int  number;

    public PagingResponse(Page<T> page) {
        this.last = page.isLast();
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.size = page.getSize();
        this.number = page.getNumber();
    }


}
