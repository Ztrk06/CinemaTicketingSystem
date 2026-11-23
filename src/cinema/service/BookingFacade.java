package cinema.service;

import cinema.domain.Ticket;
import cinema.service.strategy.IPricingStrategy;

/**
 * Facade class that simplifies the booking and payment process.
 * Acts as an intermediary coordinating TicketService and PaymentService.
 */
public class BookingFacade {
    private final TicketService ticketService;
    private final PaymentService paymentService;

    public BookingFacade(TicketService ticketService, PaymentService paymentService) {
        this.ticketService = ticketService;
        this.paymentService = paymentService;
    }

    /**
     * Processes the payment for a ticket using the specified pricing strategy.
     * Confirms the booking if the payment is successful.
     *
     * @param ticket          The ticket being purchased.
     * @param pricingStrategy The strategy used to calculate the final price (e.g., Student, Standard).
     * @param basePrice       The base price of the seat for the session.
     * @param cardNumber      The credit card number used for the transaction.
     */
    public void completePayment(Ticket ticket, IPricingStrategy pricingStrategy, double basePrice, String cardNumber) {
        System.out.println("\n--- Payment Process Starting ---");
        double finalPrice = pricingStrategy.calculatePrice(basePrice);
        System.out.println("Calculated ticket price: " + finalPrice + " TL");

        boolean isPaid = paymentService.processPayment(cardNumber, finalPrice);

        if (isPaid) {
            ticketService.confirmBooking(ticket);
            System.out.println("Transaction completed. Your ticket is ready!");
        } else {
            System.out.println("Payment failed. Transaction is being canceled.");
        }
        System.out.println("----------------------------------------\n");
    }
}