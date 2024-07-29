package com.green.gajigaji.common.myexception;

//선택적 예외처리 하기위해 RE를 사용함. (ex.프로그래밍 오류)
public class WrongValue extends RuntimeException {
    // private static final long serialVersionUID = 1L;
    //직렬화된 객체의 클래스 버전을 식별하는 고유 ID임. 딱히 지금은 필요없음. 클래스 구조 변경 시 호환성을 유지하는 데 사용함 (역직렬화 오류 방지용)

    public WrongValue() {super();}
    public WrongValue(String message) {super(message);}
    public WrongValue(String message, Throwable cause) {super(message, cause);}
    public WrongValue(Throwable cause) {super(cause);}
}