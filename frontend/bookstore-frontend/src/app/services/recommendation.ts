import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface RecommendationBook {
  id: number;
  title: string;
  author: string;
  price: number;
  stock: number;
  imageUrl: string;
}

@Injectable({
  providedIn: 'root'
})
export class RecommendationService {

  private apiUrl = 'http://localhost:8080/api/recommendations';

  constructor(private http: HttpClient) {}

  getRecommendations(userId: number): Observable<RecommendationBook[]> {

    return this.http.get<RecommendationBook[]>(
      `${this.apiUrl}/user/${userId}`
    );
  }

}