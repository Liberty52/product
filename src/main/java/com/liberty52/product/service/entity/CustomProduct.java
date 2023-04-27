package com.liberty52.product.service.entity;

import com.liberty52.product.global.exception.external.badrequest.CartAddInvalidItemException;
import com.liberty52.product.global.exception.internal.InvalidQuantityException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.*;

import static org.springframework.util.ObjectUtils.isEmpty;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomProduct {
    @Id
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String userCustomPictureUrl;
    @Column(nullable = false)
    private String modelingPictureUrl;
    // 작동 과정을 몰라서 nullable은 안넣음.
    private String thumbnailPictureUrl;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, updatable = false)
    private String authId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "customProduct")
    private List<CustomProductOption> options = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders orders;

    public Map<String, String> getOptionsMap() {
        Map<String, String> optionsMap = new HashMap<>();
        for (CustomProductOption option : this.options) {
            String optTypeName = option.getOptionDetail().getProductOption().getName();
            String optName = option.getOptionDetail().getName();
            optionsMap.put(optTypeName, optName);
        }
        return optionsMap;
    }

    public static CustomProduct createCartItem(String authId, int quantity, String image) {
        return new CustomProduct(image, quantity, authId);
    }

    @Builder
    private CustomProduct(String userCustomPictureUrl, int quantity, String authId) {
        this.userCustomPictureUrl = this.modelingPictureUrl = this.thumbnailPictureUrl = userCustomPictureUrl;
        this.quantity = quantity;
        this.authId = authId;
    }

    public static CustomProduct create(String userCustomPictureUrl, int quantity, String authId) {
        return builder().userCustomPictureUrl(userCustomPictureUrl).quantity(quantity).authId(authId).build();
    }

    public void associateWithProduct(Product product) {
        this.product = product;
    }

    public void associateWithCart(@NonNull Cart cart){
        verifyItemCanAddCart();
        Objects.requireNonNull(cart);
        this.cart = cart;
        cart.addCustomProduct(this);
        removedFromOrder();
    }

    /**
     * Orders만 이 메스드를 호출한다.
     * Visibility: package
     */
    public void associateWithOrder(@NonNull Orders orders){
        Objects.requireNonNull(orders);
        verifyQuantity();
        this.orders = orders;
        orders.addCustomProduct(this);
        removedFromCart();
    }

    public void addOption(CustomProductOption customProductOption) {
        this.options.add(customProductOption);
    }

    private void verifyItemCanAddCart() {
        if(isEmpty(this.orders))  return;
        throw new CartAddInvalidItemException();
    }

    private void verifyQuantity(){
        if( quantity <= 0)
            throw new InvalidQuantityException();
    }

    private void removedFromCart() {
        this.cart = null;
    }

    private void removedFromOrder() {
        this.orders = null;
    }

    public boolean isInCart() {
        return (this.cart != null) && (this.orders == null);
    }

    public boolean isInOrder() {
        return (this.cart == null) && (this.orders != null);
    }

    public void modifyCustomPictureUrl(String customPictureUrl) {
        this.userCustomPictureUrl = customPictureUrl;
    }

    public void modifyQuantity(int quantity){
        this.quantity = quantity;
    }
}
