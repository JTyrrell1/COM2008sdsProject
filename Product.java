
public class Product {
    
    private String productName;
    private String brandName;
    private String productCode;
    private double price;
    private ModellingScale modellingScale;
    private int stockNumber;
    
    /**
     * @param productName
     * @param brandName
     * @param productCode
     * @param price
     * @param modellingScale
     * @param stockNumber
     */
    public Product(String productName, String brandName, String productCode, double price,
            ModellingScale modellingScale, int stockNumber) {
        this.productName = productName;
        this.brandName = brandName;
        this.productCode = productCode;
        this.price = price;
        this.modellingScale = modellingScale;
        this.stockNumber = stockNumber;
    }
    
    

}
