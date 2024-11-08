package store.io;

import java.text.DecimalFormat;
import java.util.List;
import store.domain.Product;

public class OutputView {

    public static void printAllStockQuantities(List<Product> products) {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.\n");
        StringBuilder sb = new StringBuilder();
        products.forEach(product -> printStockQuantity(sb, product));
        System.out.println(sb);
    }

    private static void printStockQuantity(StringBuilder sb, Product product) {
        sb.append("- ")
                .append(product.getName())
                .append(" ");
        changePriceToOutputViewFormat(sb, product.getPrice());
        sb.append(" ");
        changeQuantityToOutputViewFormat(sb, product.getQuantity());
        sb.append(" ");
        changePromotionToOutputViewFormat(sb, product.getPromotion());
        sb.append("\n");

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
