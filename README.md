# java-convenience-store-precourse

## 기능 요구 사항
구매자의 할인 혜택과 재고 상황을 고려하여 최종 결제 금액을 계산하고 안내하는 결제 시스템을 구현한다.

- 사용자가 입력한 상품의 가격과 수량을 기반으로 최종 결제 금액을 계산한다.
  - 총구매액은 상품별 가격과 수량을 곱하여 계산하며, 프로모션 및 멤버십 할인 정책을 반영하여 최종 결제 금액을 산출한다.
- 구매 내역과 산출한 금액 정보를 영수증으로 출력한다.
- 영수증 출력 후 추가 구매를 진행할지 또는 종료할지를 선택할 수 있다.
- 사용자가 잘못된 값을 입력할 경우 IllegalArgumentException를 발생시키고, "[ERROR]"로 시작하는 에러 메시지를 출력 후 그 부분부터 입력을 다시 받는다.
  - Exception이 아닌 IllegalArgumentException, IllegalStateException 등과 같은 명확한 유형을 처리한다.
### 재고 관리
- 각 상품의 재고 수량을 고려하여 결제 가능 여부를 확인한다.
- 고객이 상품을 구매할 때마다, 결제된 수량만큼 해당 상품의 재고에서 차감하여 수량을 관리한다.
- 재고를 차감함으로써 시스템은 최신 재고 상태를 유지하며, 다음 고객이 구매할 때 정확한 재고 정보를 제공한다.
### 프로모션 할인
- 오늘 날짜가 프로모션 기간 내에 포함된 경우에만 할인을 적용한다.
- 프로모션은 N개 구매 시 1개 무료 증정(Buy N Get 1 Free)의 형태로 진행된다.
- 1+1 또는 2+1 프로모션이 각각 지정된 상품에 적용되며, 동일 상품에 여러 프로모션이 적용되지 않는다.
- 프로모션 혜택은 프로모션 재고 내에서만 적용할 수 있다.
- 프로모션 기간 중이라면 프로모션 재고를 우선적으로 차감하며, 프로모션 재고가 부족할 경우에는 일반 재고를 사용한다.
- 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 필요한 수량을 추가로 가져오면 혜택을 받을 수 있음을 안내한다.
- 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제하게 됨을 안내한다.
### 멤버십 할인
- 멤버십 회원은 프로모션 미적용 금액의 30%를 할인받는다.
- 프로모션 적용 후 남은 금액에 대해 멤버십 할인을 적용한다.
- 멤버십 할인의 최대 한도는 8,000원이다.
### 영수증 출력
- 영수증은 고객의 구매 내역과 할인을 요약하여 출력한다.
- 영수증 항목은 아래와 같다.
  - 구매 상품 내역: 구매한 상품명, 수량, 가격
  - 증정 상품 내역: 프로모션에 따라 무료로 제공된 증정 상품의 목록
  - 금액 정보
    - 총구매액: 구매한 상품의 총 수량과 총 금액
    - 행사할인: 프로모션에 의해 할인된 금액
    - 멤버십할인: 멤버십에 의해 추가로 할인된 금액
    - 내실돈: 최종 결제 금액
    - 영수증의 구성 요소를 보기 좋게 정렬하여 고객이 쉽게 금액과 수량을 확인할 수 있게 한다.

## 기능 목록
### 1. 상품 재고 수량 읽어오기
- products.md에서 상품 재고 수량을 읽어온다.
  - 상품 가격은 0 이상의 정수 범위여야한다.
  - 상품 수량은 0 이상의 정수 범위여야한다.

### 2. 재고 수량 출력하기
- 재고 수량을 출력한다.
  - 재고 수량이 0개일 시 수량을 '재고 없음'으로 출력한다.
  - 프로모션이 없을 시 프로모션은 출력하지 않는다.
```
안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 10개 탄산2+1
- 콜라 1,000원 10개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 5개
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개

구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
```

### 3. 구매 상품, 수량 입력받기
- 구매 상품과 수량을 입력받는다.
  - 입력값이 빈 문자열인지 검사하고 빈 값이라면 예외를 터트린다.
    - [ERROR] 잘못된 입력입니다. 다시 입력해 주세요.
