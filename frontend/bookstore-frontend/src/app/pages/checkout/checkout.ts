
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CartItem, CartService } from '../../services/cart';
import { Address, AddressService } from '../../services/address';
import { OrderService } from '../../services/order';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './checkout.html',
  styleUrl: './checkout.css'
})
export class Checkout implements OnInit {
  cartItems: CartItem[] = [];
  totalPrice = 0;

  addresses: Address[] = [];
  selectedAddress: Address | null = null;

  errorMessage = '';
  successMessage = '';
  selectedPaymentMethod = 'UPI';
  useGiftPoints = false;

  giftPointsAvailable = 0;

  showAddressForm = false;
  savingAddress = false;
  deletingAddressId: number | null = null;

  newAddress: Omit<Address, 'id'> = {
    fullName: '',
    phone: '',
    addressLine: '',
    city: '',
    state: '',
    postalCode: ''
  };

  constructor(
    private cartService: CartService,
    private addressService: AddressService,
    private orderService: OrderService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadGiftPoints();
    this.loadCart();
    this.loadAddresses();
  }

  // Read the user's currently saved gift-point balance.
  loadGiftPoints(): void {
    const userData = localStorage.getItem('currentUser');

    if (!userData) {
      this.giftPointsAvailable = 0;
      return;
    }

    try {
      const user = JSON.parse(userData);
      const points = Number(user.giftPoints ?? 0);

      this.giftPointsAvailable =
        Number.isFinite(points) ? Math.max(0, Math.floor(points)) : 0;
    } catch {
      this.giftPointsAvailable = 0;
      this.errorMessage = 'Unable to read your account details. Please login again.';
    }
  }

  // Redeem only whole points, never exceeding the subtotal or balance.
  get giftPointsToRedeem(): number {
    if (!this.useGiftPoints) {
      return 0;
    }

    return Math.min(
      this.giftPointsAvailable,
      Math.max(0, Math.floor(this.totalPrice))
    );
  }
  get giftPointsToRedeemForDisplay(): number {
    return Math.min(
      this.giftPointsAvailable,
      Math.max(0, Math.floor(this.totalPrice))
    );
  }
  // Calculate the amount remaining after the gift-point discount.
  get payableAmount(): number {
    return Math.max(0, this.totalPrice - this.giftPointsToRedeem);
  }

  loadCart(): void {
    const userData = localStorage.getItem('currentUser');

    if (!userData) {
      this.errorMessage = 'Please login first.';
      return;
    }

    let user: { id: number };

    try {
      user = JSON.parse(userData);
    } catch {
      this.errorMessage = 'Unable to read your account details. Please login again.';
      return;
    }

    this.cartService.getCart(user.id).subscribe({
      next: (data) => {
        this.cartItems = data;
        this.calculateTotal();
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error loading cart:', error);
        this.errorMessage = 'Unable to load cart.';
        this.cdr.detectChanges();
      }
    });
  }

  loadAddresses(): void {
    const userData = localStorage.getItem('currentUser');

    if (!userData) {
      return;
    }

    let user: { id: number };

    try {
      user = JSON.parse(userData);
    } catch {
      this.errorMessage = 'Unable to read your account details. Please login again.';
      return;
    }

    this.addressService.getAddresses(user.id).subscribe({
      next: (data) => {
        this.addresses = data;

        if (
          !this.selectedAddress ||
          !this.addresses.some(
            (address) => address.id === this.selectedAddress?.id
          )
        ) {
          this.selectedAddress = this.addresses[0] ?? null;
        }

        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error loading addresses:', error);
        this.errorMessage = 'Unable to load delivery address.';
        this.cdr.detectChanges();
      }
    });
  }

  selectAddress(address: Address): void {
    this.selectedAddress = address;
    this.errorMessage = '';
    this.successMessage = '';
  }

  calculateTotal(): void {
    this.totalPrice = this.cartItems.reduce(
      (total, item) => total + item.book.price * item.quantity,
      0
    );
  }

