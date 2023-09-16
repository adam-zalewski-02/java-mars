package be.howest.ti.mars.logic.domain;


public class Tier {
    private int id;
    private final String name;
    private final int monthlyLimitInkg;
    private final double additionalDisposalPerKg;
    private final int freePickUps;

    private final double pickupFeePerKg;
    private final double paymentPerMonth;



    public Tier(String name, int monthlyLimitInkg, double additionalDisposalPerKg, int freePickUps, double pickupFeePerKg, double paymentPerMonth) {
        this.name = name;
        this.monthlyLimitInkg = monthlyLimitInkg;
        this.additionalDisposalPerKg = additionalDisposalPerKg;
        this.freePickUps = freePickUps;
        this.pickupFeePerKg = pickupFeePerKg;
        this.paymentPerMonth = paymentPerMonth;
    }

    public Tier(int id, String name, int monthlyLimitInkg, double additionalDisposalPerKg, int freePickUps, double pickupFeePerKg, double paymentPerMonth) {
        this(name, monthlyLimitInkg, additionalDisposalPerKg, freePickUps, pickupFeePerKg, paymentPerMonth);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMonthlyLimitInkg() {
        return monthlyLimitInkg;
    }

    public double getAdditionalDisposalPerKg() {
        return additionalDisposalPerKg;
    }

    public int getFreePickUps() {
        return freePickUps;
    }

    public double getPickupFeePerKg() {
        return pickupFeePerKg;
    }

    public double getPaymentPerMonth() {
        return paymentPerMonth;
    }

    public void setId(int id) {
        this.id = id;
    }
}
