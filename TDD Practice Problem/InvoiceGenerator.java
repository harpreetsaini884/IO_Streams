package org.rituraj;

public class InvoiceGenerator {
    public InvoiceGenerator(){}
    private static final double COST_PER_KM = 10;
    private static final int COST_PER_MINUTE = 1;
    private static final double MINIMUM_FARE = 5;

    private static final double NORMAL_COST_PER_KM = 10;
    private static final int NORMAL_COST_PER_MIN = 1;
    private static final double NORMAL_MIN_FARE = 5;

    private static final double PREMIUM_COST_PER_KM = 15;
    private static final int PREMIUM_COST_PER_MIN = 2;
    private static final double PREMIUM_MIN_FARE = 20;

    public double calculateFare(double distance, int time, RideType type) {
        double totalFare;
        if (type == RideType.PREMIUM) {
            totalFare = distance * PREMIUM_COST_PER_KM + time * PREMIUM_COST_PER_MIN;
            return Math.max(totalFare, PREMIUM_MIN_FARE);
        } else {
            totalFare = distance * NORMAL_COST_PER_KM + time * NORMAL_COST_PER_MIN;
            return Math.max(totalFare, NORMAL_MIN_FARE);
        }
    }



    private RideRepository rideRepository;
    public InvoiceGenerator(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }



    public InvoiceSummary getInvoiceByUser(String userId) {
        Ride[] rides = rideRepository.getRides(userId);
        return generateInvoice(rides);
    }


    public double calculateFare(double distance, int time) {
        double fare = distance * COST_PER_KM + time * COST_PER_MINUTE;
        return Math.max(fare, MINIMUM_FARE);
    }

    public double calculateFare(Ride[] rides) {
        double totalFare = 0;
        for (Ride ride : rides) {
            totalFare += calculateFare(ride.distance, ride.time, ride.rideType);
        }
        return totalFare;
    }


    public InvoiceSummary generateInvoice(Ride[] rides) {
        double totalFare = calculateFare(rides);
        return new InvoiceSummary(rides.length, totalFare);
    }


}

