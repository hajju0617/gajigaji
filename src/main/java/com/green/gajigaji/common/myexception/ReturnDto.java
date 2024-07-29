package com.green.project2nd.common.myexception;

public class ReturnDto extends RuntimeException {
    public ReturnDto() {super();}
    public ReturnDto(String message) {super(message);}
    public ReturnDto(String message, Throwable cause) {super(message, cause);}
    public ReturnDto(Throwable cause) {super(cause);}
}
