package com.mjc.school.service.exceptions;

public enum ExceptionErrorCodes {
    NEWS_DOES_NOT_EXIST(Constants.ERROR_CODE_000001, "News with id %d does not exist"),
    AUTHOR_DOES_NOT_EXIST(Constants.ERROR_CODE_000002, "Author id does not exist. Author id is: %s"),
    TAG_DOES_NOT_EXIST(Constants.ERROR_CODE_000003, "Tag with id %d does not exist"),
    COMMENT_DOES_NOT_EXIST(Constants.ERROR_CODE_000004, "Comment with id %d does not exist"),
    VALIDATION_EXCEPTION(Constants.ERROR_CODE_000005, "Validation failed %s");

    private final String errorCode;
    private final String errorMessage;

    ExceptionErrorCodes(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    private static class Constants {
        public static final String ERROR_CODE_000001 = "000001";
        public static final String ERROR_CODE_000002 = "000002";
        public static final String ERROR_CODE_000003 = "000003";
        public static final String ERROR_CODE_000004 = "000004";
        public static final String ERROR_CODE_000005 = "000005";
    }
}
