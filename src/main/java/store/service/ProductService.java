package store.service;

import java.util.List;
import store.domain.Product;
import store.repository.ProductsRepository;

public class ProductService {

    private final ProductsRepository productsRepository;

    public ProductService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public List<Product> getAllProducts(){
        return productsRepository.findAllProducts();
    }

}
