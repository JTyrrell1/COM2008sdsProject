import java.util.Date;

public class BankAccount {

    private int accountNumber;
    private int civ;
    private int expiryDate;
    private String firstName;
    private String lastName;
    
    /**
     * @param accountNumber
     * @param civ
     * @param expiryDate
     * @param firstName
     * @param lastName
     */
    public BankAccount(int accountNumber, int civ, int expiryDate, String firstName, String lastName) {
        this.accountNumber = accountNumber;
        this.civ = civ;
        this.expiryDate = expiryDate;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return
                "Account Number: " + accountNumber + "\n" +
                "Security Number: " + civ + '\n' +
                "Expiry Date: " + expiryDate + "\n" +
                "Name: " + firstName + ' ' + lastName;
    }
}
