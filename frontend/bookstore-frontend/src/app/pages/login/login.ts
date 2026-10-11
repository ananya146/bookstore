import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService, LoginRequest } from '../../services/auth';
import { Router, RouterLink } from '@angular/router';
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {

  email = '';
  password = '';
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  login(): void {

    const loginData: LoginRequest = {
      email: this.email,
      password: this.password
    };

    this.authService.login(loginData).subscribe({
      next: (user) => {

        console.log('Login successful:', user);

        // Save the logged-in user in browser storage
        localStorage.setItem(
          'currentUser',
          JSON.stringify(user)
        );

        // Go to the bookstore homepage
        this.router.navigate(['/books']);
      },

      error: (error) => {

        console.error(error);

        this.errorMessage =
          'Invalid email or password.';
      }
    });
  }
}