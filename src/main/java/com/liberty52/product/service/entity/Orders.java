package com.liberty52.product.service.entity;

import com.liberty52.product.global.contants.PriceConstants;
import com.liberty52.product.global.exception.external.AlreadyCompletedOrderException;
import com.liberty52.product.service.entity.payment.Payment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Orders {

    @Id
    private String id = UUID.randomUUID().toString();

    @Column(updatable = false, nullable = false)
    private String authId;

    private LocalDate orderDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private int deliveryPrice = PriceConstants.DEFAULT_DELIVERY_PRICE;

    private Long amount;

    @OneToMany(mappedBy = "orders")
    private List<CustomProduct> customProducts = new ArrayList<>();

    @JoinColumn(name = "order_destination_id")
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    private OrderDestination orderDestination;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;

    private Orders(String authId, int deliveryPrice, OrderDestination orderDestination) {
        this.authId = authId;
        orderStatus = OrderStatus.ORDERED;
        this.deliveryPrice = deliveryPrice;
        this.orderDestination = orderDestination;
    }

    private Orders(String authId, OrderDestination orderDestination) {
        this.authId = authId;
        this.orderStatus = OrderStatus.READY;
        this.orderDestination = orderDestination;
    }

    public static Orders create(String authId, int deliveryPrice, OrderDestination orderDestination){
        return new Orders(authId,deliveryPrice,orderDestination);
    }

    public static Orders create(String authId, OrderDestination orderDestination){
        return new Orders(authId, orderDestination);
    }

    public void changeOrderStatusToNextStep(){
        if(orderStatus.equals(OrderStatus.COMPLETE))
            throw new AlreadyCompletedOrderException();
        this.orderStatus = OrderStatus.values()[orderStatus.ordinal()+1];
    }

    public void associateWithCustomProduct(List<CustomProduct> customProducts){
        customProducts.forEach(cp ->
                cp.associateWithOrder(this));
        this.customProducts = customProducts;
    }

    void addCustomProduct(CustomProduct customProduct) {
        this.customProducts.add(customProduct);
    }


    public void calcTotalAmountAndSet() {
        AtomicLong totalAmount = new AtomicLong();

        this.customProducts.forEach(customProduct -> {
            // 기본금
            totalAmount.getAndAdd(customProduct.getProduct().getPrice());
            // 옵션 추가금액
            customProduct.getOptions().forEach(customProductOption ->
                    totalAmount.getAndAdd(customProductOption.getOptionDetail().getPrice()));
            // 수량
            totalAmount.getAndUpdate(x -> customProduct.getQuantity() * x);
        });
        // 배송비
        totalAmount.getAndAdd(this.deliveryPrice);

        this.amount = totalAmount.get();
    }

    public void setPayment(Payment<?> payment) {
        this.payment = payment;
    }

    public void changeOrderStatusToOrdered() {
        this.orderStatus = OrderStatus.ORDERED;
    }
}
