import {CurrencyPipe, NgForOf, NgIf, NgStyle} from '@angular/common';
import {Component, OnInit} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';
import {
  AvatarComponent,
  BadgeComponent,
  ButtonDirective,
  ButtonGroupComponent,
  CardBodyComponent,
  CardComponent,
  CardFooterComponent,
  CardHeaderComponent,
  ColComponent,
  FormCheckLabelDirective,
  GutterDirective,
  PaginationComponent,
  ProgressBarDirective,
  ProgressComponent,
  RowComponent,
  SpinnerComponent,
  TableDirective,
  TextColorDirective
} from '@coreui/angular';
import {ChartjsComponent} from '@coreui/angular-chartjs';
import {IconDirective} from '@coreui/icons-angular';

import {WidgetsBrandComponent} from '../widgets/widgets-brand/widgets-brand.component';
import {WidgetsDropdownComponent} from '../widgets/widgets-dropdown/widgets-dropdown.component';
import {Product} from "../../models/product.model";
import {ProductService} from "../../services/product.service";
import {Router} from "@angular/router";
@Component({
  templateUrl: 'product.component.html',
  styleUrls: ['product.component.scss'],
  standalone: true,
  imports: [WidgetsDropdownComponent, TextColorDirective, CardComponent,
    CardBodyComponent, RowComponent, ColComponent, ButtonDirective,
    IconDirective, ReactiveFormsModule, ButtonGroupComponent, FormCheckLabelDirective,
    ChartjsComponent, NgStyle, CardFooterComponent, GutterDirective,
    ProgressBarDirective, ProgressComponent, WidgetsBrandComponent,
    CardHeaderComponent, TableDirective, AvatarComponent,
    SpinnerComponent, BadgeComponent, CurrencyPipe, NgIf, NgForOf, PaginationComponent]
})
export class ProductComponent implements OnInit {

  products: Product[] = [];
  loading = true;
  currentPage = 0;
  pageSize = 10;
  totalPages = 0;
  totalElements = 0;
  Math = Math; // For using Math in template

  constructor(private productService: ProductService, private router: Router) {
  }

  ngOnInit() {
    this.loadProducts();
  }

  loadProducts() {
    this.loading = true;
    this.productService.getProducts(this.currentPage, this.pageSize).subscribe({
      next: (page) => {
        this.products = page.content;
        this.totalPages = page.totalPages;
        this.totalElements = page.totalElements;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading products:', error);
        this.loading = false;
      }
    });
  }

  onPageChange(page: number) {
    this.currentPage = page - 1; // CoreUI pagination is 1-based
    this.loadProducts();
  }

  getStockStatus(product: Product): string {
    if (product.currentStock <= 0) {
      return 'Out of Stock';
    }
    if (product.currentStock <= product.minimumStock) {
      return 'Low Stock';
    }
    return 'In Stock';
  }

  getStockStatusColor(product: Product): string {
    if (product.currentStock <= 0) {
      return 'danger';
    }
    if (product.currentStock <= product.minimumStock) {
      return 'warning';
    }
    return 'success';
  }

  addProduct() {
    this.router.navigate(['/products/create']);
  }

  editProduct(product: Product) {
    this.router.navigate(['/products/edit', product.id]);
  }

  deleteProduct(id: string) {
    if (confirm('Are you sure you want to delete this product?')) {
      this.productService.deleteProduct(id).subscribe({
        next: () => {
          this.loadProducts();
        },
        error: (error) => {
          console.error('Error deleting product:', error);
        }
      });
    }
  }
}
