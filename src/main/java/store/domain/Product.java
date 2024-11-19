package store.domain;

import store.error.ErrorCode;

public class Product {

    private final String name;
    private final int price;
    private int quantity;
    private final String promotionName;
    private Promotion promotion;

    public Product(String name, int price, int quantity, String promotionName) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotionName = promotionName;
    }



    @Override
    public String toString() {
        return this.name;
    }

    public int getAvailablePromotionCount() {
        if (promotion == null) {
            return 0;
        }
        return quantity - (quantity % promotion.getPromotionBundleCount());
    }

    public boolean hasPromotion() {
        return promotionName != null;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void minusGeneralQuantity(PurchaseProduct purchaseProduct) {

        if (purchaseProduct.getQuantity() > this.quantity) {
            throw new IllegalArgumentException(ErrorCode.OVER_QUANTITY.getMessage());
        }
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public void minusQuantity(int quantity) {
        this.quantity -= quantity;
    }
}
