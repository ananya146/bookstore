import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import {
  OrderHistoryItem,
  OrderHistoryService
} from '../../services/order-history';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
@Component({
  selector: 'app-order-history',
  standalone: true,
  imports: [DatePipe, RouterLink],
  templateUrl: './order-history.html',
  styleUrl: './order-history.css'
})
export class OrderHistory implements OnInit {

  orders: OrderHistoryItem[] = [];
  errorMessage = '';

  constructor(
    private orderHistoryService: OrderHistoryService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {

    const userData = localStorage.getItem('currentUser');

    if (!userData) {
      this.errorMessage = 'Please login first.';
      return;
    }

    const user = JSON.parse(userData);

    this.orderHistoryService.getOrders(user.id).subscribe({

      next: (data) => {
        this.orders = data;
        this.cdr.detectChanges();
      },

      error: (error) => {
        console.error('Error loading orders:', error);
        this.errorMessage = 'Unable to load order history.';
        this.cdr.detectChanges();
      }

    });
  }

  canCancel(order: OrderHistoryItem): boolean {

    if (order.status === 'CANCELLED') {
      return false;
    }

    const orderDate = new Date(order.orderDate);
    const now = new Date();

    const hoursPassed =
      (now.getTime() - orderDate.getTime()) / (1000 * 60 * 60);

    return hoursPassed <= 48;
  }

  cancelOrder(order: OrderHistoryItem): void {

    const confirmed = confirm(
      `Are you sure you want to cancel Order #${order.id}?`
    );

    if (!confirmed) {
      return;
    }

    this.orderHistoryService.cancelOrder(order.id).subscribe({

      next: (updatedOrder) => {

        const index = this.orders.findIndex(
          (item) => item.id === updatedOrder.id
        );

        if (index !== -1) {
          this.orders[index] = updatedOrder;
        }

        this.cdr.detectChanges();
      },

      error: (error) => {

        console.error('Error cancelling order:', error);

        this.errorMessage = 'Unable to cancel order.';

        this.cdr.detectChanges();
      }

    });
  }

  buyAgain(order: OrderHistoryItem): void {

    this.orderHistoryService.buyAgain(order.id).subscribe({

      next: () => {
        alert('Items added to your cart successfully.');
      },

      error: (error) => {
        console.error('Error buying again:', error);
        this.errorMessage = 'Unable to add items to cart.';
        this.cdr.detectChanges();
      }

    });
  }

}