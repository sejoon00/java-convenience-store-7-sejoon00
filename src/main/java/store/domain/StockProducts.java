package store.domain;

import java.util.Collections;
import java.util.List;
import store.error.ErrorCode;

public class StockProducts {

    private final List<Product> stockProducts;

    public StockProducts(List<Product> stockProducts) {
        this.stockProducts = List.copyOf(stockProducts);
    }

    public PromotionProducts getPromotionProducts(){
        return PromotionProducts.from(this.stockProducts);
    }

    public GeneralProducts getGeneralProducts(){
        return GeneralProducts.from(this.stockProducts);
    }

    public List<Product> getStockProducts() {
        return Collections.unmodifiableList(this.stockProducts);
    }

    public void updateStockQuantity(PurchaseProduct purchaseProduct) {
        if(purchaseProduct.isPromotionProduct()){
            getPromotionProducts().updateStockQuantity(purchaseProduct);
            return;
        }

        getGeneralProducts().updateStockQuantity(purchaseProduct);
    }

    public void validateProductExist(PurchaseProduct purchaseProduct) {
        boolean isExist = stockProducts.stream()
                .anyMatch(product -> product.getName().equals(purchaseProduct.getName()));

        if (!isExist) {
            throw new IllegalArgumentException(ErrorCode.NOT_FOUND_PRODUCT.getMessage());
        }
    }
}
