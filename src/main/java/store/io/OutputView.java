package store.io;

import java.text.DecimalFormat;
import java.util.List;
import store.domain.GeneralStocks;
import store.domain.Product;
import store.domain.Stocks;
import store.dto.ReceiptResponse;

public class OutputView {

    public static void printReceipt(ReceiptResponse receiptResponse) {
        System.out.println("==============W 편의점================");
        System.out.println("상품명\t\t수량\t금액");
        receiptResponse.purchaseProductResponses().forEach(purchaseProductResponse -> {
            System.out.println(purchaseProductResponse.productName() + "\t\t" + purchaseProductResponse.productQuantity() + "\t" + getDFFormat(purchaseProductResponse.totalPrice()));
        });
        System.out.println("=============증\t정===============");
        receiptResponse.promotionProductResponses().forEach(promotionProductResponse -> {
            System.out.println(promotionProductResponse.productName() + "\t\t" + promotionProductResponse.count());
        });
        System.out.println("====================================");
        System.out.println("총구매액\t\t" + receiptResponse.totalCount() + "\t" + getDFFormat(receiptResponse.totalPrice()));
        System.out.println("행사할인"+"-" + getDFFormat(receiptResponse.promotionDiscountPrice()));
        System.out.println("멤버십할인"+"-" + getDFFormat(receiptResponse.membershipDiscountPrice()));
        System.out.println("내실돈" + getDFFormat(receiptResponse.totalPayment()));
        System.out.println();
    }

    private static String getDFFormat(int price) {
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(price);
    }

    public static void printAllStockQuantities(List<Product> products, Stocks stocks) {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.\n");
        StringBuilder sb = new StringBuilder();
        products.forEach(product -> printStockQuantity(sb, product, stocks));
        System.out.println(sb);
    }

    private static void printStockQuantity(StringBuilder sb, Product product, Stocks stocks) {
        sb.append("- ")
                .append(product.getName())
                .append(" ");
        changePriceToOutputViewFormat(sb, product.getPrice());
        sb.append(" ");
        changeQuantityToOutputViewFormat(sb, product.getQuantity());
        sb.append(" ");
        changePromotionToOutputViewFormat(sb, product.getPromotionName());
        sb.append("\n");
        GeneralStocks generalStocks = stocks.getGeneralProducts();
        if(!generalStocks.isContainProduct(product.getName())){

            sb.append("- ")
                    .append(product.getName())
                    .append(" ");
            changePriceToOutputViewFormat(sb, product.getPrice());
            sb.append(" ");
            changeQuantityToOutputViewFormat(sb, 0);
            sb.append(" ");
            changePromotionToOutputViewFormat(sb, product.getPromotionName());
            sb.append("\n");
        }

    }

    private static void changePriceToOutputViewFormat(StringBuilder sb, int price) {
        DecimalFormat df = new DecimalFormat("###,###");
        sb.append(df.format(price)).append("원");
    }

    private static void changeQuantityToOutputViewFormat(StringBuilder sb, int quantity) {
        if (quantity == 0) {
            sb.append("재고 없음");
            return;
        }
        sb.append(quantity).append("개");
    }

    private static void changePromotionToOutputViewFormat(StringBuilder sb, String promotion) {
        if (promotion == null || promotion.isBlank()) {
            return;
        }
        sb.append(promotion);
    }


}
