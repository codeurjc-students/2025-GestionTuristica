import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html'
})
export class Login {
  email: string = '';
  password: string = '';

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router
  ) {}

  login(){
    const loginData = {
      email: this.email,
      password: this.password
    }

    this.authService.login(loginData).subscribe({
      next: () => {
        this.router.navigate(['/']);
      },
      error: (err) => console.error('Login error:', err)
    })
  }

}
