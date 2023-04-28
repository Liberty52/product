package com.liberty52.product.service.applicationservice;

import com.liberty52.product.MockS3Test;
import com.liberty52.product.TestBeanConfig;
import com.liberty52.product.global.adapter.cloud.AuthServiceClient;
import com.liberty52.product.global.config.DBInitConfig;
import com.liberty52.product.service.controller.dto.PaymentVBankResponseDto;
import com.liberty52.product.service.controller.dto.PreregisterOrderRequestDto;
import com.liberty52.product.service.controller.dto.PreregisterOrderResponseDto;
import com.liberty52.product.service.entity.OrderStatus;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.entity.payment.PaymentStatus;
import com.liberty52.product.service.entity.payment.PaymentType;
import com.liberty52.product.service.entity.payment.VBankPayment;
import com.liberty52.product.service.repository.CustomProductRepository;
import com.liberty52.product.service.repository.OrdersRepository;
import com.liberty52.product.service.repository.ProductOptionRepository;
import com.liberty52.product.service.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import({TestBeanConfig.class})
class MonoItemOrderServiceImplTest extends MockS3Test {
    @Autowired
    MonoItemOrderService monoItemOrderService;
    @Autowired
    CustomProductRepository customProductRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductOptionRepository productOptionRepository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private AuthServiceClient authServiceClient;

    MonoItemOrderServiceImplTest() throws IOException {
    }

    String productName = "Liberty 52_Frame";
    String authId = UUID.randomUUID().toString();
    MockMultipartFile imageFile = new MockMultipartFile("image", "test.png", "image/jpeg", new FileInputStream("src/test/resources/static/test.jpg"));

    private static final String LIBERTY = "Liberty 52_Frame";
    private static final String OPTION_1 = "이젤 거치형";
    private static final String OPTION_2 = "1mm 두께 승화전사 인쇄용 알루미늄시트";
    private static final String OPTION_3 = "유광실버";
    private static final int QUANTITY = 2;

    @Test
    void test_preregisterCardPaymentOrders() {
        PreregisterOrderResponseDto dto = monoItemOrderService.preregisterCardPaymentOrders(authId,
                PreregisterOrderRequestDto.forTestCard(
                        LIBERTY, List.of(OPTION_1, OPTION_2, OPTION_3), 2, List.of(),
                        "receiverName", "receiverEmail", "receiverPhoneNumber", "address1", "address2", "zipCode"),
                        imageFile);

        String oId = dto.getMerchantId();
        Orders orders = ordersRepository.findById(oId).get();

        Assertions.assertNotNull(orders);
        Assertions.assertEquals(authId, orders.getAuthId());
        Assertions.assertEquals(productName, orders.getCustomProducts().get(0).getProduct().getName());
        Assertions.assertEquals(3, orders.getCustomProducts().get(0).getOptions().size());
        Assertions.assertEquals(2, orders.getCustomProducts().get(0).getQuantity());
        Assertions.assertEquals("receiverName", orders.getOrderDestination().getReceiverName());
        Assertions.assertEquals("receiverEmail", orders.getOrderDestination().getReceiverEmail());
        Assertions.assertEquals("receiverPhoneNumber", orders.getOrderDestination().getReceiverPhoneNumber());
        Assertions.assertEquals("address1", orders.getOrderDestination().getAddress1());
        Assertions.assertEquals("address2", orders.getOrderDestination().getAddress2());
        Assertions.assertEquals("zipCode", orders.getOrderDestination().getZipCode());

        Assertions.assertEquals(OrderStatus.READY, orders.getOrderStatus());
        Assertions.assertNotEquals(0, orders.getAmount());

        Assertions.assertNotNull(orders.getPayment());
        Assertions.assertEquals(PaymentType.CARD, orders.getPayment().getType());
        Assertions.assertEquals(PaymentStatus.READY, orders.getPayment().getStatus());
        Assertions.assertSame(orders, orders.getPayment().getOrders());
        Assertions.assertTrue(orders.getPayment().getInfoAsString().isBlank());
    }

    @Test
    public void calcTotalPrice() {
        Orders order = DBInitConfig.DBInitService.getOrder();
        order.calcTotalAmountAndSet();
        System.out.println(order.getAmount());
    }

    @Test
    void test_registerVBankPaymentOrders() {
        PreregisterOrderRequestDto requestDto = PreregisterOrderRequestDto.forTestVBank(
                LIBERTY, List.of(OPTION_1, OPTION_2, OPTION_3), QUANTITY, List.of(),
                "receiverName", "receiverEmail", "receiverPhoneNumber", "address1", "address2", "zipCode",
                "하나은행 1234123412341234 리버티", "tester"
        );
        PaymentVBankResponseDto responseDto = monoItemOrderService.registerVBankPaymentOrders("AUTH_ID", requestDto, imageFile);

        String orderId = responseDto.getOrderId();

        Orders order = ordersRepository.findById(orderId).orElseThrow(RuntimeException::new);

        Assertions.assertNotNull(order);
        Assertions.assertEquals(OrderStatus.WAITING_DEPOSIT, order.getOrderStatus());
        Assertions.assertEquals(PaymentType.VBANK, order.getPayment().getType());
        Assertions.assertNotEquals("", order.getPayment().getInfoAsString());
        Assertions.assertNotNull(((VBankPayment.VBankPaymentInfo)(order.getPayment().getInfoAsDto())).getVbankInfo());
        Assertions.assertNotNull(((VBankPayment.VBankPaymentInfo)(order.getPayment().getInfoAsDto())).getDepositorName());
    }

}
