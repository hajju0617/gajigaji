package com.green.gajigaji.party.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GetPartyLocationRes {
    @Schema(example = "01", description = "지역 시")
    private String cdSub;
    @Schema(example = "01", description = "지역 구")
    private String cdGb;
    @Schema(example = "강남구", description = "지역 이름")
    private String cdGbNm;
}
