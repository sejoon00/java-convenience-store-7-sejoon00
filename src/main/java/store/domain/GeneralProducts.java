package store.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.error.ErrorCode;

public class GeneralProducts {

    private Map<String, Product> generalProducts;

    public GeneralProducts(Map<String, Product> generalProducts) {
        this.generalProducts = generalProducts;
    }

    public static GeneralProducts from(List<Product> products){
        Map<String, Product> collectMap = products.stream()
                .filter(product -> !product.hasPromotion())
                .collect(Collectors.toMap(Product::getName, product -> product));

        return new GeneralProducts(collectMap);
    }

    public void minusStockQuantity(PurchaseProduct purchaseProduct) {
        if (!generalProducts.containsKey(purchaseProduct.getName())) {
            throw new IllegalArgumentException(ErrorCode.NOT_FOUND_PRODUCT.getMessage());
        }
        Product product = generalProducts.get(purchaseProduct.getName());
    }

    public boolean isContainProduct(String productName) {
        return generalProducts.containsKey(productName);
    }

    public Product getProduct(String productName) {
        return generalProducts.get(productName);
    }

    public boolean isGeneralProduct(PurchaseProduct purchaseProduct) {
        return generalProducts.containsKey(purchaseProduct.getName());
    }
}
