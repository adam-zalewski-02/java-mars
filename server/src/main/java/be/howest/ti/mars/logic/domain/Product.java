package be.howest.ti.mars.logic.domain;

public class Product {
    private int id;
    private String name;
    private double pricePerKg;
    private double amountInStockInKg;

    public Product(String name, double pricePerKg, double amountInStockInKg) {
        this.name = name;
        this.pricePerKg = pricePerKg;
        this.amountInStockInKg = amountInStockInKg;
    }
    public Product(int id, String name, double pricePerKg, double amountInStockInKg) {
        this(name, pricePerKg, amountInStockInKg);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPricePerKg() {
        return pricePerKg;
    }

    public double getAmountInStockInKg() {
        return amountInStockInKg;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void substractStock(double amount) {
        this.amountInStockInKg -= amount;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pricePerKg=" + pricePerKg +
                ", amountInStockInKg=" + amountInStockInKg +
                '}';
    }
}
