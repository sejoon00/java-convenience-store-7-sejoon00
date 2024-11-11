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
        //TODO: 실제로 재고 차감시켜야됨 아직 안함
        if(purchaseProduct.isPromotionProduct()){
            getPromotionProducts().updateStockQuantity(purchaseProduct, getGeneralProducts());
            return;
        }
        getGeneralProducts().minusStockQuantity(purchaseProduct);
    }

    public void validateProductExist(PurchaseProduct purchaseProduct) {
        boolean isExist = stockProducts.stream()
                .anyMatch(product -> product.getName().equals(purchaseProduct.getName()));

        if (!isExist) {
            throw new IllegalArgumentException(ErrorCode.NOT_FOUND_PRODUCT.getMessage());
        }
    }

    public int getPriceByProductName(String productName) {
        return stockProducts.stream()
                .filter(product -> product.getName().equals(productName))
                .findFirst()
                .map(Product::getPrice)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_PRODUCT.getMessage()));
    }

}
