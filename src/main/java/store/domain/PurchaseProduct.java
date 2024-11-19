package store.domain;

import store.error.ErrorCode;

public class PurchaseProduct {

    public static final int VALID_INPUT_SPLIT_COUNT = 2;
    private final String name;
    private int quantity;
    private boolean isPromotionProduct;
    private int promotionCount;
    private int generalCount;
    private int giftCount;
    private PromotionState promotionState;

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

    public void checkPromotionStock(Product promotionProduct){
        Promotion promotion = promotionProduct.getPromotion();

        int 프로모션할_개수 = quantity - (quantity % promotion.getPromotionBundleCount());
        int 프로모션하고_하고_남은거개수 = quantity % promotion.getPromotionBundleCount();
        int 프로모션_가능_개수 = promotionProduct.getAvailablePromotionCount();

        //프로모션할 개수가 가능한 재고보다 많을 때
        if(프로모션_가능_개수 < 프로모션할_개수){
            initialCount(프로모션_가능_개수, 프로모션하고_하고_남은거개수 + 프로모션할_개수 - 프로모션_가능_개수,
                    PromotionState.LACK_PROMOTION, promotion);
            return;
        }
        //이제부터 프로모션 가능 개수가 더 큼
        //프로모션 가능한데 적게 가져왔을 때
        if(프로모션하고_하고_남은거개수 == promotion.getBuyCount()){
            //프로모션 가능한데 적게 가져왔을 때 추가하면 가능한 재고보다 많아질 때
            if(프로모션_가능_개수 < 프로모션할_개수 + promotion.getPromotionBundleCount()){
                initialCount(프로모션할_개수, 프로모션할_개수, PromotionState.LACK_PROMOTION, promotion);
                return;
            }
            //프로모션 가능한데 적게 가져왔을 때 추가해도 괜찮을 때
            initialCount( 프로모션할_개수 + promotion.getPromotionBundleCount(), 0, PromotionState.MORE_PROMOTION, promotion);
            quantity += promotion.getPlusCount();
            return;
        }

        //딱 프로모션 개수에 맞아 떨어질때
        if(프로모션하고_하고_남은거개수 == 0){
            initialCount(프로모션할_개수, 0, PromotionState.FIT_PROMOTION, promotion);
            return;
        }
        //프로모션하고 남는 것이 프로모션 대상 개수 범위에도 안맞을 때
        initialCount(프로모션할_개수, 프로모션하고_하고_남은거개수, PromotionState.LACK_PROMOTION, promotion);
    }

    private void initialCount(int promotionCount, int generalCount, PromotionState promotionState, Promotion promotion) {
        this.promotionCount = promotionCount;
        this.generalCount = generalCount;
        this.promotionState = promotionState;
        this.giftCount = promotionCount / promotion.getPromotionBundleCount();
    }

    public void checkGeneralStock(Product generalProduct) {
        if(isPromotionProduct){
            if(generalCount > generalProduct.getQuantity())
                throw new IllegalArgumentException(ErrorCode.OVER_QUANTITY.getMessage());
            return;
        }
        if(generalCount == 0)
            return;

        if(generalProduct == null && generalCount > 0)
            throw new IllegalArgumentException(ErrorCode.OVER_QUANTITY.getMessage());

        if(quantity > generalProduct.getQuantity())
            throw new IllegalArgumentException(ErrorCode.OVER_QUANTITY.getMessage());
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

    public void notGetMorePromotion(){
        promotionCount = quantity;
    }

    public void notGetLackPromotion(){
        generalCount = 0;
    }

    public int getPromotionCount() {
        return promotionCount;
    }

    public int getGeneralCount() {
        return generalCount;
    }

    public int getGiftCount() {
        return giftCount;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public PromotionState getPromotionState() {
        return promotionState;
    }

    public boolean isPromotionProduct() {
        return isPromotionProduct;
    }

    public void setIsPromotionTrue() {
        isPromotionProduct = true;
    }
}
