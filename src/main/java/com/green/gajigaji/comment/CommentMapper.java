package com.green.gajigaji.comment;


import com.green.gajigaji.comment.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface CommentMapper {
    long postBoardComment(CommentPostReq p);
    int deleteBoardComment(CommentDeleteReq p);
    int patchBoardComment(CommentPatchReq p);
    List<CommentGetRes> getBoardComment(CommentGetReq data);
    long getTotalCount(long CommentBoardSeq);
}
