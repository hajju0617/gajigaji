package com.green.gajigaji.common.myexception;

public class MsgExceptionNull extends RuntimeException {
    public MsgExceptionNull() {super();}
    public MsgExceptionNull(String message) {super(message);}
    public MsgExceptionNull(String message, Throwable cause) {super(message, cause);}
    public MsgExceptionNull(Throwable cause) {super(cause);}
}
