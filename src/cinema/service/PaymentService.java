package cinema.service;

import cinema.domain.CreditCard;

/**
 * Service responsible for processing payments and managing credit card information.
 */
public class PaymentService {
    private final ICreditCardRepository cardRepository;

    public PaymentService(ICreditCardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    /**
     * Processes a payment by verifying the card details and checking for sufficient balance.
     * If successful, the amount is deducted and the repository is updated.
     *
     * @param cardNumber The 16-digit credit card number.
     * @param amount     The amount to be charged.
     * @return True if the payment is successful, false otherwise.
     */
    public boolean processPayment(String cardNumber, double amount) {
        System.out.println("Connecting to the bank...");

        CreditCard card = cardRepository.findByCardNumber(cardNumber);

        if (card == null) {
            System.out.println("ERROR: No such credit card found in the system!");
            return false;
        }

        System.out.println("Card Owner: " + card.getOwnerName() + " (" + card.getBankName() + ")");

        if (card.getBalance() < amount) {
            System.out.println("ERROR: Insufficient balance on your card! (Current Balance: " + card.getBalance() + " TL)");
            return false;
        }

        card.setBalance(card.getBalance() - amount);
        cardRepository.update(card);

        System.out.println("Payment Successful! " + amount + " TL charged. Remaining Balance: " + card.getBalance() + " TL");
        return true;
    }

    /**
     * Adds a new credit card to the system's repository.
     *
     * @param card The credit card object to be saved.
     */
    public void addCreditCard(CreditCard card) {
        cardRepository.save(card);
        System.out.println("SUCCESS: Your " + card.getBankName() + " card has been added to the system!");
    }
}