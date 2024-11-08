package store.service;

import java.util.List;
import store.domain.Promotion;
import store.domain.PromotionProducts;
import store.domain.PurchaseProducts;
import store.domain.StockProducts;

public class PurchaseProductService {

    private final PromotionService promotionService;

    public PurchaseProductService(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    public void getReceipts(PurchaseProducts purchaseProducts, StockProducts stockProducts) {
        List<Promotion> allPromotions = promotionService.getAllPromotions();

        checkPromotionProduct(purchaseProducts, stockProducts, allPromotions);

    }

    private static void checkPromotionProduct(PurchaseProducts purchaseProducts, StockProducts stockProducts,
                                              List<Promotion> allPromotions) {
        purchaseProducts.getPurchaseProducts()
                .forEach(purchaseProduct -> {
                    PromotionProducts promotionProducts = stockProducts.getPromotionProducts();
                    if(promotionProducts.isPromotionProduct(purchaseProduct, allPromotions)){
                        purchaseProduct.setIsPromotionTrue();
                    }
                });
    }
}
