package com.winsum.chatliu.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageResultDTO<T> {
    private Long total;
    private Integer totalPage;
    private Integer pageNum;
    private List<T> items;


    public PageResultDTO() {
    }

    public PageResultDTO(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public PageResultDTO(Long total, Integer totalPage, Integer pageNum, List<T> items) {
        this.total = total;
        this.totalPage = totalPage;
        this.pageNum = pageNum;
        this.items = items;
    }
}
