package store.domain;

public class Receipt {

    private int totalPrice = 0;
    private int totalCount = 0;
    private int promotionDiscountPrice = 0;
    private int availableMemberShipPrice = 0;
    private int membershipDiscountPrice = 0;
    private int totalPayment = 0;


    public Receipt(Stocks stocks, PurchaseProducts purchaseProducts, boolean isMembership) {
        initial(stocks, purchaseProducts);
        setMembershipDiscountPrice(isMembership);
        totalPayment = totalPrice - promotionDiscountPrice - membershipDiscountPrice;

    }

    private void initial(Stocks stocks, PurchaseProducts purchaseProducts){
        for (PurchaseProduct purchaseProduct : purchaseProducts.getPurchaseProducts()){
            int price = stocks.getPriceByProductName(purchaseProduct.getName());
            totalPrice += price * purchaseProduct.getQuantity();
            promotionDiscountPrice += price * purchaseProduct.getGiftCount();
            totalCount += purchaseProduct.getQuantity();
            availableMemberShipPrice += price * purchaseProduct.getGeneralCount();
        }
    }

    private void setMembershipDiscountPrice(boolean isMembership) {
        if (isMembership) {
            membershipDiscountPrice = (int) (availableMemberShipPrice * 0.3);
            if (membershipDiscountPrice > 8000) {
                membershipDiscountPrice = 8000;
            }
        }
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getPromotionDiscountPrice() {
        return promotionDiscountPrice;
    }

    public int setMembershipDiscountPrice() {
        return membershipDiscountPrice;
    }

    public int getTotalPayment() {
        return totalPayment;
    }
}
