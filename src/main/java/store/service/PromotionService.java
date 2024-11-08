package store.service;

import java.util.List;
import store.domain.Promotion;
import store.repository.PromotionRepository;

public class PromotionService {

    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAllPromotion();
    }
}
