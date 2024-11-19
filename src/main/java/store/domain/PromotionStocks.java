package store.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PromotionStocks {

    private Map<String, Product> promotionProducts;

    private PromotionStocks(Map<String, Product> promotionProducts) {
        this.promotionProducts = promotionProducts;
    }

    public static PromotionStocks from(List<Product> products){
        Map<String, Product> collectMap = products.stream()
                .filter(Product::hasPromotion)
                .collect(Collectors.toMap(Product::getName, product -> product));

        return new PromotionStocks(collectMap);
    }

    public Product getProduct(String productName) {
        return promotionProducts.get(productName);
    }
}
