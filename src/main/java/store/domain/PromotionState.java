package store.domain;

public enum PromotionState {

    MORE_PROMOTION("추가 상품 지급 가능한 상태"),
    LACK_PROMOTION("프로모션 재고 부족한 상태"),
    FIT_PROMOTION("프로모션 개수에 맞게 구매한 상태"),

    ;
    private String value;

    PromotionState(String value) {
        this.value = value;
    }
}
