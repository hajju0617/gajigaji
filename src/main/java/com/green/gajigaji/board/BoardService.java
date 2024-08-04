package com.green.gajigaji.board;


import com.green.gajigaji.board.model.*;
import com.green.gajigaji.common.exception.CustomException;
import com.green.gajigaji.common.model.CustomFileUtils;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor

public class BoardService {
    private final BoardMapper mapper;
    private final CustomFileUtils customFileUtils;


    public BoardPostRes postBoard(List<MultipartFile> pics, BoardPostReq p) {
        mapper.postBoard(p);
        if(pics == null)
        {return BoardPostRes.builder().boardSeq(p.getBoardSeq()).build();}

        BoardPicPostDto dto = BoardPicPostDto.builder().boardSeq(p.getBoardSeq()).
                fileNames(new ArrayList()).build();
        try {
            String path = String.format("board/%d", p.getBoardSeq());
            customFileUtils.makeFolders(path);
            for(MultipartFile pic : pics){
                String fileName = customFileUtils.makeRandomFileName(pic);
                String target = String.format("%s/%s" , path, fileName);
                customFileUtils.transferTo(pic, target);
                dto.getFileNames().add(fileName);
            }
            mapper.postBoardPics(dto);

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("게시글 등록 오류");
        }

        return BoardPostRes.builder()
                .boardSeq(dto.getBoardSeq())
                .pics(dto.getFileNames())
                .build();
    }

    public int deleteBoard(BoardDeleteReq p) {
        List<String> fileNames = mapper.getFileNamesByBoardSeq(p.getBoardSeq());
        for (String fileName : fileNames) {
            mapper.deleteBoardPics(p.getBoardSeq(), fileName);
        }
        return mapper.deleteBoard(p.getBoardSeq(), p.getBoardMemberSeq(), p.getBoardPartySeq());
    }


    public BoardPatchReq boardPatch(List<MultipartFile> newPics, BoardPatchReq p) {
        int updateCount = mapper.patchBoard(p);
        if (updateCount == 0) {
            throw new RuntimeException("수정된 부분이 없음");
        }

        List<String> existingFileNames = mapper.getFileNamesByBoardSeq(p.getBoardSeq());

        boolean hasNewFiles = newPics != null && !newPics.isEmpty();
        boolean hasDeletedFiles = p.getDeleteFileNames() != null && !p.getDeleteFileNames().isEmpty();

        if (!hasNewFiles && !hasDeletedFiles) {
            return p;
        }

        BoardPicPostDto dto = BoardPicPostDto.builder().boardSeq(p.getBoardSeq()).fileNames(new ArrayList()).build();
        String path = String.format("board/%d", p.getBoardSeq());

        try {
            if (hasDeletedFiles) {
                deleteFiles(p.getDeleteFileNames(), existingFileNames, path, p.getBoardSeq());
            }
            if (hasNewFiles) {
                uploadFiles(newPics, path, dto);
            }
            mapper.postBoardPics(dto);
        } catch (Exception e) {
            log.error("Board 수정 오류: {}", e.getMessage(), e);
            throw new RuntimeException("board 수정 오류", e);
        }

        return p;
    }

    private void deleteFiles(List<String> deleteFileNames, List<String> existingFileNames, String path, long boardSeq) throws IOException {
        for (String fileName : deleteFileNames) {
            if (existingFileNames.contains(fileName)) {
                String target = String.format("%s/%s", path, fileName);
                customFileUtils.deleteFolder(target);
                mapper.deleteBoardPics(boardSeq, fileName);
            }
        }
    }
    private void uploadFiles(List<MultipartFile> newPics, String path, BoardPicPostDto dto) throws Exception {
        for (MultipartFile pic : newPics) {
            String fileName = customFileUtils.makeRandomFileName(pic);
            String target = String.format("%s/%s", path, fileName);
            customFileUtils.transferTo(pic, target);
            dto.getFileNames().add(fileName);
        }
    }

    public BoardGetPage getBoard(BoardGetReq p) {
        try{
            List<BoardGetRes> board = mapper.getBoard(p);
            for(BoardGetRes boards : board) {
                List<String> pics = mapper.getFileNamesByBoardSeq(boards.getBoardSeq());
                boards.setPics(pics);
            }
            long totalElements = mapper.getTotalCount(p.getBoardPartySeq());
            return new BoardGetPage(board,p.getSize(), totalElements);
        }catch (Exception e) {
            throw new RuntimeException("게시글 목록 조회 중 오류 발생", e);
        }
    }

    public BoardGetRes getBoardDetail(long boardSeq) {
        try {
            mapper.incrementBoardHit(boardSeq);
            BoardGetRes board = mapper.getBoardDetail(boardSeq);
            if (board == null) {
                throw new RuntimeException("해당 게시글을 찾을 수 없습니다");
            }
            List<String> pics = mapper.getFileNamesByBoardSeq(boardSeq);
            board.setPics(pics);
            return board;
        } catch (Exception e) {
            throw new RuntimeException("게시글 상세 조회 중 오류 발생", e);
        }
    }
}
