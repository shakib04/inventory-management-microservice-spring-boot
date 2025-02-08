import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = '/api/orders';

  constructor(private http: HttpClient) {}

  // Get paginated orders
  getOrders(page: number = 0, size: number = 10): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<any>(this.apiUrl, { params });
  }

  // Get single order by id
  getOrder(id: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  // Create new order
  createOrder(orderRequest: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, orderRequest);
  }

  // Update order status
  updateOrderStatus(id: string, status: string): Observable<any> {
    return this.http.patch<any>(`${this.apiUrl}/${id}/status`, { status });
  }

  // Delete order
  deleteOrder(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Search orders with filters
  searchOrders(filters: any): Observable<any> {
    let params = new HttpParams()
      .set('page', filters.page ? filters.page.toString() : '0')
      .set('size', filters.size ? filters.size.toString() : '10');

    if (filters.orderNumber) {
      params = params.set('orderNumber', filters.orderNumber);
    }
    if (filters.status) {
      params = params.set('status', filters.status);
    }

    return this.http.get<any>(this.apiUrl, { params });
  }
}
