package com.green.gajigaji.board;

import com.green.gajigaji.board.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {
    int postBoard(BoardPostReq p);
    int postBoardPics(BoardPicPostDto p);
    int deleteBoardPics(@Param("boardSeq") long boardSeq, @Param("fileName") String fileName);
    int deleteBoard(@Param("boardSeq") long boardSeq, @Param("boardMemberSeq") long boardMemberSeq, @Param("boardPartySeq") long boardPartySeq);
    int patchBoard(BoardPatchReq p);
    List<BoardGetRes> getBoardDetail(BoardGetReq data);
    long getTotalCount();
    List<String> getFileNamesByBoardSeq(long boardSeq);
    int incrementBoardHit(@Param("boardSeq") long boardSeq , @Param("boardPartySeq") long boardPartySeq, @Param("boardMemberSeq") long boardMemberSeq);
    BoardGetRes getBoard(@Param("boardSeq") long boardSeq , @Param("boardPartySeq") long boardPartySeq, @Param("boardMemberSeq") long boardMemberSeq);
}

