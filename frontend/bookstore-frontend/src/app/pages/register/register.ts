import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService, RegisterRequest } from '../../services/auth';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {

  name = '';
  email = '';
  password = '';
  errorMessage = '';
  successMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  register(): void {

    const registerData: RegisterRequest = {
      name: this.name,
      email: this.email,
      password: this.password
    };

    this.authService.register(registerData).subscribe({
      next: (user) => {

        console.log('Registration successful:', user);

        this.successMessage = 'Registration successful! Redirecting to login...';
        this.errorMessage = '';

        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 1000);
      },

      error: (error) => {

        console.error(error);

        this.errorMessage =
          'Registration failed. Email may already exist.';

        this.successMessage = '';
      }
    });
  }
}