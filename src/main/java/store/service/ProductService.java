package store.service;

import java.util.List;
import store.domain.GeneralProducts;
import store.domain.Product;
import store.domain.PromotionProducts;
import store.domain.StockProducts;
import store.repository.ProductsRepository;

public class ProductService {

    private final ProductsRepository productsRepository;

    public ProductService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public StockProducts getAllProducts(){
        return new StockProducts(productsRepository.findAllProducts());
    }




}
