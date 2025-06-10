package com.Hostel.Exception;




public class HostelUrlMappingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public HostelUrlMappingException(String message) {
        super(message);
    }

    public HostelUrlMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public HostelUrlMappingException(Throwable cause) {
        super(cause);
    }

    public HostelUrlMappingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
