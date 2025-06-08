package pl.biletmiejski.backend.dto.entities;

import pl.biletmiejski.backend.model.Vehicle;

import java.util.List;
import java.util.stream.Collectors;

public class VehicleMapper {
    public static VehicleDto toDto(Vehicle vehicle) {
        return new VehicleDto(vehicle.getVehicleId());
    }

    public static List<VehicleDto> toDtoList(List<Vehicle> vehicles) {
        return vehicles.stream()
                .map(VehicleMapper::toDto)
                .collect(Collectors.toList());
    }
}

