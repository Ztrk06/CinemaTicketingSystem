package cinema.service;
import cinema.domain.CreditCard;
import java.util.List;

public interface ICreditCardRepository {
    CreditCard findByCardNumber(String cardNumber);
    void update(CreditCard creditCard);
    void save(CreditCard creditCard);
}