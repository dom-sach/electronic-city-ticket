package pl.biletmiejski.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.biletmiejski.backend.model.Vehicle;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByVehicleId(String vehicleId);

    List<Vehicle> findAll();
}
