package store.dto;

import store.domain.PurchaseProduct;

public record PurchaseProductResponse(
        String productName,
        int productQuantity,
        int totalPrice
) {

    public static PurchaseProductResponse from(PurchaseProduct purchaseProduct, int totalPrice) {
        return new PurchaseProductResponse(
                purchaseProduct.getName(),
                purchaseProduct.getQuantity(),
                totalPrice
        );
    }
}
