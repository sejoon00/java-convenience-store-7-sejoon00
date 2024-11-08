package store.domain;

import java.util.Collections;
import java.util.List;

public class StockProducts {

    private final List<Product> stockProducts;

    public StockProducts(List<Product> stockProducts) {
        this.stockProducts = List.copyOf(stockProducts);
    }

    public PromotionProducts getPromotionProducts(){
        return PromotionProducts.from(this.stockProducts);
    }

    public GeneralProducts getGeneralProducts(){
        return GeneralProducts.from(this.stockProducts);
    }

    public List<Product> getStockProducts() {
        return Collections.unmodifiableList(this.stockProducts);
    }
}
