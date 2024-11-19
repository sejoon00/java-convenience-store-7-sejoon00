package store.service;

import java.util.ArrayList;
import java.util.List;
import store.config.AppConfig;
import store.domain.GeneralStocks;
import store.domain.Promotion;
import store.domain.PromotionStocks;
import store.domain.PromotionState;
import store.domain.PurchaseProduct;
import store.domain.PurchaseProducts;
import store.domain.Receipt;
import store.domain.Stocks;
import store.dto.PromotionProductResponse;
import store.dto.PurchaseProductResponse;
import store.dto.ReceiptResponse;
import store.io.InputView;

public class PurchaseProductService {

    private static final ProductService productService = AppConfig.productService();


    public PurchaseProducts checkFromStockQuantity(Stocks stocks, PurchaseProducts purchaseProducts) {
        purchaseProducts.validateProductExist(stocks);
        purchaseProducts.checkStock(stocks);
        inputPromotionQuestion(stocks, purchaseProducts);
        return purchaseProducts;
    }

    private static void inputPromotionQuestion(Stocks stocks, PurchaseProducts purchaseProducts) {
        List<PurchaseProduct> purchaseProductlist = purchaseProducts.getPurchaseProducts();

        for (PurchaseProduct purchaseProduct : purchaseProductlist) {
            if (!purchaseProduct.isPromotionProduct()) {
                continue;
            }
            Promotion promotion = stocks.getPromotion(purchaseProduct.getName());

            PromotionState promotionState = purchaseProduct.getPromotionState();

            if (promotionState == PromotionState.MORE_PROMOTION) {
                boolean isPurchase = InputView.printPromotionQuantityMoreEnough(purchaseProduct.getName(),
                        promotion.getPlusCount());
                if (!isPurchase) {
                    purchaseProduct.notGetMorePromotion();
                }
            }
            if (promotionState == PromotionState.LACK_PROMOTION) {
                boolean isPurchase = InputView.printPromotionQuantityNotEnough(purchaseProduct.getName(),
                        purchaseProduct.getGeneralCount());
                if (!isPurchase) {
                    purchaseProduct.notGetLackPromotion();
                }
            }
        }
    }

    public ReceiptResponse getReceipt(PurchaseProducts purchaseProducts, boolean isMembership) {
        Stocks stocks = productService.getAllProducts();
        List<PurchaseProductResponse> purchaseProductResponses = new ArrayList<>();
        List<PromotionProductResponse> promotionProductResponses = new ArrayList<>();

        Receipt receipt = new Receipt(stocks, purchaseProducts, isMembership);

        for (PurchaseProduct purchaseProduct : purchaseProducts.getPurchaseProducts()) {
            int price = stocks.getPriceByProductName(purchaseProduct.getName());
            int totalPrice = price * purchaseProduct.getQuantity();
            purchaseProductResponses.add(PurchaseProductResponse.from(purchaseProduct, totalPrice));
            promotionProductResponses.add(
                    new PromotionProductResponse(purchaseProduct.getName(), purchaseProduct.getGiftCount()));
        }
        return ReceiptResponse.of(purchaseProductResponses, promotionProductResponses, receipt);
    }

    public void updateStockQuantity(Stocks stocks, PurchaseProducts purchaseProducts) {
        purchaseProducts.getPurchaseProducts()
                .forEach(purchaseProduct -> {caculateStockCount(stocks, purchaseProduct);});
    }

    private static void caculateStockCount(Stocks stocks, PurchaseProduct purchaseProduct) {
        if (purchaseProduct.isPromotionProduct()) {
            PromotionStocks promotionStocks = stocks.getPromotionProducts();
            promotionStocks.getProduct(purchaseProduct.getName())
                    .minusQuantity(purchaseProduct.getPromotionCount());
        }
        GeneralStocks generalStocks = stocks.getGeneralProducts();
        generalStocks.getProduct(purchaseProduct.getName())
                .minusQuantity(purchaseProduct.getGeneralCount());
    }
}
