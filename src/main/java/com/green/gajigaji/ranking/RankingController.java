package com.green.gajigaji.ranking;

import com.green.gajigaji.common.model.ResultDto;
import com.green.gajigaji.ranking.model.GetRankingRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.green.gajigaji.common.GlobalConst.SUCCESS;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/ranking")
@Tag(name = "Ranking(모임 랭킹)", description = "Get Only")
public class RankingController {
    private final RankingService service;

    @GetMapping("/total")
    @Operation(summary = "통합 랭킹 조회", description =
            "<strong> 전체 기간 통합 랭킹 조회 </strong><p></p>" +
                    "<p><strong> limit </strong> : 조회하고 싶은 행 수 (int) (디폴트값 : 3)</p>")
    @ApiResponse(
            description =
                    "<p> ResponseCode 응답 코드 </p>" +
                            "<p> 1 : 성공 (List 형식으로 리턴)</p>" +
                            "<p> 2 : 실패, ResultMsg</p>")
    public ResultDto<List<GetRankingRes>> getRankingTotal(@RequestParam(defaultValue = "3") int limit) {
        List<GetRankingRes> list = service.getRankingTotal(limit);
        return ResultDto.resultDto(HttpStatus.OK, SUCCESS, "통합 랭킹 조회 완료", list);
    }

    @GetMapping("/location")
    @Operation(summary = "지역 랭킹 조회", description =
            "<strong> 전체 기간 지역 랭킹 조회 </strong><p></p>" +
                    "<p><strong> partyLocation </strong> : 지역 코드 (int) (디폴트값 : 1)</p>" +
                    "<p><strong> limit </strong> : 조회하고 싶은 행 수 (int) (디폴트값 : 3)</p>")
    @ApiResponse(
            description =
                    "<p> ResponseCode 응답 코드 </p>" +
                            "<p> 1 : 성공 (List 형식으로 리턴)</p>" +
                            "<p> 2 : 실패, ResultMsg</p>")
    public ResultDto<List<GetRankingRes>> getRankingLocation(@RequestParam(defaultValue = "1") int partyLocation
            , @RequestParam(defaultValue = "3") int limit) {
        List<GetRankingRes> list = service.getRankingLocation(partyLocation, limit);
        return ResultDto.resultDto(HttpStatus.OK, SUCCESS, "지역 랭킹 조회 완료", list);
    }

    @GetMapping("/category")
    @Operation(summary = "카테고리 랭킹 조회", description =
            "<strong> 전체 기간 카테고리 랭킹 조회 </strong><p></p>" +
                    "<p><strong> partyGenre </strong> : 카테고리 코드 (int) (디폴트값 : 1)</p>" +
                    "<p> 1:스포츠 2:게임 3:맛집 4:패션 5:자기계발 6:문화예술 7:Bar 8:기타</p>" +
                    "<p><strong> limit </strong> : 조회하고 싶은 행 수 (int) (디폴트값 : 3)</p>")
    @ApiResponse(
            description =
                    "<p> ResponseCode 응답 코드 </p>" +
                            "<p> 1 : 성공 (List 형식으로 리턴)</p>" +
                            "<p> 2 : 실패, ResultMsg</p>")
    public ResultDto<List<GetRankingRes>> getRankingGenre(@RequestParam(defaultValue = "1") int partyGenre
            , @RequestParam(defaultValue = "3") int limit) {
        List<GetRankingRes> list = service.getRankingGenre(partyGenre, limit);
        return ResultDto.resultDto(HttpStatus.OK, SUCCESS, "카테고리 랭킹 조회 완료", list);
    }

    @GetMapping("/thisMonth")
    @Operation(summary = "이번 달 랭킹 조회", description =
            "<strong> 이번 달 랭킹 조회 (지역, 카테고리 별 조회 가능)</strong><p></p>" +
                    "<p><strong> partyLocation </strong> : 지역 코드 (int) (디폴트값 : 0)</p>" +
                    "<p><strong> partyGenre </strong> : 카테고리 코드 (int) (디폴트값 : 0)</p>" +
                    "<p> 1:스포츠 2:게임 3:맛집 4:패션 5:자기계발 6:문화예술 7:Bar 8:기타</p>" +
                    "<p><strong> limit </strong> : 조회하고 싶은 행 수 (int) (디폴트값 : 3)</p>")
    @ApiResponse(
            description =
                    "<p> ResponseCode 응답 코드 </p>" +
                            "<p> 1 : 성공 (List 형식으로 리턴)</p>" +
                            "<p> 2 : 실패, ResultMsg</p>")
    public ResultDto<List<GetRankingRes>> getRankingThisMonth(
            @RequestParam(defaultValue = "0", required = false) Integer partyLocation
          , @RequestParam(defaultValue = "0", required = false) Integer partyGenre
          , @RequestParam(defaultValue = "3") int limit) {
        List<GetRankingRes> list = service.getRankingThisMonth(
                  partyLocation == null ? 0 : partyLocation
                , partyGenre    == null ? 0 : partyGenre
                , limit);
        return ResultDto.resultDto(HttpStatus.OK, SUCCESS, "이번 달 랭킹 조회 완료", list);
    }

    @GetMapping("/lastMonth")
    @Operation(summary = "지난 달 랭킹 조회", description =
            "<strong> 지난 달 랭킹 조회 (지역, 카테고리 별 조회 가능)</strong><p></p>" +
                    "<p><strong> partyLocation </strong> : 지역 코드 (int) (디폴트값 : 0)</p>" +
                    "<p><strong> partyGenre </strong> : 카테고리 코드 (int) (디폴트값 : 0)</p>" +
                    "<p> 1:스포츠 2:게임 3:맛집 4:패션 5:자기계발 6:문화예술 7:Bar 8:기타</p>" +
                    "<p><strong> limit </strong> : 조회하고 싶은 행 수 (int) (디폴트값 : 3)</p>")
    @ApiResponse(
            description =
                    "<p> ResponseCode 응답 코드 </p>" +
                            "<p> 1 : 성공 (List 형식으로 리턴)</p>" +
                            "<p> 2 : 실패, ResultMsg</p>")
    public ResultDto<List<GetRankingRes>> getRankingLastMonth(
            @RequestParam(defaultValue = "0", required = false) Integer partyLocation
            , @RequestParam(defaultValue = "0", required = false) Integer partyGenre
            , @RequestParam(defaultValue = "3") int limit) {
        List<GetRankingRes> list = service.getRankingLastMonth(
                  partyLocation == null ? 0 : partyLocation
                , partyGenre    == null ? 0 : partyGenre
                , limit);
        return ResultDto.resultDto(HttpStatus.OK, SUCCESS, "지난 달 랭킹 조회 완료", list);
    }
}
