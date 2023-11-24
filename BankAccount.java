import java.util.Date;

public class BankAccount {
    
    private String cardName;
    private int cardNumber;
    private Date expiryDate;
    private int securityCode;
    private String cardHolderName;
    
    /**
     * @param cardName
     * @param cardNumber
     * @param expiryDate
     * @param securityCode
     * @param cardHolderName
     */
    public BankAccount(String cardName, int cardNumber, Date expiryDate, int securityCode, String cardHolderName) {
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.securityCode = securityCode;
        this.cardHolderName = cardHolderName;
    }
    
}
