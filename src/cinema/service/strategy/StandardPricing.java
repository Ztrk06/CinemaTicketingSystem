package cinema.service.strategy;

public class StandardPricing implements IPricingStrategy {
    @Override
    public double calculatePrice(double basePrice) {
        return basePrice;
    }
}