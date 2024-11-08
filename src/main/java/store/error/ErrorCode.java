package store.error;

public enum ErrorCode {

    BLANK_INPUT_MESSAGE("잘못된 입력입니다. 다시 입력해 주세요."),
    NOT_INTEGER("구입 금액은 정수 범위가 아닌 값이나 문자는 입력할 수 없습니다."),
    File_NOT_FOUND("존재하지 않는 파일입니다 : "),
    PRODUCT_PRICE_IS_NOT_POSITIVE_NUMBERS("상품 가격은 0원 초과이어합니다."),
    PRODUCT_QUANTITY_IS_NEGATIVE_NUMBERS("상품 수량은 0개 이상이어야합니다."),
    FILE_VALUE_TYPE_ERROR("파일에 타입 형식에 맞지 않는 값이 저장되어있습니다."),
    INVALID_INPUT_FORMAT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    ;
    private final String message;
    private static final String PREFIX = "[ERROR] ";

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return PREFIX + message;
    }
}