  saveAddress(): void {
    const userData = localStorage.getItem('currentUser');

    if (!userData) {
      this.errorMessage = 'Please login first.';
      return;
    }

    let user: { id: number };

    try {
      user = JSON.parse(userData);
    } catch {
      this.errorMessage = 'Unable to read your account details. Please login again.';
      return;
    }

    this.savingAddress = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.addressService.addAddress(user.id, this.newAddress).subscribe({
      next: (address) => {
        this.addresses = [...this.addresses, address];
        this.selectedAddress = address;
        this.showAddressForm = false;
        this.savingAddress = false;

        this.newAddress = {
          fullName: '',
          phone: '',
          addressLine: '',
          city: '',
          state: '',
          postalCode: ''
        };

        this.successMessage = 'Address saved successfully.';
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error saving address:', error);
        this.errorMessage =
          error?.error?.message || 'Unable to save address. Please try again.';
        this.savingAddress = false;
        this.cdr.detectChanges();
      }
    });
  }

  deleteAddress(address: Address): void {
    const confirmed = window.confirm(
      `Delete the saved address for ${address.fullName}?\n\n` +
      'Addresses linked to existing orders cannot be deleted.'
    );

    if (!confirmed) {
      return;
    }

    const userData = localStorage.getItem('currentUser');

    if (!userData) {
      this.errorMessage = 'Please login first.';
      return;
    }

    let user: { id: number };

    try {
      user = JSON.parse(userData);
    } catch {
      this.errorMessage = 'Unable to read your account details. Please login again.';
      return;
    }

    this.deletingAddressId = address.id;
    this.errorMessage = '';
    this.successMessage = '';

    this.addressService.deleteAddress(user.id, address.id).subscribe({
      next: () => {
        this.addresses = this.addresses.filter(
          (savedAddress) => savedAddress.id !== address.id
        );

        if (this.selectedAddress?.id === address.id) {
          this.selectedAddress = this.addresses[0] ?? null;
        }

        this.deletingAddressId = null;
        this.successMessage = 'Address deleted successfully.';
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error deleting address:', error);

        if (error?.status === 409) {
          this.errorMessage =
            'This address is linked to an existing order and cannot be deleted.';
        } else if (error?.status === 403) {
          this.errorMessage = 'You cannot delete another user’s address.';
        } else if (error?.status === 404) {
          this.errorMessage = 'Address not found. Please refresh and try again.';
        } else {
          this.errorMessage =
            error?.error?.message || 'Unable to delete address. Please try again.';
        }

        this.deletingAddressId = null;
        this.cdr.detectChanges();
      }
    });
  }

  placeOrder(): void {
    const userData = localStorage.getItem('currentUser');

    if (!userData) {
      this.errorMessage = 'Please login first.';
      return;
    }

    if (!this.selectedAddress) {
      this.errorMessage = 'Please select a delivery address.';
      return;
    }

    if (this.cartItems.length === 0) {
      this.errorMessage = 'Your cart is empty.';
      return;
    }

    let user: { id: number; giftPoints?: number };

    try {
      user = JSON.parse(userData);
    } catch {
      this.errorMessage = 'Unable to read your account details. Please login again.';
      return;
    }

    // Send the calculated redemption amount, not the entire balance.
    const giftPointsUsed = this.giftPointsToRedeem;

    this.errorMessage = '';
    this.successMessage = '';

    this.orderService.createOrder(
      user.id,
      this.selectedAddress.id,
      this.selectedPaymentMethod,
      giftPointsUsed
    ).subscribe({
      next: (order) => {
        console.log('Order created:', order);

        // Keep the locally saved balance in sync after successful order creation.
        user.giftPoints = Math.max(
          0,
          this.giftPointsAvailable - giftPointsUsed
        );

        localStorage.setItem('currentUser', JSON.stringify(user));
        this.giftPointsAvailable = user.giftPoints;

        localStorage.setItem('lastOrder', JSON.stringify(order));
        localStorage.setItem('paymentMethod', this.selectedPaymentMethod);

        this.router.navigate(['/payment-confirmation']);
      },
      error: (error) => {
        console.error('Error creating order:', error);
        this.errorMessage =
          error?.error?.message || 'Unable to place order.';
        this.cdr.detectChanges();
      }
    });
  }
}
