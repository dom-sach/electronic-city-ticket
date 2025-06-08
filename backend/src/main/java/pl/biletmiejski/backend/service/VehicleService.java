package pl.biletmiejski.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.biletmiejski.backend.model.Vehicle;
import pl.biletmiejski.backend.repository.VehicleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }
}

