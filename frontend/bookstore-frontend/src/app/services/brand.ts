import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book } from './book';

export interface Brand {
  id: number;
  name: string;
}

@Injectable({
  providedIn: 'root'
})
export class BrandService {

  private brandApiUrl = 'http://localhost:8080/api/brands';
  private bookApiUrl = 'http://localhost:8080/api/books';

  constructor(private http: HttpClient) {}

  getBrands(): Observable<Brand[]> {
    return this.http.get<Brand[]>(this.brandApiUrl);
  }

  getBooksByBrand(brandId: number): Observable<Book[]> {
    return this.http.get<Book[]>(
      `${this.bookApiUrl}/brand/${brandId}`
    );
  }

}