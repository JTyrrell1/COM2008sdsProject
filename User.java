public class User {
    
    private int userID;
    private String email;
    private String password;
    private String forename;
    private String surname;
    private int addressID;
    private int bankID;
    private Order[] orders;
    private String userType;
    
    /**
     * @param userID
     * @param email
     * @param forename
     * @param surname
     * @param addressID
     * @param bankID
     * @param orders
     * @param userType
     */
    public User(int userID, String email, String forename, String surname, int addressID,
            int bankID, Order[] orders, String userType) {
        this.userID = userID;
        this.email = email;
        this.forename = forename;
        this.surname = surname;
        this.addressID = addressID;
        this.bankID = bankID;
        this.orders = orders;
        this.userType = userType;
    }
    
    
}
