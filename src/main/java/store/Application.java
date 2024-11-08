package store;

import java.util.List;
import java.util.function.Supplier;
import store.config.AppConfig;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.PurchaseProducts;
import store.domain.StockProducts;
import store.io.InputView;
import store.io.OutputView;
import store.service.ProductService;
import store.service.PromotionService;
import store.service.PurchaseProductService;

public class Application {

    private static final ProductService productService = AppConfig.productService();
    private static final PurchaseProductService purchaseProductService = AppConfig.purchaseProductService();

    public static void main(String[] args) {

        StockProducts stockProducts = productService.getAllProducts();
        OutputView.printAllStockQuantities(stockProducts.getStockProducts());
        try (InputView inputView = new InputView()) {
            inputView.inputPurchaseProducts();
            System.out.println();
        }
    }
}
