package org.rituraj;

import java.util.*;

public class RideRepository {
    private final Map<String, List<Ride>> userRides = new HashMap<>();

    public void addRides(String userId, Ride[] rides) {
        userRides.put(userId, Arrays.asList(rides));
    }

    public Ride[] getRides(String userId) {
        return userRides.get(userId).toArray(new Ride[0]);
    }
}

