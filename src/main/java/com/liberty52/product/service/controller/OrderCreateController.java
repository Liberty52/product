package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.OrderCreateService;
import com.liberty52.product.service.controller.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "주문", description = "주문 관련 API를 제공합니다")
@RestController
@RequiredArgsConstructor
public class OrderCreateController {
    private final OrderCreateService orderCreateService;

    @Operation(summary = "카드 결제 주문 생성", description = "사용자가 카드 결제 주문을 생성하는 엔드포인트입니다.")
    @PostMapping("/orders/card")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentCardResponseDto createCardPaymentOrders(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authId,
            @RequestPart("dto") @Validated OrderCreateRequestDto dto,
            @RequestPart("imageFile") MultipartFile imageFile
    ) {
        return orderCreateService.createCardPaymentOrders(authId, dto, imageFile);
    }

    @Operation(summary = "카드 결제 주문 최종 승인 확인", description = "사용자가 카드 결제 주문의 최종 승인을 확인하는 엔드포인트입니다.")
    @GetMapping("/orders/card/{orderId}/confirm")
    @ResponseStatus(HttpStatus.OK)
    public PaymentConfirmResponseDto confirmFinalApprovalOfCardPayment(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authId,
            @PathVariable("orderId") String orderId
    ) {
        return orderCreateService.confirmFinalApprovalOfCardPayment(authId, orderId);
    }

    @Operation(summary = "가상계좌 결제 주문 생성", description = "사용자가 가상계좌 결제 주문을 생성하는 엔드포인트입니다.")
    @PostMapping("/orders/vbank")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentVBankResponseDto createVBankPaymentOrders(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authId,
            @RequestPart("dto") @Validated OrderCreateRequestDto dto,
            @RequestPart("imageFile") MultipartFile imageFile
    ) {
        return orderCreateService.createVBankPaymentOrders(authId, dto, imageFile);
    }

    @Operation(summary = "카트를 이용한 카드 결제 주문 생성", description = "사용자가 카트를 이용하여 카드 결제 주문을 생성하는 엔드포인트입니다.")
    @PostMapping("/orders/card/carts")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentCardResponseDto createCardPaymentOrdersByCarts(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authId,
            @RequestBody @Validated OrderCreateRequestDto dto
    ) {
        return orderCreateService.createCardPaymentOrdersByCarts(authId, dto);
    }

    @Operation(summary = "카트를 이용한 가상 계좌 결제 주문 생성", description = "사용자가 카트를 이용하여 가상 계좌 결제 주문을 생성하는 엔드포인트입니다.")
    @PostMapping("/orders/vbank/carts")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentVBankResponseDto createVBankPaymentOrdersByCarts(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authId,
            @RequestBody @Validated OrderCreateRequestDto dto
    ) {
        return orderCreateService.createVBankPaymentOrdersByCarts(authId, dto);
    }

}
