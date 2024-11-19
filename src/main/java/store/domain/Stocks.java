package store.domain;

import java.util.List;
import store.error.ErrorCode;

public class Stocks {

    private final List<Product> stockProducts;
    private final PromotionStocks promotionStocks;
    private final GeneralStocks generalStocks;

    public Stocks(List<Product> stockProducts) {
        this.stockProducts = List.copyOf(stockProducts);
        this.promotionStocks = PromotionStocks.from(this.stockProducts);
        this.generalStocks = GeneralStocks.from(this.stockProducts);
    }

    public PromotionStocks getPromotionProducts(){
        return this.promotionStocks;
    }

    public GeneralStocks getGeneralProducts(){
        return this.generalStocks;
    }

    public List<Product> getStockProducts() {
        return this.stockProducts;
    }

    public Promotion getPromotion(String productName){
        Product product = getPromotionProducts().getProduct(productName);
        return product.getPromotion();
    }

    public void validateProductExist(PurchaseProduct purchaseProduct) {
        boolean isExist = stockProducts.stream()
                .anyMatch(product -> product.getName().equals(purchaseProduct.getName()));

        if (!isExist) {
            throw new IllegalArgumentException(ErrorCode.NOT_FOUND_PRODUCT.getMessage());
        }
    }

    public void checkStock(PurchaseProduct purchaseProduct) {
        Product promotionProduct = promotionStocks.getProduct(purchaseProduct.getName());
        if (promotionProduct != null) {
            purchaseProduct.setIsPromotionTrue();
            purchaseProduct.checkPromotionStock(promotionProduct);
        }

        Product generalProduct = generalStocks.getProduct(purchaseProduct.getName());
        purchaseProduct.checkGeneralStock(generalProduct);
    }



    public int getPriceByProductName(String productName) {
        return stockProducts.stream()
                .filter(product -> product.getName().equals(productName))
                .findFirst()
                .map(Product::getPrice)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_PRODUCT.getMessage()));
    }



}
