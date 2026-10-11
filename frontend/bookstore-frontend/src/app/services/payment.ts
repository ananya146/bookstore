import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Payment {
  id: number;
  paymentMethod: string;
  amount: number;
  status: string;
  paymentDate: string;
}

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  private apiUrl = 'http://localhost:8080/api/payments';

  constructor(private http: HttpClient) {}

  processPayment(
    orderId: number,
    paymentMethod: string
  ): Observable<Payment> {

    return this.http.post<Payment>(
      `${this.apiUrl}?orderId=${orderId}&paymentMethod=${paymentMethod}`,
      {}
    );
  }

}