package org.rituraj;

public class InvoiceSummary {
    private final int totalRides;
    private final double totalFare;
    private final double averageFare;

    public InvoiceSummary(int totalRides, double totalFare) {
        this.totalRides = totalRides;
        this.totalFare = totalFare;
        this.averageFare = totalFare / totalRides;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof InvoiceSummary)) return false;
        InvoiceSummary other = (InvoiceSummary) obj;
        return this.totalRides == other.totalRides &&
                Double.compare(this.totalFare, other.totalFare) == 0 &&
                Double.compare(this.averageFare, other.averageFare) == 0;
    }
}
