package be.howest.ti.mars.logic.domain;


public class Customer {
    private int id;
    private String name;
    private String address;
    private Tier tier;
    private double amountLeftToDispose;
    private double amountOfGarbageDisposed;
    private int pickupsLeft;

    public Customer(String name, String address) {
        this.name = name;
        this.address = address;
        this.amountOfGarbageDisposed = 0;
        amountLeftToDispose = 0;
        pickupsLeft = 0;
    }

    public Customer(int id, String name, String address, Tier tier, double amountOfGarbageDisposed, double amountLeftToDispose, int pickupsLeft) {
        this(name, address);
        this.id = id;
        this.tier = tier;
        this.amountOfGarbageDisposed = amountOfGarbageDisposed;
        this.amountLeftToDispose = amountLeftToDispose;
        this.pickupsLeft = pickupsLeft;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Tier getTier() {
        return tier;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTier(Tier tier) {
        this.tier = tier;
    }

    public double getAmountOfGarbageDisposed() {
        return amountOfGarbageDisposed;
    }

    public void addGarbage(double amount) {
        if (checkIfSubscribed() && this.amountLeftToDispose > this.amountOfGarbageDisposed) {
            this.amountOfGarbageDisposed += amount;
            this.amountLeftToDispose = this.tier.getMonthlyLimitInkg() - this.amountOfGarbageDisposed;
        } else throw new IllegalArgumentException("There are no disposals left");
    }

    public double getAmountLeftToDispose() {
        return amountLeftToDispose;
    }

    public int getPickupsLeft() {
        return pickupsLeft;
    }

    public void reduceAmountOfFreePickupsLeft() {
        if (checkIfSubscribed() && this.pickupsLeft > 0) {
            this.pickupsLeft--;
        } else throw new IllegalArgumentException("There are no pickups left, you will get billed for your pickup");
    }

    private boolean checkIfSubscribed() {
        return this.tier.getId() != 0;
    }
}
