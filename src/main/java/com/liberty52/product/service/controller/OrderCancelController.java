package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.OrderCancelService;
import com.liberty52.product.service.controller.dto.OrderCancelDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderCancelController {

    private final OrderCancelService orderCancelService;

    @PostMapping("/orders/cancel")
    @ResponseStatus(HttpStatus.OK)
    public OrderCancelDto.Response cancelOrder(@RequestHeader(HttpHeaders.AUTHORIZATION) String authId,
                                               @RequestBody @Validated OrderCancelDto.Request request) {
        return orderCancelService.cancelOrder(authId, request);
    }

}
