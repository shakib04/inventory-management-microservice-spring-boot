import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {ButtonModule, CardModule, FormModule, GridModule, SpinnerModule, TableModule} from '@coreui/angular';
import {ProductService} from "../../services/product.service";
import {OrderService} from "../../services/order.service";
import {Product} from "../../models/product.model";
import {Page} from "../../models/page.model";
import {OrderRequest} from "../../models/order.model";


@Component({
  selector: 'app-order-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CardModule,
    GridModule,
    FormModule,
    ButtonModule,
    SpinnerModule,
    TableModule
  ],
  templateUrl: `order-form.component.html`
})
export class OrderFormComponent implements OnInit {
  orderForm: FormGroup;
  loading = false;
  products: any[] = [];

  constructor(
    private fb: FormBuilder,
    private orderService: OrderService,
    private productService: ProductService,
    private router: Router
  ) {
    this.orderForm = this.fb.group({
      customerId: ['', Validators.required],
      items: this.fb.array([])
    });
  }

  get orderItems() {
    return this.orderForm.get('items') as FormArray;
  }

  ngOnInit() {
    this.loadProducts();
  }

  loadProducts() {
    this.productService.getProducts().subscribe({
      next: (products) => {
        this.products = products.content;
      },
      error: (error) => {
        console.error('Error loading products:', error);
      }
    });
  }

  addOrderItem() {
    const itemForm = this.fb.group({
      productId: ['', Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]]
    });

    this.orderItems.push(itemForm);
  }

  removeOrderItem(index: number) {
    this.orderItems.removeAt(index);
  }

  getProductPrice(productId: string): number {
    const product = this.products.find(p => p.id === productId);
    return product?.price || 0;
  }

  onProductSelect(event: any, index: number) {
    this.updateItemTotal(index);
  }

  updateItemTotal(index: number) {
    const item = this.orderItems.at(index);
    const productId = item.get('productId')?.value;
    const quantity = item.get('quantity')?.value || 0;
    const price = this.getProductPrice(productId);
  }

  calculateItemTotal(index: number): number {
    const item = this.orderItems.at(index);
    const productId = item.get('productId')?.value;
    const quantity = item.get('quantity')?.value || 0;
    return this.getProductPrice(productId) * quantity;
  }

  calculateOrderTotal(): number {
    return this.orderItems.controls.reduce((total, item) => {
      const productId = item.get('productId')?.value;
      const quantity = item.get('quantity')?.value || 0;
      return total + (this.getProductPrice(productId) * quantity);
    }, 0);
  }

  onSubmit() {
    if (this.orderForm.valid && this.orderItems.length > 0) {
      this.loading = true;

      const orderRequest: OrderRequest = {
        customerId: this.orderForm.get('customerId')?.value,
        items: this.orderItems.value
      };

      this.orderService.createOrder(orderRequest).subscribe({
        next: () => {
          this.router.navigate(['/orders']);
        },
        error: (error) => {
          console.error('Error creating order:', error);
          this.loading = false;
        }
      });
    }
  }

  onCancel() {
    this.router.navigate(['/orders']);
  }

  isFieldValid(field: string): boolean {
    const control = this.orderForm.get(field);
    return !control?.errors || !control?.touched;
  }

  isFieldInvalid(field: string): boolean {
    const control = this.orderForm.get(field);
    return !!control?.errors && control?.touched;
  }
}
