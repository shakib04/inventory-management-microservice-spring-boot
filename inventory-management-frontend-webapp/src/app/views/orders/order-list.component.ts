import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import {
  CardModule,
  GridModule,
  ButtonModule,
  BadgeModule,
  SpinnerModule,
  PaginationModule,
  ListGroupModule
} from '@coreui/angular';
import { IconModule } from '@coreui/icons-angular';
import {OrderService} from "../../services/order.service";

@Component({
  selector: 'app-order-list',
  standalone: true,
  imports: [
    CommonModule,
    CardModule,
    GridModule,
    ButtonModule,
    BadgeModule,
    SpinnerModule,
    PaginationModule,
    ListGroupModule,
    IconModule
  ],
  templateUrl: 'order-list.component.html',
  styleUrl: 'order.component.scss'
})
export class OrderListComponent implements OnInit {
  orders: any[] = [];
  loading = true;
  currentPage = 1;
  pageSize = 10;
  totalPages = 0;

  constructor(
    private orderService: OrderService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadOrders();
  }

  loadOrders() {
    this.loading = true;
    this.orderService.getOrders(this.currentPage - 1, this.pageSize).subscribe({
      next: (response) => {
        this.orders = response.content;
        this.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading orders:', error);
        this.loading = false;
      }
    });
  }

  getStatusColor(status: string): string {
    switch (status) {
      case 'PENDING':
        return 'warning';
      case 'CONFIRMED':
        return 'info';
      case 'SHIPPED':
        return 'primary';
      case 'DELIVERED':
        return 'success';
      case 'CANCELLED':
        return 'danger';
      default:
        return 'light';
    }
  }

  createOrder() {
    this.router.navigate(['/orders/create']);
  }

  editOrder(order: any) {
    this.router.navigate(['/orders/edit', order.id]);
  }

  deleteOrder(order: any) {
    if (confirm('Are you sure you want to delete this order?')) {
      this.orderService.deleteOrder(order.id).subscribe({
        next: () => {
          this.loadOrders();
        },
        error: (error) => {
          console.error('Error deleting order:', error);
        }
      });
    }
  }

  onPageChange(page: number) {
    this.currentPage = page;
    this.loadOrders();
  }
}
