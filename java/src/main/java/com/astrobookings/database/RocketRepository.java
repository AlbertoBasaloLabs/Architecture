package com.astrobookings.database;

import com.astrobookings.models.Rocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RocketRepository {
    private static final Map<String, Rocket> db = new HashMap<>();

    static {
        // Dummy data
        var dummyId = "00000000-0000-0000-0000-000000000001";
        Rocket r1 = new Rocket(dummyId, "Falcon 9", 10, 25000.0);
        db.put(dummyId, r1);
    }

    public Rocket save(Rocket rocket) {
        if (rocket.getName() == null || rocket.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Rocket name must be provided");
        }
        if (rocket.getCapacity() <= 0) {
            throw new IllegalArgumentException("Rocket capacity must be greater than 0");
        }
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
