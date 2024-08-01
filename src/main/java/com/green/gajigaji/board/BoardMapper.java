package com.green.gajigaji.board;

import com.green.gajigaji.board.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {
    int postBoard(BoardPostReq p);
    int postBoardPics(BoardPicPostDto p);
    int deleteBoardPics(long boardSeq,String fileName);

    int deleteBoard(long boardSeq, long boardMemberSeq, long boardPartySeq);
    int patchBoard(BoardPatchReq p);
    long getTotalCount();
    List<String> getFileNamesByBoardSeq(long boardSeq);

    int incrementBoardHit(long boardSeq);
    BoardGetRes getBoard(long boardPartySeq);
    List<BoardGetRes> getBoardDetail(long boardSeq);
}

