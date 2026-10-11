import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Order {
  id: number;
  totalAmount: number;
  status: string;
  orderDate: string;
  tentativeDeliveryDate: string;
  giftPointsUsed: number;
}

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private apiUrl = 'http://localhost:8080/api/orders';

  constructor(private http: HttpClient) {}

  createOrder(
    userId: number,
    addressId: number,
    paymentMethod: string,
    giftPointsUsed: number
  ): Observable<Order> {

    return this.http.post<Order>(
      `${this.apiUrl}?userId=${userId}&addressId=${addressId}&paymentMethod=${paymentMethod}&giftPointsUsed=${giftPointsUsed}`,
      {}
    );
  }
}