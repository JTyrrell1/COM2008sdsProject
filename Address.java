public class Address {
    
    private String houseNumber;
    private String roadName;
    private String cityName;
    private String postcode;

    /**
     * @param houseNumber
     * @param roadName
     * @param cityName
     * @param postcode
     */
    public Address(String houseNumber, String roadName, String cityName, String postcode) {
        this.houseNumber = houseNumber;
        this.roadName = roadName;
        this.cityName = cityName;
        this.postcode = postcode;
    }

    @Override
    public String toString() {
        return
                "House Number: " + houseNumber + '\n' +
                "Road Name: " + roadName + '\n' +
                "City Name: " + cityName + '\n' +
                "Postcode: " + postcode;
    }
}
