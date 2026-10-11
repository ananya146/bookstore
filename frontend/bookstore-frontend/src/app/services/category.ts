import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book } from './book';

export interface Category {
  id: number;
  name: string;
}

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private categoryApiUrl = 'http://localhost:8080/api/categories';
  private bookApiUrl = 'http://localhost:8080/api/books';

  constructor(private http: HttpClient) {}

  getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(this.categoryApiUrl);
  }

  getBooksByCategory(categoryId: number): Observable<Book[]> {
    return this.http.get<Book[]>(
      `${this.bookApiUrl}/category/${categoryId}`
    );
  }

}