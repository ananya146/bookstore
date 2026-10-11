
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CartItem, CartService } from '../../services/cart';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './cart.html',
  styleUrl: './cart.css'
})
export class Cart implements OnInit {

  cartItems: CartItem[] = [];
  errorMessage = '';

  constructor(
    private cartService: CartService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadCart();
  }

  loadCart(): void {

    const userData = localStorage.getItem('currentUser');

    if (!userData) {
      this.errorMessage = 'Please login first.';
      return;
    }

    const user = JSON.parse(userData);

    this.cartService.getCart(user.id).subscribe({

      next: (data) => {
        this.cartItems = data;

        console.log('Cart loaded:', this.cartItems);
        console.log(
          'Number of cart items:',
          this.cartItems.length
        );

        this.cdr.detectChanges();
      },

      error: (error) => {
        console.error('Error loading cart:', error);

        this.errorMessage = 'Unable to load cart.';

        this.cdr.detectChanges();
      }
    });
  }

  increaseQuantity(item: CartItem): void {

    this.cartService.updateQuantity(
      item.id,
      item.quantity + 1
    ).subscribe({

      next: () => {
        item.quantity++;

        this.cdr.detectChanges();
      },

      error: (error) => {
        console.error(
          'Error increasing quantity:',
          error
        );
      }
    });
  }

  decreaseQuantity(item: CartItem): void {

    if (item.quantity <= 1) {
      return;
    }

    this.cartService.updateQuantity(
      item.id,
      item.quantity - 1
    ).subscribe({

      next: () => {
        item.quantity--;

        this.cdr.detectChanges();
      },

      error: (error) => {
        console.error(
          'Error decreasing quantity:',
          error
        );
      }
    });
  }

  removeFromCart(item: CartItem): void {

    this.cartService.removeFromCart(item.id).subscribe({

      next: () => {

        this.cartItems = this.cartItems.filter(
          cartItem => cartItem.id !== item.id
        );

        this.cdr.detectChanges();
      },

      error: (error) => {
        console.error(
          'Error removing item from cart:',
          error
        );
      }
    });
  }

  getTotalPrice(): number {

    return this.cartItems.reduce(
      (total, item) =>
        total + (item.book.price * item.quantity),
      0
    );
  }
}
