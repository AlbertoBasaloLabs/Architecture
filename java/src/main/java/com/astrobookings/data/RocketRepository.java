package com.astrobookings.data;

import com.astrobookings.model.Rocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RocketRepository {
    private static final Map<String, Rocket> db = new HashMap<>();

    static {
        // Dummy data
        Rocket r1 = new Rocket("r1", "Falcon 9", 10, 25000.0);
        db.put(r1.getId(), r1);
    }

    public Rocket save(Rocket rocket) {
        // BAD SMELL: Business logic in Repository
        if (rocket.getCapacity() > 10) {
            throw new IllegalArgumentException("Rocket capacity cannot exceed 10");
        }
        db.put(rocket.getId(), rocket);
        return rocket;
    }

    public Optional<Rocket> findById(String id) {
        return Optional.ofNullable(db.get(id));
    }

    public List<Rocket> findAll() {
        return List.copyOf(db.values());
    }
}
