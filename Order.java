import java.util.Date;

public class Order {
    
    private int orderID;
    private OrderStatus orderStatus;
    private int productID;

    /**
     * @param orderID
     * @param orderStatus
     * @param productID
     */
    public Order(int orderID, OrderStatus orderStatus, int productID) {
        this.orderID = orderID;
        this.orderStatus = orderStatus;
        this.productID = productID;
    }
    
    
}
