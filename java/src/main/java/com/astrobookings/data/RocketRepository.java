package com.astrobookings.data;

import com.astrobookings.model.Rocket;
import java.util.UUID;
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
        // Validate name
        if (rocket.getName() == null || rocket.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Rocket name must be provided");
        }
        // Validate capacity (existing rule)
        if (rocket.getCapacity() > 10) {
            throw new IllegalArgumentException("Rocket capacity cannot exceed 10");
        }
        // Validate duplicate ID
        if (rocket.getId() != null && db.containsKey(rocket.getId())) {
            throw new IllegalArgumentException("Rocket with id " + rocket.getId() + " already exists");
        }
        // Assign a new ID if missing
        if (rocket.getId() == null) {
            rocket.setId(UUID.randomUUID().toString());
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
