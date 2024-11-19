package store.domain;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;

public class Promotion {
    private String name;
    private int buyCount;
    private int plusCount;
    private LocalDate startDate;
    private LocalDate endDate;

    public Promotion(String name, int buyCount, int plusCount, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buyCount = buyCount;
        this.plusCount = plusCount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public boolean isIncludeDateRange() {
        LocalDate now = DateTimes.now().toLocalDate();
        return startDate.isBefore(now) && endDate.isAfter(now);
    }

    public boolean isEqualsName(String name) {
        return this.name.equals(name);
    }

    public int getPlusCount() {
        return plusCount;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public int getPromotionBundleCount() {
        return buyCount + plusCount;
    }
}
