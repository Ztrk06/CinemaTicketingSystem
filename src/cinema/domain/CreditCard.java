package cinema.domain;

public class CreditCard {
    private String cardNumber;
    private String ownerName;
    private String bankName;
    private double balance;

    public CreditCard(String cardNumber, String ownerName, String bankName, double balance) {
        this.cardNumber = cardNumber;
        this.ownerName = ownerName;
        this.bankName = bankName;
        this.balance = balance;
    }

    public String getCardNumber() { return cardNumber; }
    public String getOwnerName() { return ownerName; }
    public String getBankName() { return bankName; }
    public double getBalance() { return balance; }

    public void setBalance(double balance) { this.balance = balance; }

    public String toCSV() {
        return cardNumber + "," + ownerName + "," + bankName + "," + balance;
    }
}