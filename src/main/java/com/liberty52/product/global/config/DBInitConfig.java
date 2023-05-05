package com.liberty52.product.global.config;

import com.liberty52.product.global.contants.PriceConstants;
import com.liberty52.product.global.contants.ProductConstants;
import com.liberty52.product.global.contants.VBankConstants;
import com.liberty52.product.service.applicationservice.OrderCreateService;
import com.liberty52.product.service.entity.*;
import com.liberty52.product.service.entity.payment.VBank;
import com.liberty52.product.service.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DBInitConfig {

    private final DBInitService initService;

    @PostConstruct
    public void init() {
        initService.init();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    public static class DBInitService {

        private final OrderCreateService orderCreateService;
        private final CartItemRepository customProductRepository;
        private final ProductRepository productRepository;
        private final ProductOptionRepository productOptionRepository;
        private final OptionDetailRepository optionDetailRepository;
        private final CartRepository cartRepository;
        private final CustomProductOptionRepository customProductOptionRepository;
        private final OrdersRepository ordersRepository;
        public static final String AUTH_ID = "TESTER-001";
        public static final String LIBERTY = "Liberty 52_Frame";
        private static Orders order;
        private static Product product;
        private static Review review;
        private final ReviewRepository reviewRepository;
        private final VBankRepository vBankRepository;

        private final Environment env;

        public void init() {
            try {
                Product product = Product.create(LIBERTY, ProductState.ON_SAIL, 100L);
                Field id = product.getClass().getDeclaredField("id");
                id.setAccessible(true);
                id.set(product, "LIB-001");

                productRepository.save(product);
                DBInitService.product = product;

                ProductOption option1 = ProductOption.create(ProductConstants.PROD_OPT_1, true);
                option1.associate(product);
                option1 = productOptionRepository.save(option1);

                OptionDetail detailEasel = OptionDetail.create("이젤 거치형", 100);
                detailEasel.associate(option1);
                Field detailEaselId = detailEasel.getClass().getDeclaredField("id");
                detailEaselId.setAccessible(true);
                detailEaselId.set(detailEasel, "OPT-001");
                detailEasel = optionDetailRepository.save(detailEasel);

                OptionDetail detailWall = OptionDetail.create("벽걸이형", 100);
                detailWall.associate(option1);
                Field detailWallId = detailWall.getClass().getDeclaredField("id");
                detailWallId.setAccessible(true);
                detailWallId.set(detailWall, "OPT-002");
                detailWall = optionDetailRepository.save(detailWall);

                ProductOption option2 = ProductOption.create(ProductConstants.PROD_OPT_2, true);
                option2.associate(product);
                option2 = productOptionRepository.save(option2);

                OptionDetail material = OptionDetail.create("1mm 두께 승화전사 인쇄용 알루미늄시트", 100);
                material.associate(option2);
                Field materialId = material.getClass().getDeclaredField("id");
                materialId.setAccessible(true);
                materialId.set(material, "OPT-003");
                material = optionDetailRepository.save(material);

                ProductOption option3 = ProductOption.create(ProductConstants.PROD_OPT_3, true);
                option3.associate(product);
                option3 = productOptionRepository.save(option3);

                OptionDetail materialOption1 = OptionDetail.create("유광실버", 100);
                materialOption1.associate(option3);
                Field materialOption1Id = materialOption1.getClass().getDeclaredField("id");
                materialOption1Id.setAccessible(true);
                materialOption1Id.set(materialOption1, "OPT-004");
                materialOption1 = optionDetailRepository.save(materialOption1);

                OptionDetail materialOption2 = OptionDetail.create("무광실버", 100);
                materialOption2.associate(option3);
                materialOption2 = optionDetailRepository.save(materialOption2);

                OptionDetail materialOption3 = OptionDetail.create("유광백색", 100);
                materialOption3.associate(option3);
                materialOption3 = optionDetailRepository.save(materialOption3);

                OptionDetail materialOption4 = OptionDetail.create("무광백색", 100);
                materialOption4.associate(option3);
                materialOption4 = optionDetailRepository.save(materialOption4);

                // Add Cart & CartItems
                Cart cart = cartRepository.save(Cart.create(AUTH_ID));

                final String imageUrl = env.getProperty(
                        "product.representative-url.liberty52-frame");
                CustomProduct customProduct = CustomProduct.create(imageUrl, 1, AUTH_ID);
                customProduct.associateWithProduct(product);
                customProduct.associateWithCart(cart);
                customProduct = customProductRepository.save(customProduct);

                CustomProductOption customProductOption = CustomProductOption.create();
                customProductOption.associate(customProduct);
                customProductOption.associate(detailEasel);
                customProductOption = customProductOptionRepository.save(customProductOption);

                // Add Order
                Orders order = ordersRepository.save(
                        Orders.create(AUTH_ID, PriceConstants.DEFAULT_DELIVERY_PRICE,
                                OrderDestination.create("receiver", "email", "01012341234",
                                        "경기도 어딘가",
                                        "101동 101호", "12345")));
                DBInitService.order = order;

                customProduct = CustomProduct.create(imageUrl, 1, AUTH_ID);
                customProduct.associateWithProduct(product);
                customProduct.associateWithOrder(order);
                customProduct = customProductRepository.save(customProduct);

                customProductOption = CustomProductOption.create();
                customProductOption.associate(customProduct);
                customProductOption.associate(detailEasel);
                customProductOption = customProductOptionRepository.save(customProductOption);

                // Add Review
                Review review = Review.create(3, "good");
                review.associate(order);
                review.associate(product);
                ReviewImage.create(review, imageUrl);

                for (int i = 0; i < 3; i++) {
                    Reply reply = Reply.create("맛있따" + i, AUTH_ID);
                    reply.associate(review);
                }
                reviewRepository.save(review);

                // Add Order
                Orders orderSub
                        = ordersRepository.save(
                        Orders.create(AUTH_ID, PriceConstants.DEFAULT_DELIVERY_PRICE,
                                OrderDestination.create("receiver", "email", "01012341234",
                                        "경기도 어딘가",
                                        "101동 101호", "12345")));
                DBInitService.order = order;

                customProduct = CustomProduct.create(imageUrl, 1, AUTH_ID);
                customProduct.associateWithProduct(product);
                customProduct.associateWithOrder(orderSub);
                customProduct = customProductRepository.save(customProduct);

                customProductOption = CustomProductOption.create();
                customProductOption.associate(customProduct);
                customProductOption.associate(detailEasel);
                customProductOption = customProductOptionRepository.save(customProductOption);

                for (int i = 0; i < 10; i++) {
                    Orders guestOrder = Orders.create("GUEST-00"+i,
                            PriceConstants.DEFAULT_DELIVERY_PRICE,
                            OrderDestination.create("receiver", "email", "01012341234", "경기도 어딘가",
                                    "101동 101호", "12345"));

                    Field guestOrderId = guestOrder.getClass().getDeclaredField("id");
                    guestOrderId.setAccessible(true);
                    guestOrderId.set(guestOrder, "GORDER-00"+i);

                    ordersRepository.save(guestOrder);

                    customProduct = CustomProduct.create(imageUrl, 1, "GUEST-001");
                    customProduct.associateWithProduct(product);
                    customProduct.associateWithOrder(guestOrder);
                    customProduct = customProductRepository.save(customProduct);

                    customProductOption = CustomProductOption.create();
                    customProductOption.associate(customProduct);
                    customProductOption.associate(detailEasel);
                    customProductOption = customProductOptionRepository.save(customProductOption);

                    Review noPhotoReview = Review.create(3, "good");
                    noPhotoReview.associate(guestOrder);
                    noPhotoReview.associate(product);

                    reviewRepository.save(noPhotoReview);
                }


                VBank vBank_hana = VBank.of(VBankConstants.VBANK_HANA);
                VBank vBank_kb = VBank.of(VBankConstants.VBANK_KB);
                vBankRepository.saveAll(List.of(vBank_hana, vBank_kb));

                // 아래 save가 없어도 DB엔 정상적으로 들어가지만, 테스트에선 반영이 안 됨.
                DBInitService.order = ordersRepository.save(order);
                DBInitService.product = productRepository.save(product);
                DBInitService.review = reviewRepository.save(review);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static Orders getOrder() {
            return order;
        }

        public static Product getProduct() {
            return product;
        }
        public static Review getReview() {
            return review;
        }

    }
}
