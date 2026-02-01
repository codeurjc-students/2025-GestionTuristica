import { AuthService, RegisterRequest } from './../../services/auth.service';
import { Component } from '@angular/core';
import { FormsModule } from "@angular/forms";
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [FormsModule, RouterLink],
  templateUrl: './register.html'
})
export class Register {
  name: string = '';
  email: string = '';
  password: string = '';

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router
  ) {}
  register() {
    const registerData: RegisterRequest = {
      name: this.name,
      email: this.email,
      password: this.password
    }

    this.authService.register(registerData).subscribe({
      next: () => {
        console.log('Registration successful');
        this.router.navigate(['/login']);
      },
      error: (err) => console.error('Registration error:', err)
    })
  }
}
