import { DialogService } from './../../services/dialog.service';
import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router, RouterLink } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login {
  email: string = '';
  password: string = '';

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router,
    private readonly dialogService: DialogService
  ) { }

  login(form: NgForm) {

    if (form.invalid) {
      this.dialogService.infoDialog('Error', 'Introduce tu dirección de correo electrónico y contraseña para iniciar sesión.');
      return;
    }

    const loginData = {
      email: this.email,
      password: this.password
    }

    this.authService.login(loginData).subscribe({
      next: () => {
        this.router.navigate(['/']);
      },
      error: () => {
        this.dialogService.infoDialog('Error', 'El correo o la contraseña son incorrectos.')
        this.router.navigate(['login'])
      }
    })
  }

}
