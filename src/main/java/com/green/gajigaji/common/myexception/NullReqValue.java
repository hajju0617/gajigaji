package com.green.project2nd.common.myexception;

public class NullReqValue extends RuntimeException{
    public NullReqValue() {super();}
    public NullReqValue(String message) {super(message);}
    public NullReqValue(String message, Throwable cause) {super(message, cause);}
    public NullReqValue(Throwable cause) {super(cause);}
}
