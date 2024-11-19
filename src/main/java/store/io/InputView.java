package store.io;

import camp.nextstep.edu.missionutils.Console;
import java.util.function.Supplier;
import store.config.AppConfig;
import store.domain.PurchaseProducts;
import store.domain.Stocks;
import store.error.ErrorCode;
import store.service.PurchaseProductService;

public class InputView implements AutoCloseable {

    private static final PurchaseProductService purchaseProductService = AppConfig.purchaseProductService();
    private final Reader DEFAULT_READER = new ConsoleReader();
    private final Reader reader;

    public InputView(Reader reader) {
        this.reader = reader;
    }

    public InputView() {
        this.reader = DEFAULT_READER;
    }

    private String readLine() {
        String input = reader.readLine();
        validate(input);
        return input;
    }

    private static void printReInput(IllegalArgumentException ex) {
        System.out.flush();
        System.out.println(ex.getMessage());
    }

    private static void validate(String input) {
        if (input.isBlank()) {
            throw new IllegalArgumentException(ErrorCode.BLANK_INPUT_MESSAGE.getMessage());
        }
    }

    private <T> T retryInput(String prompt, Supplier<T> inputSupplier) {
        System.out.println(prompt);
        while (true) {
            try {
                T result = inputSupplier.get();
                System.out.println();
                return result;
            } catch (IllegalArgumentException ex) {
                printReInput(ex);
            }
        }
    }


    public PurchaseProducts inputPurchaseProducts(Stocks stocks) {
        return retryInput("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])",
                () -> purchaseProductService.checkFromStockQuantity(stocks, PurchaseProducts.from(readLine())));
    }

    public static boolean isContinue() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");

        return inputYesOrNo();
    }

    public static boolean isMembership(){
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");

        return inputYesOrNo();
    }

    public static boolean printPromotionQuantityNotEnough(String productName, int quantity) {
        System.out.println("현재 "+ productName+" " + quantity +"개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");

        return inputYesOrNo();
    }

    public static boolean printPromotionQuantityMoreEnough(String productName, int plusQuantity) {
        System.out.println("현재 "+ productName+"은(는) " + plusQuantity +"개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");

        return inputYesOrNo();
    }

    private static boolean inputYesOrNo() {

        while (true) {
            String input = Console.readLine();
            if (input.equals("y") || input.equals("Y")) {
                return true;
            }
            if (input.equals("n") || input.equals("N")) {
                return false;
            }
            System.out.println("Y 또는 N을 입력해주세요.");
        }
    }


    @Override
    public void close() {
        reader.close();
    }
}
