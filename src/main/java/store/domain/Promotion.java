package store.domain;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;

public class Promotion {
    private String name;
    private int buyCount;
    private int getCount;
    private LocalDate startDate;
    private LocalDate endDate;

    public Promotion(String name, int buyCount, int getCount, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buyCount = buyCount;
        this.getCount = getCount;
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
}
