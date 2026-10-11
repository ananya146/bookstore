import { Routes } from '@angular/router';

import { Login } from './pages/login/login';

import { Register } from './pages/register/register';

import { Books } from './pages/books/books';

import { Cart } from './pages/cart/cart';

import { Checkout } from './pages/checkout/checkout';

import { OrderConfirmation } from './pages/order-confirmation/order-confirmation';

import { OrderHistory } from './pages/order-history/order-history';

import { BookDetail } from './pages/book-detail/book-detail';

import { PaymentConfirmation } from './pages/payment-confirmation/payment-confirmation';

export const routes: Routes = [

  { path: 'login', component: Login },

  { path: 'register', component: Register },

  { path: 'books', component: Books },

  { path: 'books/:id', component: BookDetail },

  { path: 'cart', component: Cart },

  { path: 'checkout', component: Checkout },

  { path: 'payment-confirmation', component: PaymentConfirmation },

  { path: 'order-confirmation', component: OrderConfirmation },

  { path: 'orders', component: OrderHistory },

  { path: '', redirectTo: 'books', pathMatch: 'full' }

];