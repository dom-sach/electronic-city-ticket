package pl.biletmiejski.backend.dto.entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VehicleDto {
    // Gettery i Settery
    private String vehicleId; // ID pojazdu

    public VehicleDto(String vehicleId) {
        this.vehicleId = vehicleId;
    }

}