- ',' 로 문자열을 구분한다.
- 개별 상품이 '[사이다-2]'으로 상품명과 수량이 구성되었는지 검사한다.
  - 나눈 문자열의 앞 뒤에서 []를 분리한다.
  - 앞 뒤가 []가 아니라면 예외를 터트린다.
    - [ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.
- '상품명-수량'을 '-' 구분자로 분리한다.
  - '-'로 분리할 수 없다면 예외를 터트린다.
  - [ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.
- 구매 상품을 검사한다.
  - 구매 상품이 빈 문자열인지 검사하고 빈 값이라면 예외를 터트린다.
    - [ERROR] 잘못된 입력입니다. 다시 입력해 주세요.
- 수량을 검사한다.
  - 수량이 빈 문자열인지 검사하고 빈 값이라면 예외를 터트린다.
  - 수량이 정수 범위의 숫자인지 검사하고 숫자가 아니라면 예외를 터트린다.
  - 수량이 1개 이상인지 검사하고 아니라면 예외를 터트린다.
    - [ERROR] 잘못된 입력입니다. 다시 입력해 주세요.

### 4. 구매 상품과 수량 재고 확인하기
- 입력받은 구매 상품과 수량의 재고가 있는지 확인한다.
  - 상품이 존재하지 않으면 예외를 터트린다.
    - [ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.
  - 상품이 존재하나 재고를 초과할 시 예외를 터트린다.
    - [ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.

### 5. 프로모션 적용 대상 상품인지 판단하기
- 구입 상품이 프로모션 상품인지 확인한다.
- 오늘 날짜가 프로모션 기간에 속하는지 확인한다.
- 프로모션 기간에 속한다면 프로모션 재고를 우선적으로 차감하며, 프로모션 재고가 부족할 경우에는 프로모션 적용 후 남은 수량에 대해 일반 재고를 사용한다.
  - 프로모션 적용 수량만큼 프로모션 재고가 있는지 확인한다.
    - 재고가 있다면 프로모션 수량만큼 구매했는지 확인한다.
      - 프로모션 수량에 맞게 구매했다면 정상 처리한다.
        - 프로모션은 N개 구매 시 1개 무료 증정(Buy N Get 1 Free)의 형태로 진행된다.
        - 1+1 또는 2+1 프로모션이 각각 지정된 상품에 적용되며, 동일 상품에 여러 프로모션이 적용되지 않는다.
      - 프로모션 수량에 나눠 떨어지지 않는다면 필요한 수량을 추가로 가져오면 혜택을 받을 수 있음을 안내한다.
        - 현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
        - Y: 증정 받을 수 있는 상품을 추가한다. 
        - N: 증정 받을 수 있는 상품을 추가하지 않는다.
    - 재고가 없다면 프로모션 혜택없이, 일부 수량에 대해 정가로 결제하게 됨을 안내한다.
      - 현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
      - Y: 일부 수량에 대해 정가로 결제한다. 
      - N: 정가로 결제해야하는 수량만큼 제외한 후 결제를 진행한다.
  - 프로모션 기간에 속하지 않는다면, 일반 재고를 사용한다.


### 6. 프로모션 이벤트 관련 Y/N 입력을 받는다.
- 프로모션 이벤트와 관련된 입력 Y/N을 받는다.
  - 입력 값이 'Y','N','y','n' 중에 없다면 예외를 터트린다.
    - [ERROR] 잘못된 입력입니다. 다시 입력해 주세요.

### 7. 멤버십 할인 적용 여부를 체크한다.
- 프로모션 이벤트와 관련된 입력 Y/N을 받는다.
  - Y: 멤버십 할인을 적용한다.
    - 멤버십 회원은 프로모션 '미적용' 상품 금액의 30%를 할인받는다.
    - 프로모션 적용 후 남은 금액에 대해 멤버십 할인을 적용한다. 
    - 멤버십 할인의 최대 한도는 8,000원이다.
  - N: 멤버십 할인을 적용하지 않는다.
  - 입력 값이 'Y','N','y','n' 중에 없다면 예외를 터트린다.
    - [ERROR] 잘못된 입력입니다. 다시 입력해 주세요.

### 8. 구매 상품 만큼 재고를 차감시킨다.
- 시스템에 구매 수량 만큼 수량을 감소시킨다.
  - '시스템 상의 남아있는 수량 - 감소 시키는 수량'이 음수가 된다면 예외를 터트린다.

### 8. 구매 상품 내역, 증정 상품 내역, 금액 정보를 출력한다.
- 구매 상품과 금액을 출력한다.
- 증정품을 출력한다.
- 총 구매액, 행사할인, 멤버십할인, 내실돈을 출력한다.
  - 행사할인은 증정품 가격의 합산 값이다.
```
==============W 편의점================
상품명		수량	    금액
콜라		    3 	    3,000
에너지바 		5 	    10,000
=============증	정===============
콜라		    1
====================================
총구매액		8	    13,000
행사할인			    -1,000
멤버십할인			    -3,000
내실돈			     9,000
```

### 9. 추가 구매 여부 확인
- 추가 구매 여부를 확인하기 위해 안내 문구를 출력한다.
  - 감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
- 추가 구매한다면 프로그램을 종료하지않고 다시 반복한다.
- 추가 구매하지 않는다면 프로그램을 종료한다.