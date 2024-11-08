package store;

import java.util.List;
import store.config.AppConfig;
import store.domain.Product;
import store.io.InputView;
import store.io.OutputView;
import store.service.ProductService;

public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현

        ProductService productService = AppConfig.productService();
        List<Product> allProducts = productService.getAllProducts();
        OutputView.printAllStockQuantities(allProducts);
        try(InputView inputView = new InputView()){

        }
    }
}
