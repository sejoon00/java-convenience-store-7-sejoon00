package store.config;

import store.repository.ProductsMarkDownFileRepository;
import store.repository.ProductsRepository;
import store.repository.PromotionMarkDownFileRepository;
import store.repository.PromotionRepository;
import store.service.ProductService;
import store.service.PromotionService;
import store.service.PurchaseProductService;

public class AppConfig {

    public static ProductService productService(){
        return new ProductService(productsRepository(), promotionService());
    }

    public static ProductsRepository productsRepository(){
        return new ProductsMarkDownFileRepository();
    }

    public static PromotionService promotionService(){
        return new PromotionService(promotionRepository());
    }

    public static PromotionRepository promotionRepository() {
        return new PromotionMarkDownFileRepository();
    }

    public static PurchaseProductService purchaseProductService() {
        return new PurchaseProductService();
    }
}
