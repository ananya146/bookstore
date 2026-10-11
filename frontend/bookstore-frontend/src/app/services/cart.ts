
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CartItem {
  id: number;
  quantity: number;
  book: {
    id: number;
    title: string;
    author: string;
    price: number;
    stock: number;
    imageUrl: string;
  };
}

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private apiUrl = 'http://localhost:8080/api/cart';

  constructor(private http: HttpClient) {}

  getCart(userId: number): Observable<CartItem[]> {
    return this.http.get<CartItem[]>(
      `${this.apiUrl}/user/${userId}`
    );
  }

  addToCart(
    userId: number,
    bookId: number,
    quantity: number
  ): Observable<CartItem> {
    return this.http.post<CartItem>(
      `${this.apiUrl}/items?userId=${userId}&bookId=${bookId}&quantity=${quantity}`,
      {}
    );
  }

  updateQuantity(
    cartItemId: number,
    quantity: number
  ): Observable<CartItem> {
    return this.http.put<CartItem>(
      `${this.apiUrl}/items/${cartItemId}?quantity=${quantity}`,
      {}
    );
  }

  removeFromCart(cartItemId: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/items/${cartItemId}`
    );
  }
}
