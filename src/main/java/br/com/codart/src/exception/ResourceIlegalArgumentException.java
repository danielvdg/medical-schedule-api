package br.com.codart.src.exception;

public class ResourceIlegalArgumentException extends RuntimeException {

    public ResourceIlegalArgumentException(String message) {
        super(message);
    }

    public ResourceIlegalArgumentException(String message, Throwable cause) {
            super(message, cause);
    }
}
