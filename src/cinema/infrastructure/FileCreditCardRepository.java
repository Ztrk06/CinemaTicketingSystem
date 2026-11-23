package cinema.infrastructure;

import cinema.domain.CreditCard;
import cinema.service.ICreditCardRepository;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * File-based implementation of the Credit Card Repository.
 * Handles persistent storage of credit card data using a CSV file.
 */
public class FileCreditCardRepository implements ICreditCardRepository {
    private final String filePath = "data/cards.csv";

    public FileCreditCardRepository() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                if (file.getParentFile() != null) file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves all credit cards from the CSV file.
     *
     * @return A list of all stored credit cards.
     */
    public List<CreditCard> findAll() {
        List<CreditCard> cards = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    cards.add(new CreditCard(parts[0], parts[1], parts[2], Double.parseDouble(parts[3])));
                }
            }
        } catch (IOException e) {
            // Intentionally left empty or could be logged
        }
        return cards;
    }

    /**
     * Finds a specific credit card by its card number.
     *
     * @param cardNumber The card number to search for.
     * @return The CreditCard object if found, otherwise null.
     */
    @Override
    public CreditCard findByCardNumber(String cardNumber) {
        for (CreditCard c : findAll()) {
            if (c.getCardNumber().equals(cardNumber)) return c;
        }
        return null;
    }

    /**
     * Updates an existing credit card's information in the file.
     * Reads all records, applies the update to the matching card, and rewrites the entire file.
     *
     * @param creditCard The credit card containing the updated information.
     */
    @Override
    public void update(CreditCard creditCard) {
        List<CreditCard> cards = findAll();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (CreditCard c : cards) {
                if (c.getCardNumber().equals(creditCard.getCardNumber())) {
                    bw.write(creditCard.toCSV());
                } else {
                    bw.write(c.toCSV());
                }
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves a new credit card by appending it to the CSV file.
     *
     * @param creditCard The new credit card to save.
     */
    @Override
    public void save(CreditCard creditCard) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(creditCard.toCSV());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}