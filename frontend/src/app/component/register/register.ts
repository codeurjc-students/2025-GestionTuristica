import { AuthService, RegisterRequest } from './../../services/auth.service';
import { Component } from '@angular/core';
import { FormsModule, NgForm } from "@angular/forms";
import { Router, RouterLink } from '@angular/router';
import { DialogService } from '../../services/dialog.service';

@Component({
  selector: 'app-register',
  imports: [FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.scss'
})
export class Register {
  name: string = '';
  email: string = '';
  password: string = '';

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router,
    private readonly dialogService: DialogService
  ) { }
  register(form: NgForm) {

    if (form.invalid) {
      this.dialogService.infoDialog('Error', 'Es necesario rellenar todos los campos para registrarse.');
      return;
    }

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
      error: () => {
        this.dialogService.infoDialog('Error', 'Ha ocurrido un error inesperado. Intenta registrarte de nuevo');
        this.router.navigate(['register']);
      }
    })
  }
}
