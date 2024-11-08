package store.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GeneralProducts {

    private Map<String, Product> generalProducts;

    public GeneralProducts(Map<String, Product> generalProducts) {
        this.generalProducts = generalProducts;
    }

    public static GeneralProducts from(List<Product> products){
        Map<String, Product> collectMap = products.stream()
                .filter(product -> !product.hasPromotion())
                .collect(Collectors.toMap(Product::getName, product -> product));

        return new GeneralProducts(collectMap);
    }
}
