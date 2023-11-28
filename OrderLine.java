
public class OrderLine {
    
    private int orderLineID;
    private Product[] products;
    private int quantity;
    private double lineCost;
    
    /**
     * @param orderLineID
     * @param products
     * @param quantity
     * @param lineCost
     */
    public OrderLine(int orderLineID, Product[] products, int quantity, double lineCost) {
        this.orderLineID = orderLineID;
        this.products = products;
        this.quantity = quantity;
        this.lineCost = lineCost;
    }
    
}
