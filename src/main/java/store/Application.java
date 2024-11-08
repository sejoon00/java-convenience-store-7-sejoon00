package store;

import store.config.AppConfig;
import store.io.InputView;
import store.service.ProductService;

public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현

        ProductService productService = AppConfig.productService();
        productService.getAllProducts();

        try(InputView inputView = new InputView()){

        }
    }
}
