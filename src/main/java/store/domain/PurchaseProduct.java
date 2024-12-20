package store.domain;

import store.error.ErrorCode;

public class PurchaseProduct {

    public static final int VALID_INPUT_SPLIT_COUNT = 2;
    private String name;
    private int quantity;
    private boolean isPromotionProduct;
    private int promotionCount;
    private int generalCount;
    private int giftCount;

    private PurchaseProduct(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
        this.isPromotionProduct = false;
    }

    public static PurchaseProduct from(String productInput) {
        String tempInput = parseToNameAndQuantity(productInput);
        String[] splitInput = tempInput.split("-");
        validInputCount(splitInput);
        validateName(splitInput);
        return new PurchaseProduct(splitInput[0], parseInt(splitInput[1]));
    }

    public int getPromotionCount() {
        return promotionCount;
    }

    public int getGeneralCount() {
        return generalCount;
    }

    public void setGiftCount(int giftCount) {
        this.giftCount = giftCount;
    }

    public int getGiftCount() {
        return giftCount;
    }

    public void setPromotionCount(int promotionCount) {
        this.promotionCount = promotionCount;
    }

    public void setGeneralCount(int generalCount) {
        this.generalCount = generalCount;
    }

    public void plusQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void minusQuantity(int quantity) {
        this.quantity -= quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isPromotionProduct() {
        return isPromotionProduct;
    }

    public void setIsPromotionTrue() {
        isPromotionProduct = true;
    }

    private static void validateName(String[] splitInput) {
        if(splitInput[0].isBlank()){
            throw new IllegalArgumentException(ErrorCode.INVALID_INPUT_FORMAT.getMessage());
        }
    }

    private static void validInputCount(String[] splitInput) {
        if (splitInput.length != VALID_INPUT_SPLIT_COUNT) {
            throw new IllegalArgumentException(ErrorCode.INVALID_INPUT_FORMAT.getMessage());
        }
    }

    private static String parseToNameAndQuantity(String productInput) {
        if(productInput.isBlank())
            throw new IllegalArgumentException(ErrorCode.INVALID_INPUT_FORMAT.getMessage());
        if (productInput.charAt(0) != '[' || productInput.charAt(productInput.length() - 1) != ']') {
            throw new IllegalArgumentException(ErrorCode.INVALID_INPUT_FORMAT.getMessage());
        }
        return productInput.replace("[", "").replace("]", "");
    }

    private static int parseInt(String value){
        try{
            int intQuantity = Integer.parseInt(value);
            if(intQuantity <= 0)
                throw new IllegalArgumentException(ErrorCode.INVALID_INPUT_FORMAT.getMessage());
            return intQuantity;
        } catch (NumberFormatException e){
            throw new IllegalArgumentException(ErrorCode.FILE_VALUE_TYPE_ERROR.getMessage());
        }
    }
}
