package store.repository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import store.domain.Promotion;
import store.error.ErrorCode;

public class PromotionMarkDownFileRepository implements PromotionRepository {

    public static final String PROMOTIONS_MD_FILE = "src/main/resources/promotions.md";

    @Override
    public List<Promotion> findAllPromotion() {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(PROMOTIONS_MD_FILE)
        )) {
            String textLine;
            String header = reader.readLine();
            List<Promotion> promotions = new ArrayList<>();
            while ((textLine = reader.readLine()) != null) {

                List<String> splitTextLine = Arrays.stream(textLine.split(",",-1))
                        .map(this::getValueOrNull)
                        .toList();

                addPromotions(splitTextLine, promotions);
            }
            return promotions;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(ErrorCode.File_NOT_FOUND.getMessage() + PROMOTIONS_MD_FILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Promotion> findPromotionByName(String name) {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(PROMOTIONS_MD_FILE)
        )) {
            String textLine;
            String header = reader.readLine();
            while ((textLine = reader.readLine()) != null) {

                List<String> splitTextLine = Arrays.stream(textLine.split(",",-1))
                        .map(this::getValueOrNull)
                        .toList();

                if(splitTextLine.get(0).equals(name))
                    return Optional.of(toPromotions(splitTextLine));
            }
            return Optional.empty();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(ErrorCode.File_NOT_FOUND.getMessage() + PROMOTIONS_MD_FILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Promotion toPromotions(List<String> splitTextLine) {
        String name = splitTextLine.get(0);
        int buy = parseInt(splitTextLine.get(1));
        int get = parseInt(splitTextLine.get(2));
        LocalDate startDate = parseLocalDate(splitTextLine.get(3));
        LocalDate endDate = parseLocalDate(splitTextLine.get(4));

        return new Promotion(name, buy, get, startDate, endDate);
    }

    private void addPromotions(List<String> splitTextLine, List<Promotion> promotions) {
        String name = splitTextLine.get(0);
        int buy = parseInt(splitTextLine.get(1));
        int get = parseInt(splitTextLine.get(2));
        LocalDate startDate = parseLocalDate(splitTextLine.get(3));
        LocalDate endDate = parseLocalDate(splitTextLine.get(4));

        promotions.add(new Promotion(name, buy, get, startDate, endDate));
    }

    private LocalDate parseLocalDate(String date){
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private Integer parseInt(String value){
        try{
            int intPrice = Integer.parseInt(value);
            if(intPrice <= 0)
                throw new IllegalArgumentException(ErrorCode.PROMOTION_COUNT_NOT_POSITIVE_NUMBERS.getMessage());
            return intPrice;
        } catch (NumberFormatException e){
            throw new IllegalArgumentException(ErrorCode.FILE_VALUE_TYPE_ERROR.getMessage());
        }
    }

    private String getValueOrNull(String value){
        if(value.equals("null") || value.isBlank())
            return null;
        return value;
    }

}
