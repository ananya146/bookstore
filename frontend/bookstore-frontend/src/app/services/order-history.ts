import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface OrderHistoryItem {
  id: number;
  totalAmount: number;
  status: string;
  orderDate: string;
  tentativeDeliveryDate: string;
  giftPointsUsed: number;
  cancelledAt: string | null;
}

@Injectable({
  providedIn: 'root'
})
export class OrderHistoryService {

  private apiUrl = 'http://localhost:8080/api/orders';

  constructor(private http: HttpClient) {}

  getOrders(userId: number): Observable<OrderHistoryItem[]> {

    return this.http.get<OrderHistoryItem[]>(
      `${this.apiUrl}/user/${userId}`
    );
  }

  cancelOrder(orderId: number): Observable<OrderHistoryItem> {

    return this.http.post<OrderHistoryItem>(
      `${this.apiUrl}/${orderId}/cancel`,
      {}
    );
  }

  buyAgain(orderId: number): Observable<any> {

    return this.http.post<any>(
      `${this.apiUrl}/${orderId}/buy-again`,
      {}
    );
  }

}