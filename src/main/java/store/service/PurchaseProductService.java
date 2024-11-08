package store.service;

import java.util.List;
import store.config.AppConfig;
import store.domain.Promotion;
import store.domain.PurchaseProducts;
import store.domain.StockProducts;

public class PurchaseProductService {

    private final PromotionService promotionService;
    private static final ProductService productService = AppConfig.productService();

    public PurchaseProductService(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    public PurchaseProducts updateStockQuantity(PurchaseProducts purchaseProducts) {
        StockProducts stockProducts = productService.getAllProducts();
        List<Promotion> allPromotions = promotionService.getAllPromotions();

        purchaseProducts.validateProductExist(stockProducts);
        purchaseProducts.validateStock(stockProducts, allPromotions);
        return purchaseProducts;
    }
}
