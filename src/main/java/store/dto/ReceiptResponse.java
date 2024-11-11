package store.dto;

import java.util.List;

public record ReceiptResponse(
        List<PurchaseProductResponse> purchaseProductResponses,
        List<PromotionProductResponse> promotionProductResponses,
        int totalPrice,
        int totalCount,
        int promotionDiscountPrice,
        int membershipDiscountPrice,
        int totalResult
) {
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

    @Override
    public int totalResult() {
        return totalResult;
    }
}
