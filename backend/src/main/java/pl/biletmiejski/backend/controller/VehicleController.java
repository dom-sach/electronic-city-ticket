package pl.biletmiejski.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.biletmiejski.backend.dto.entities.VehicleDto;
import pl.biletmiejski.backend.dto.entities.VehicleMapper;
import pl.biletmiejski.backend.model.Vehicle;
import pl.biletmiejski.backend.service.VehicleService;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @Operation(summary = "Pojazdy", description = "Umożliwia pozyskanie wszystkich pojazdów")
    @GetMapping
    public ResponseEntity<List<VehicleDto>> getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        List<VehicleDto> vehicleDtos = VehicleMapper.toDtoList(vehicles); // Mapowanie na DTO
        return ResponseEntity.ok(vehicleDtos);
    }
}

