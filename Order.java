import java.util.Date;

public class Order {
    
    private int orderNumber;
    private Date orderDate;
    private double orderCost;
    private OrderStatus orderStatus;
    private OrderLine[] orderLines;
    
    /**
     * @param orderNumber
     * @param orderDate
     * @param orderCost
     * @param orderStatus
     */
    public Order(int orderNumber, Date orderDate, double orderCost, OrderStatus orderStatus) {
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.orderCost = orderCost;
        this.orderStatus = orderStatus;
    }
    
    
}
