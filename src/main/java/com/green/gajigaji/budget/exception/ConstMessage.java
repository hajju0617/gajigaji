package com.green.gajigaji.budget.exception;

public interface ConstMessage {
     String POST_SUCCESS_MESSAGE = "등록이 정상적으로 완료되었습니다.";
     String PATCH_SUCCESS_MESSAGE = "수정이 정상적으로 완료되었습니다.";
     String GET_SUCCESS_MESSAGE = "조회가 정상적으로 완료되었습니다.";
     String DELETE_SUCCESS_MESSAGE = "삭제가 정상적으로 완료되었습니다.";
     String NULL_ERROR_MESSAGE = "입력되지 않은 값이 있습니다.";
     String NOT_FOUND_PLAN = "해당 일정이 존재하지 않습니다.";
     String NOT_FOUND_PARTY = "해당 모임이 존재하지 않습니다.";
     String NOT_FOUND_MEMBER = "해당 모임 멤버가 존재하지 않습니다.";
     String NOT_FOUND_BUDGET = "해당 회계 내역이 존재하지 않습니다.";
     String PIC_SAVE_ERROR = "파일 저장 중 오류가 발생하였습니다.";
     String[] monthList = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
}
