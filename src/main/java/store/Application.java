package store;

import store.config.AppConfig;
import store.domain.PurchaseProducts;
import store.domain.Stocks;
import store.io.InputView;
import store.io.OutputView;
import store.service.ProductService;
import store.service.PurchaseProductService;

public class Application {

    private static final ProductService productService = AppConfig.productService();
    private static final PurchaseProductService purchaseProductService = AppConfig.purchaseProductService();

    public static void main(String[] args) {

        Stocks stocks = productService.getAllProducts();
        InputView inputView = new InputView();

        while (true) {
            OutputView.printAllStockQuantities(stocks.getStockProducts(), stocks);
            PurchaseProducts purchaseProducts = inputView.inputPurchaseProducts(stocks);
            boolean isMemberShip = InputView.isMembership();

            purchaseProductService.updateStockQuantity(stocks, purchaseProducts);
            OutputView.printReceipt(purchaseProductService.getReceipt(purchaseProducts, isMemberShip));

            if (!InputView.isContinue()) {
                break;
            }
        }
        inputView.close();
    }
}
