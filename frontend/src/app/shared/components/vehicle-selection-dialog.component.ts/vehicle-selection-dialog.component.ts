import { Component, Inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from '@angular/material/dialog';
import { VehicleService } from '../../../core/services/vehicle.service';
import { Vehicle} from '../../../core/models/ticket.model';
import { Observable, debounceTime, switchMap } from 'rxjs';
import {MatFormField, MatInput} from '@angular/material/input';
import {MatOption, MatSelect} from '@angular/material/select';
import {FormsModule} from '@angular/forms';
import {MatButton} from '@angular/material/button';
import {NgForOf} from '@angular/common';
import {MatLabel} from '@angular/material/form-field';

@Component({
  selector: 'app-vehicle-selection-dialog',
  templateUrl: './vehicle-selection-dialog.component.html',
  standalone: true,
  imports: [
    MatSelect,
    MatFormField,
    MatDialogContent,
    MatDialogTitle,
    FormsModule,
    MatInput,
    MatOption,
    NgForOf,
    MatDialogActions,
    MatButton,
    MatLabel
  ],
  styleUrls: ['./vehicle-selection-dialog.component.scss']
})
export class VehicleSelectionDialogComponent {
  vehicles: Vehicle[] = [];
  filteredVehicles: Vehicle[] = [];
  searchVehicle: string = ''; // Text from input field for filtering vehicles
  selectedVehicleId: string = ''; // Add this line for selected vehicle ID
  isLoading = false;

  constructor(
    public dialogRef: MatDialogRef<VehicleSelectionDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any, // Data passed from the parent component (ticket info)
    private vehicleService: VehicleService
  ) {}

  ngOnInit(): void {
    this.loadVehicles();
  }

  // Fetch vehicles from backend
  loadVehicles(): void {
    this.isLoading = true;
    this.vehicleService.getVehicles().subscribe({
      next: (data) => {
        this.vehicles = data;
        this.filteredVehicles = data;  // Initially show all vehicles
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error fetching vehicles:', err);
        this.isLoading = false;
      }
    });
  }

  // Filter vehicles based on search input
  filterVehicles(): void {
    this.filteredVehicles = this.vehicles.filter(vehicle =>
      vehicle.vehicleId.toLowerCase().includes(this.searchVehicle.toLowerCase())
    );
  }

  // Activating ticket with selected vehicle ID
  activateTicket(): void {
    if (this.selectedVehicleId) {
      this.dialogRef.close({ vehicleId: this.selectedVehicleId });  // Pass selected vehicleId to the parent
    } else {
      console.log("No vehicle selected");
    }
  }

  // Cancel the dialog without doing anything
  cancel(): void {
    this.dialogRef.close();
  }
}
