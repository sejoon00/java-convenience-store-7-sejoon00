package store.config;

import store.repository.ProductsMarkDownFileRepository;
import store.repository.ProductsRepository;
import store.service.ProductService;

public class AppConfig {

    public static ProductService productService(){
        return new ProductService(productsRepository());
    }

    public static ProductsRepository productsRepository(){
        return new ProductsMarkDownFileRepository();
    }
}
