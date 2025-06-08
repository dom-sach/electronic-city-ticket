import {Component, OnInit} from '@angular/core';
import {User} from '../../../core/models/user.model';
import {UserService} from '../../../core/services/user.service';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {FormsModule} from '@angular/forms';
import {MatTab, MatTabGroup} from '@angular/material/tabs';
import {MatList, MatListItem} from '@angular/material/list';
import {NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  standalone: true,
  imports: [
    MatListItem,
    MatList,
    MatTab,
    MatTabGroup,
    MatLabel,
    MatFormField,
    FormsModule,
    MatInput,
    NgIf,
    NgForOf
  ],
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements OnInit {
  usersByRole: Map<string, User[]> = new Map();
  searchQuery: string = '';

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getAllUsers().subscribe({
      next: (data) => {
        console.log("[user-list.component] users fetched: ", data)
        this.usersByRole = new Map(Object.entries(data));
      },
      error: (error) => {
        console.error('Błąd ładowania użytkowników:', error);
      }
    });
  }

  getAllUsers(): User[] {
    let allUsers: User[] = [];
    this.usersByRole.forEach((users) => {
      allUsers = allUsers.concat(users);
    });
    return allUsers;
  }

  filterUsers(role: string): User[] {
    // Jeśli brak zapytania, zwróć wszystkich użytkowników
    if (!this.searchQuery) {
      return this.usersByRole.get(role) || [];
    }
    // Filtruj użytkowników po emailu
    return (this.usersByRole.get(role) || []).filter(user =>
      user.email.toLowerCase().includes(this.searchQuery.toLowerCase())
    );
  }
}
