package store.repository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import store.domain.Product;
import store.error.ErrorCode;

public class ProductsMarkDownFileRepository implements ProductsRepository {

    public static final String PRODUCTS_MD_FILE = "src/main/resources/products.md";

    @Override
    public List<Product> findAllProducts() {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(PRODUCTS_MD_FILE)
        )) {
            String textLine;
            String header = reader.readLine();
            List<Product> products = new ArrayList<>();
            while ((textLine = reader.readLine()) != null) {

                List<String> splitTextLine = Arrays.stream(textLine.split(","))
                        .map(this::getValueOrNull)
                        .toList();
                String name = splitTextLine.get(0);
                int price = parsePriceToInt(splitTextLine.get(1));
                int quantity = parseQuantityToInt(splitTextLine.get(2));
                String promotion = splitTextLine.get(3);
                products.add(new Product(name, price, quantity, promotion));
            }
            return products;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(ErrorCode.File_NOT_FOUND.getMessage() + PRODUCTS_MD_FILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer parsePriceToInt(String price){
        try{
            int intPrice = Integer.parseInt(price);
            if(intPrice <= 0)
                throw new IllegalArgumentException(ErrorCode.PRODUCT_PRICE_IS_NOT_POSITIVE_NUMBERS.getMessage());
            return intPrice;
        } catch (NumberFormatException e){
            throw new IllegalArgumentException(ErrorCode.FILE_VALUE_TYPE_ERROR.getMessage());
        }
    }

    public Integer parseQuantityToInt(String quantity){
        try{
            int intQuantity = Integer.parseInt(quantity);
            if(intQuantity < 0)
                throw new IllegalArgumentException(ErrorCode.PRODUCT_QUANTITY_IS_NEGATIVE_NUMBERS.getMessage());
            return intQuantity;
        } catch (NumberFormatException e){
            throw new IllegalArgumentException(ErrorCode.FILE_VALUE_TYPE_ERROR.getMessage());
        }
    }

    private String getValueOrNull(String value){
        if(value.equals("null") || value.isBlank())
            return null;
        return value;
    }

}
