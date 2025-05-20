package pl.biletmiejski.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.biletmiejski.backend.model.Vehicle;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByVehicleId(String vehicleId);
}
