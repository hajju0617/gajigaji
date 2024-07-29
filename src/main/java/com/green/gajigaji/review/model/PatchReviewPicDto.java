package com.green.project2nd.review.model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class PatchReviewPicDto {
    @Builder.Default
    private List<String> fileNames = new ArrayList();
}
