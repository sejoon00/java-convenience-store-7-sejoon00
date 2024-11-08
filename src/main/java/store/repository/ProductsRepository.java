package store.repository;

import java.util.List;
import store.domain.Product;

public interface ProductsRepository {

    List<Product> findAllProducts();
}
