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
@EqualsAndHashCode

public class BoardPicPostDto {
    private long boardSeq;
    private List<String> fileNames = new ArrayList();
}
