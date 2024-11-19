package store.service;

import java.util.List;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.Stocks;
import store.repository.ProductsRepository;

public class ProductService {

    private final ProductsRepository productsRepository;
    private final PromotionService promotionService;

    public ProductService(ProductsRepository productsRepository, PromotionService promotionService) {
        this.productsRepository = productsRepository;
        this.promotionService = promotionService;
    }

    public Stocks getAllProducts(){
        List<Product> allProducts = productsRepository.findAllProducts();
        allProducts.stream()
                .filter(Product::hasPromotion)
                .forEach(product -> {
                    Promotion promotion = promotionService.getPromotionByName(product.getPromotionName());
                    product.setPromotion(promotion);
                });

        allProducts.stream()
                .filter(Product::hasPromotion)
                .forEach(product -> {
                    Promotion promotion = promotionService.getPromotionByName(product.getPromotionName());
                    product.setPromotion(promotion);
                });

        Stocks stocks = new Stocks(allProducts);

        return new Stocks(allProducts);
    }




}
