package store.service;

import java.util.List;
import store.domain.GeneralProducts;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.PromotionProducts;
import store.domain.StockProducts;
import store.repository.ProductsRepository;

public class ProductService {

    private final ProductsRepository productsRepository;
    private final PromotionService promotionService;

    public ProductService(ProductsRepository productsRepository, PromotionService promotionService) {
        this.productsRepository = productsRepository;
        this.promotionService = promotionService;
    }

    public StockProducts getAllProducts(){
        List<Product> allProducts = productsRepository.findAllProducts();
        allProducts.stream()
                .filter(Product::hasPromotion)
                .forEach(product -> {
                    Promotion promotion = promotionService.getPromotionByName(product.getPromotionName());
                    product.setPromotion(promotion);
                });
        return new StockProducts(allProducts);
    }




}
