import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  constructor(private http: HttpClient) {}

  // Generic CRUD methods
  getAll<T>(endpoint: string): Observable<T[]> {
    return this.http.get<T[]>(endpoint);
  }

  getById<T>(endpoint: string, id: number): Observable<T> {
    return this.http.get<T>(`${endpoint}/${id}`);
  }

  create<T>(endpoint: string, data: any): Observable<T> {
    return this.http.post<T>(endpoint, data);
  }

  update<T>(endpoint: string, id: number, data: any): Observable<T> {
    return this.http.put<T>(`${endpoint}/${id}`, data);
  }

  delete(endpoint: string, id: number): Observable<void> {
    return this.http.delete<void>(`${endpoint}/${id}`);
  }
}
