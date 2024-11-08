package store.domain;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.error.ErrorCode;

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
        Product product = promotionProducts.get(purchaseProduct.getName());
        return allPromotions.stream()
                .filter(Promotion::isIncludeDateRange)
                .anyMatch(promotion -> promotion.isEqualsName(product.getPromotion()));
    }

    public void updateStockQuantity(PurchaseProduct purchaseProduct) {
        if (!promotionProducts.containsKey(purchaseProduct.getName())) {
            throw new IllegalArgumentException(ErrorCode.NOT_FOUND_PRODUCT.getMessage());
        }
        Product product = promotionProducts.get(purchaseProduct.getName());
        product.minusQuantity(purchaseProduct.getQuantity());
    }


}
