import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-order-confirmation',
  standalone: true,
  imports: [],
  templateUrl: './order-confirmation.html',
  styleUrl: './order-confirmation.css'
})
export class OrderConfirmation {

  orderId: number = 0;
  totalAmount: number = 0;
  deliveryDate: string = '';

  constructor(private router: Router) {
    const orderData = localStorage.getItem('lastOrder');

    if (orderData) {
      const order = JSON.parse(orderData);

      this.orderId = order.id;
      this.totalAmount = order.totalAmount;

      this.deliveryDate = new Date(
        order.tentativeDeliveryDate
      ).toLocaleDateString('en-IN', {
        day: '2-digit',
        month: 'long',
        year: 'numeric'
      });
    }
  }

  continueShopping(): void {
    this.router.navigate(['/books']);
  }
}