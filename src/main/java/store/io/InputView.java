package store.io;

import java.util.function.Supplier;
import store.config.AppConfig;
import store.domain.PurchaseProducts;
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
        System.out.println("다시 입력하세요.");
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

    public void inputPurchaseProducts() {
        retryInput("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])",
                () -> purchaseProductService.updateStockQuantity(PurchaseProducts.from(readLine())));
    }

    @Override
    public void close() {
        reader.close();
    }
}
