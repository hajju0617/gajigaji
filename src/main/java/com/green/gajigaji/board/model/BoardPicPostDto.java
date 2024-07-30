package com.green.gajigaji.board.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder

public class BoardPicPostDto {
    private long boardSeq;
    @Builder.Default
    private List<String> fileNames = new ArrayList();
}
