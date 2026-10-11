import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Book {
  id: number;
  title: string;
  author: string;
  price: number;
  stock: number;
  imageUrl: string;
  category: {
    id: number;
    name: string;
  };
  brand: {
    id: number;
    name: string;
  };
}

@Injectable({
  providedIn: 'root'
})
export class BookService {

  private apiUrl = 'http://localhost:8080/api/books';

  constructor(private http: HttpClient) {}

  getBooks(): Observable<Book[]> {
    return this.http.get<Book[]>(this.apiUrl);
  }

  getBookById(bookId: number): Observable<Book> {
    return this.http.get<Book>(
      `${this.apiUrl}/${bookId}`
    );
  }

  getBooksByCategory(categoryId: number): Observable<Book[]> {
    return this.http.get<Book[]>(
      `${this.apiUrl}/category/${categoryId}`
    );
  }

}