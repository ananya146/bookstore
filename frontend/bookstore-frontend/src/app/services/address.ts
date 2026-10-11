
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Address {
  id: number;
  fullName: string;
  phone: string;
  addressLine: string;
  city: string;
  state: string;
  postalCode: string;
}

@Injectable({
  providedIn: 'root'
})
export class AddressService {
  private apiUrl = 'http://localhost:8080/api/addresses';

  constructor(private http: HttpClient) {}

  getAddresses(userId: number): Observable<Address[]> {
    return this.http.get<Address[]>(`${this.apiUrl}/user/${userId}`);
  }

  addAddress(
    userId: number,
    address: Omit<Address, 'id'>
  ): Observable<Address> {
    return this.http.post<Address>(
      `${this.apiUrl}/user/${userId}`,
      address
    );
  }

  deleteAddress(
    userId: number,
    addressId: number
  ): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/user/${userId}/${addressId}`
    );
  }
}
