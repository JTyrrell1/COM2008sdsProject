public class User {
    
    private int userID;
    private String email;
    private String forename;
    private String surname;
    private int addressID;
    private int bankID;
    private Order[] orders;
    private UserType userType;
    
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
            int bankID, Order[] orders, UserType userType) {
        this.userID = userID;
        this.email = email;
        this.forename = forename;
        this.surname = surname;
        this.addressID = addressID;
        this.bankID = bankID;
        this.orders = orders;
        this.userType = userType;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public int getBankID() {
        return bankID;
    }

    public void setBankID(int bankID) {
        this.bankID = bankID;
    }

    public Order[] getOrders() {
        return orders;
    }

    public void setOrders(Order[] orders) {
        this.orders = orders;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    
    
}
