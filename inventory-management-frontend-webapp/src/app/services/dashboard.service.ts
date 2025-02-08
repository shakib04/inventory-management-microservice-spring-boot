// dashboard.service.ts
import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  constructor(private http: HttpClient) {}

  getDashboardStats() {
    return this.http.get<any>('/api/dashboard/stats');
  }

  getRecentOrders() {
    return this.http.get<any>('/api/dashboard/recent-orders');
  }

  getLowStockItems() {
    return this.http.get<any>('/api/dashboard/low-stock');
  }
}
