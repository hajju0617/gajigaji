package com.green.gajigaji.common.myexception;

public class NullValueInDatabase extends RuntimeException {
    public NullValueInDatabase() {super();}
    public NullValueInDatabase(String message) {super(message);}
    public NullValueInDatabase(String message, Throwable cause) {super(message, cause);}
    public NullValueInDatabase(Throwable cause) {super(cause);}
}
