package com.green.gajigaji.comment.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class CommentGetPage {
    private List<CommentGetRes> list;
    private long totalPage;
    private long totalElements;
    public CommentGetPage(List<CommentGetRes> list, Integer size, long totalElements) {
       if(size != 0){
           this.totalPage = (this.totalElements + size - 1) / size;} else
           {
               this.totalPage = 1;
           }
        this.list = list;
        this.totalElements = totalElements;
    }
}
