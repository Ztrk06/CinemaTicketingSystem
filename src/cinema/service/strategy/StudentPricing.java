package cinema.service.strategy;

public class StudentPricing implements IPricingStrategy {
    @Override
    public double calculatePrice(double basePrice) {
        return basePrice * 0.8; // %20 discount
    }
}