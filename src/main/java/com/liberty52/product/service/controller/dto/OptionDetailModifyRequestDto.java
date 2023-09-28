package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OptionDetailModifyRequestDto {

    @NotBlank
    String name;

    @NotNull
    Integer price;

    @NotNull
    Boolean onSale;

    @NotNull
    Integer stock;

    public static OptionDetailModifyRequestDto create(String name, int price, boolean onSale, Integer stock){
        return new OptionDetailModifyRequestDto(name, price, onSale, stock);
    }

}
