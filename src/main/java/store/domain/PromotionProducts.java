package store.domain;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
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
        Product product = promotionProducts.get(purchaseProduct.getName());
        return allPromotions.stream()
                .filter(Promotion::isIncludeDateRange)
                .anyMatch(promotion -> promotion.isEqualsName(product.getPromotion()));
    }

    public boolean validateStock(PurchaseProduct purchaseProduct) {
        //프로모션 상품 기간인지부터 확인하자
        LocalDateTime now = DateTimes.now();

        if(promotionProducts.containsKey(purchaseProduct.getName())){

        }
        return true;
    }


}
