import { Component, OnInit } from '@angular/core';
import { RouterLink } from "@angular/router";
import { AuthService } from '../../services/auth.service';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { Subscription } from 'rxjs';
import { MatIconModule } from '@angular/material/icon';
@Component({
  selector: 'app-navbar',
  imports: [RouterLink, MatToolbarModule, MatButtonModule, MatIconModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
})
export class Navbar implements OnInit{

  constructor(private readonly authService: AuthService) {}

  userLoggedIn: boolean = false;
  private sub!: Subscription;

  ngOnInit() {
    this.sub = this.authService.loggedIn$.subscribe(
      logged => this.userLoggedIn = logged
    );
  }

  logout() {
    this.authService.logout();
  }
}
