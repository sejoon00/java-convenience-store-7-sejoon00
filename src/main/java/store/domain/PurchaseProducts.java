package store.domain;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class PurchaseProducts {

    List<PurchaseProduct> purchaseProducts;

    private PurchaseProducts(List<PurchaseProduct> purchaseProducts) {
        this.purchaseProducts = purchaseProducts;
    }

    public static PurchaseProducts from(String purchaseProductsInput){
        List<PurchaseProduct> purchaseProducts = splitPurchaseProduct(purchaseProductsInput).stream()
                .map(PurchaseProduct::from)
                .toList();
        return new PurchaseProducts(purchaseProducts);
    }

    private static List<String> splitPurchaseProduct(String purchaseProductsInput) {
        String[] split = purchaseProductsInput.split(",", -1);
        return Arrays.stream(split)
                .map(String::trim)
                .toList();
    }
}
