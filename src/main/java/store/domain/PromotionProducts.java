package store.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PromotionProducts {

    private Map<String, Product> promotionProducts;

    private PromotionProducts(Map<String, Product> promotionProducts) {
        this.promotionProducts = promotionProducts;
    }

    public static PromotionProducts from(List<Product> products){
        Map<String, Product> collectMap = products.stream()
                .filter(Product::hasPromotion)
                .collect(Collectors.toMap(Product::getName, product -> product));

        return new PromotionProducts(collectMap);
    }

    public boolean isPromotionProduct(PurchaseProduct purchaseProduct, List<Promotion> allPromotions) {
        if (!promotionProducts.containsKey(purchaseProduct.getName())) {
            return false;
        }
        Product product = promotionProducts.get(purchaseProduct.getName());
        return allPromotions.stream()
                .filter(Promotion::isIncludeDateRange)
                .anyMatch(promotion -> promotion.isEqualsName(product.getPromotionName()));
    }

    public Product getProduct(String productName) {
        return promotionProducts.get(productName);
    }
}
