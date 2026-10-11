import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { PaymentService } from '../../services/payment';

interface PaymentOrder {
  id: number;
  totalAmount: number;
  status: string;
  orderDate: string;
  tentativeDeliveryDate: string;
  giftPointsUsed: number;
}

@Component({
  selector: 'app-payment-confirmation',
  standalone: true,
  imports: [RouterLink],
  styleUrl: './payment-confirmation.css',
  templateUrl: './payment-confirmation.html'
})
export class PaymentConfirmation implements OnInit {

  order: PaymentOrder | null = null;

  paymentMethod = '';

  errorMessage = '';

  processingPayment = false;

  constructor(
    private paymentService: PaymentService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {

    const orderData = localStorage.getItem('lastOrder');

    if (!orderData) {
      this.errorMessage = 'No order information found.';
      return;
    }

    this.order = JSON.parse(orderData);

    const paymentMethodData =
      localStorage.getItem('paymentMethod');

    if (paymentMethodData) {
      this.paymentMethod = paymentMethodData;
    }
  }

  confirmPayment(): void {

    if (!this.order) {
      this.errorMessage = 'No order information found.';
      return;
    }

    if (!this.paymentMethod) {
      this.errorMessage = 'Payment method not found.';
      return;
    }

    this.processingPayment = true;
    this.errorMessage = '';

    this.paymentService
      .processPayment(
        this.order.id,
        this.paymentMethod
      )
      .subscribe({

        next: (payment) => {

          console.log('Payment successful:', payment);

          this.processingPayment = false;

          this.router.navigate([
            '/order-confirmation'
          ]);

        },

        error: (error) => {

          console.error(
            'Error processing payment:',
            error
          );

          this.processingPayment = false;

          this.errorMessage =
            'Unable to process payment. Please try again.';

          this.cdr.detectChanges();
        }

      });
  }

}