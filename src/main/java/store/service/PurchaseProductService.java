package store.service;

import java.util.ArrayList;
import java.util.List;
import store.config.AppConfig;
import store.domain.GeneralProducts;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.PromotionProducts;
import store.domain.PurchaseProduct;
import store.domain.PurchaseProducts;
import store.domain.StockProducts;
import store.dto.PromotionProductResponse;
import store.dto.PurchaseProductResponse;
import store.dto.ReceiptResponse;
import store.error.ErrorCode;
import store.io.InputView;

public class PurchaseProductService {

    private final PromotionService promotionService;
    private static final ProductService productService = AppConfig.productService();

    public PurchaseProductService(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    public ReceiptResponse getReceipt(PurchaseProducts purchaseProducts, boolean isMembership) {
        int total = 0;
        StockProducts allProducts = productService.getAllProducts();
        List<PurchaseProductResponse> purchaseProductResponses = new ArrayList<>();
        List<PromotionProductResponse> promotionProductResponses = new ArrayList<>();
        int totalPrice = 0;
        int totalCount = 0;
        int promotionDiscountPrice = 0;
        int availableMemberShipPrice = 0;
        int membershipDiscountPrice = 0;

        for (PurchaseProduct purchaseProduct : purchaseProducts.getPurchaseProducts()) {
            int price = allProducts.getPriceByProductName(purchaseProduct.getName());
            totalPrice += price * purchaseProduct.getQuantity();
            purchaseProductResponses.add(PurchaseProductResponse.from(purchaseProduct, totalPrice));
            promotionProductResponses.add(
                    new PromotionProductResponse(purchaseProduct.getName(), purchaseProduct.getGiftCount()));
            promotionDiscountPrice += price * purchaseProduct.getGiftCount();
            totalCount += purchaseProduct.getQuantity();
            availableMemberShipPrice += price * purchaseProduct.getGeneralCount();
        }
        if (isMembership) {
            membershipDiscountPrice = (int) (availableMemberShipPrice * 0.3);
            if (membershipDiscountPrice > 8000) {
                membershipDiscountPrice = 8000;
            }
        }
        int totalResult = totalPrice - promotionDiscountPrice - membershipDiscountPrice;
        return new ReceiptResponse(
                purchaseProductResponses,
                promotionProductResponses,
                totalCount,
                totalPrice,
                promotionDiscountPrice,
                membershipDiscountPrice,
                totalResult
        );
    }

    public void updateStockQuantity(StockProducts stockProducts, PurchaseProducts purchaseProducts) {

        purchaseProducts.getPurchaseProducts().stream()
                .forEach(purchaseProduct -> {
                    if (purchaseProduct.isPromotionProduct()) {
                        PromotionProducts promotionProducts = stockProducts.getPromotionProducts();
                        promotionProducts.getProduct(purchaseProduct.getName())
                                .minusQuantity(purchaseProduct.getPromotionCount());
                        GeneralProducts generalProducts = stockProducts.getGeneralProducts();
                        if(generalProducts.isGeneralProduct(purchaseProduct)){
                            generalProducts.getProduct(purchaseProduct.getName())
                                    .minusQuantity(purchaseProduct.getGeneralCount());
                        }
                    } else {
                        GeneralProducts generalProducts = stockProducts.getGeneralProducts();
                        generalProducts.getProduct(purchaseProduct.getName())
                                .minusQuantity(purchaseProduct.getGeneralCount());
                    }
                });

    }

    public PurchaseProducts checkFromStockQuantity(StockProducts stockProducts, PurchaseProducts purchaseProducts) {
        List<Promotion> allPromotions = promotionService.getAllPromotions();

        purchaseProducts.validateProductExist(stockProducts);

        GeneralProducts generalProducts = stockProducts.getGeneralProducts();
        PromotionProducts promotionProducts = stockProducts.getPromotionProducts();

        List<PurchaseProduct> purchaseProducts1 = purchaseProducts.getPurchaseProducts();

        for (PurchaseProduct purchaseProduct : purchaseProducts1) {
            //프로모션 프로덕트라면
            if (promotionProducts.isPromotionProduct(purchaseProduct, allPromotions)) {
                int 구매개수 = purchaseProduct.getQuantity();
                purchaseProduct.setIsPromotionTrue();
                Product promotionProduct = promotionProducts.getProduct(purchaseProduct.getName());

                Promotion promotion = promotionProduct.getPromotion();
//               - 콜라 1,000원 7개 탄산2+1
//               - 콜라 1,000원 10개
//               [콜라-10] [콜라-5]
                // 프로모션 적용 가능 개수를 구한다. 6개
                int 재고중_프로모션_가능한_개수 = promotionProduct.getAvailablePromotionCount();
                //프로모션 적용 가능 개수가 구매 개수보다 많다면
                if (재고중_프로모션_가능한_개수 > 구매개수) {
                    int 구매개수중_프로모션_적용된_개수 = 구매개수 - (구매개수 % promotion.getPromotionCount());
                    int 구매개수중_프로모션_미적용_개수 = 구매개수 % promotion.getPromotionCount();
                    //남은 개수가 프로모션 개수에 충족되는지
                    if (구매개수중_프로모션_미적용_개수 % promotion.getBuyCount() == 0) {
                        //프모모션 적용 개수가 남아서 추가제공할 수 있는지
                        if (재고중_프로모션_가능한_개수 >= 구매개수 + promotion.getPlusCount()) {
                            boolean isPurchase = InputView.printPromotionQuantityMoreEnough(promotionProduct.getName(),
                                    promotion.getPlusCount());
                            if (isPurchase) {
                                purchaseProduct.plusQuantity(promotion.getPlusCount());
                                purchaseProduct.setPromotionCount(구매개수 + promotion.getPlusCount());
                                purchaseProduct.setGiftCount((구매개수 + promotion.getPlusCount()) / promotion.getPromotionCount());

                            }
                            else {
                                purchaseProduct.setPromotionCount(구매개수);
                                purchaseProduct.setGiftCount((구매개수 / promotion.getPromotionCount()));
                            }
                        } else {
                            //프모모션 적용 개수가 남지 않아서 정가계산해야댐
                            int 정가구매개수 = promotion.getBuyCount();
                            Product generalProduct = generalProducts.getProduct(purchaseProduct.getName());
                            int 구매가능수량 = generalProduct.getQuantity();
                            if (구매가능수량 < 정가구매개수) {
                                throw new IllegalArgumentException(ErrorCode.OVER_QUANTITY.getMessage());
                            }
                            boolean isPurchase = InputView.printPromotionQuantityNotEnough(promotionProduct.getName(),
                                    promotion.getBuyCount());
                            if (isPurchase) {

                                purchaseProduct.setGeneralCount(정가구매개수);
                                purchaseProduct.setPromotionCount(구매개수중_프로모션_적용된_개수);
                                purchaseProduct.setGiftCount(구매개수중_프로모션_적용된_개수 / promotion.getPromotionCount());
                            }
                            else {
                                purchaseProduct.minusQuantity(정가구매개수);
                                purchaseProduct.setPromotionCount(구매개수중_프로모션_적용된_개수);
                                purchaseProduct.setGiftCount(구매개수중_프로모션_적용된_개수 / promotion.getPromotionCount());
                            }
                        }

                    } else {
                        int 정가구매개수 = 구매개수중_프로모션_미적용_개수;
                        Product generalProduct = generalProducts.getProduct(purchaseProduct.getName());
                        int 구매가능수량 = generalProduct.getQuantity();
                        if (구매가능수량 < 정가구매개수) {
                            throw new IllegalArgumentException(ErrorCode.OVER_QUANTITY.getMessage());
                        }
                        boolean isPurchase = InputView.printPromotionQuantityNotEnough(promotionProduct.getName(),
                                구매개수중_프로모션_미적용_개수);
                        if (isPurchase) {

                            purchaseProduct.setGeneralCount(정가구매개수);
                            purchaseProduct.setPromotionCount(구매개수중_프로모션_적용된_개수);
                            purchaseProduct.setGiftCount(구매개수중_프로모션_적용된_개수 / promotion.getPromotionCount());

                        }
                        else {
                            purchaseProduct.minusQuantity(정가구매개수);
                            purchaseProduct.setPromotionCount(구매개수중_프로모션_적용된_개수);
                            purchaseProduct.setGiftCount(구매개수중_프로모션_적용된_개수 / promotion.getPromotionCount());
                        }
                    }

                } else if (재고중_프로모션_가능한_개수 < 구매개수) {
                    //프로모션 적용 가능 개수가 구매 개수보다 적다면
                    int 프로모션_미적용_개수 = 구매개수 - 재고중_프로모션_가능한_개수;
                    int 정가구매개수 = 프로모션_미적용_개수;
                    Product generalProduct = generalProducts.getProduct(purchaseProduct.getName());
                    int 구매가능수량 = generalProduct.getQuantity();
                    if (구매가능수량 < 정가구매개수) {
                        throw new IllegalArgumentException(ErrorCode.OVER_QUANTITY.getMessage());
                    }
                    boolean isPurchase = InputView.printPromotionQuantityNotEnough(promotionProduct.getName(),
                            프로모션_미적용_개수);
                    if (isPurchase) {

                        purchaseProduct.setGeneralCount(정가구매개수);
                        purchaseProduct.setPromotionCount(재고중_프로모션_가능한_개수);
                        purchaseProduct.setGiftCount(재고중_프로모션_가능한_개수 / promotion.getPromotionCount());
                    } else {
                        purchaseProduct.minusQuantity(정가구매개수);
                        purchaseProduct.setPromotionCount(재고중_프로모션_가능한_개수);
                        purchaseProduct.setGiftCount(재고중_프로모션_가능한_개수 / promotion.getPromotionCount());
                    }
                } else {
                    //프로모션 적용 가능 개수가 구매 개수와 같다면
                    purchaseProduct.setPromotionCount(구매개수);
                    purchaseProduct.setGiftCount(구매개수 / promotion.getPromotionCount());

                }
            } else {
                //정가 구매 상품이라면
                int 구매개수 = purchaseProduct.getQuantity();
                Product generalProduct = generalProducts.getProduct(purchaseProduct.getName());
                int 구매가능수량 = generalProduct.getQuantity();
                if (구매가능수량 < 구매개수) {
                    throw new IllegalArgumentException(ErrorCode.OVER_QUANTITY.getMessage());
                }
                purchaseProduct.setGeneralCount(구매개수);
            }
        }

        return purchaseProducts;
    }
}
