package com.liberty52.product.global.exception.external;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ProductErrorCode implements ErrorCode{
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND),
    FORBIDDEN(HttpStatus.FORBIDDEN),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND),
    OPTION_NOT_FOUND(HttpStatus.NOT_FOUND),
    OPTION_DETAIL_NOT_FOUND(HttpStatus.NOT_FOUND),
    NOT_FOUND_CART_ITEM(HttpStatus.NOT_FOUND),

    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "구입할 상품의 수량이 0이하일 수 없습니다."),

    ALREADY_COMPLETED_ORDER(HttpStatus.BAD_REQUEST, "이미 완료된 거래입니다."),

    // 만약 전에 주문한 제품을 다시 주문하도록 한다면 삭제
    CART_ADD_INVALID_ITEM(HttpStatus.BAD_REQUEST, "해당 상품은 장바구니에 담길 수 없습니다. (사유 : 이미 주문된 제품)")

    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    private final String errorCode = "A-" + "0".repeat(Math.max(4-String.valueOf(this.ordinal() + 1).length(), 0)) + (this.ordinal() + 1);

    ProductErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    ProductErrorCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.errorMessage = "";
    }

    public String getErrorName() {
        return this.name();
    }
}