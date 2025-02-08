// dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import {
  CardModule,
  GridModule,
  WidgetModule,
  ButtonModule,
  BadgeModule,
  ProgressBarComponent, ProgressComponent
} from '@coreui/angular';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    CardModule,
    GridModule,
    WidgetModule,
    ButtonModule,
    BadgeModule,
    ProgressBarComponent,
    ProgressComponent
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  // Stats
  totalProducts = 0;
  totalOrders = 0;
  lowStockItems = 0;
  pendingOrders = 0;

  // Recent Orders
  recentOrders: any[] = [];

  // Low Stock Items
  lowStockProducts: any[] = [];

  constructor(
    private router: Router
  ) {}

  ngOnInit() {
    // Load dashboard data
    this.loadDashboardData();
  }

  loadDashboardData() {
    // TODO: Call your services to load real data
    // For now using mock data
    this.totalProducts = 150;
    this.totalOrders = 45;
    this.lowStockItems = 8;
    this.pendingOrders = 12;

    this.recentOrders = [
      { id: 1, orderNumber: 'ORD-001', status: 'PENDING', amount: 1200 },
      { id: 2, orderNumber: 'ORD-002', status: 'DELIVERED', amount: 850 },
      { id: 3, orderNumber: 'ORD-003', status: 'SHIPPED', amount: 2100 }
    ];

    this.lowStockProducts = [
      { id: 1, name: 'Product A', currentStock: 5, minimumStock: 10 },
      { id: 2, name: 'Product B', currentStock: 3, minimumStock: 15 },
      { id: 3, name: 'Product C', currentStock: 8, minimumStock: 20 }
    ];
  }

  getStockPercentage(current: number, minimum: number): number {
    return (current / minimum) * 100;
  }

  getStatusColor(status: string): string {
    switch (status) {
      case 'PENDING': return 'warning';
      case 'SHIPPED': return 'primary';
      case 'DELIVERED': return 'success';
      default: return 'light';
    }
  }

  navigateToOrders() {
    this.router.navigate(['/orders']);
  }

  navigateToProducts() {
    this.router.navigate(['/products']);
  }
}
