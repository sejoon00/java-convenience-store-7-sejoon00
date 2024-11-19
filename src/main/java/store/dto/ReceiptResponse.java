package store.dto;

import java.util.List;
import store.domain.Receipt;

public record ReceiptResponse(
        List<PurchaseProductResponse> purchaseProductResponses,
        List<PromotionProductResponse> promotionProductResponses,
        int totalPrice,
        int totalCount,
        int promotionDiscountPrice,
        int membershipDiscountPrice,
        int totalPayment
) {

    public static ReceiptResponse of(
            List<PurchaseProductResponse> purchaseProductResponses,
            List<PromotionProductResponse> promotionProductResponses,
            Receipt receipt
    ){
        return new ReceiptResponse(
                purchaseProductResponses,
                promotionProductResponses,
                receipt.getTotalCount(),
                receipt.getTotalPrice(),
                receipt.getPromotionDiscountPrice(),
                receipt.setMembershipDiscountPrice(),
                receipt.getTotalPayment()
        );
    }

    @Override
    public List<PurchaseProductResponse> purchaseProductResponses() {
        return purchaseProductResponses;
    }

    @Override
    public List<PromotionProductResponse> promotionProductResponses() {
        return promotionProductResponses;
    }

    @Override
    public int totalPrice() {
        return totalPrice;
    }

    @Override
    public int totalCount() {
        return totalCount;
    }

    @Override
    public int promotionDiscountPrice() {
        return promotionDiscountPrice;
    }

    @Override
    public int membershipDiscountPrice() {
        return membershipDiscountPrice;
    }

    public int totalPayment() {
        return totalPayment;
    }
}
