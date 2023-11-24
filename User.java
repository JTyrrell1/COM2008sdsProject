public class User {
    
    private String userID;
    private String email;
    private String password;
    private String forename;
    private String surname;
    private Address address;
    private BankAccount bankAccount;
    private Order[] orders;
    
    /**
     * @param userID
     * @param email
     * @param password
     * @param forename
     * @param surname
     * @param address
     * @param bankAccount
     * @param orders
     */
    public User(String userID, String email, String password, String forename, String surname, Address address,
            BankAccount bankAccount, Order[] orders) {
        this.userID = userID;
        this.email = email;
        this.password = password;
        this.forename = forename;
        this.surname = surname;
        this.address = address;
        this.bankAccount = bankAccount;
        this.orders = orders;
    }
    
    
}
