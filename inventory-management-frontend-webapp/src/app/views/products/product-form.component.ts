import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {ButtonModule, CardModule, FormModule, GridModule, SpinnerModule} from '@coreui/angular';
import {ProductService} from "../../services/product.service";


@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CardModule,
    GridModule,
    FormModule,
    ButtonModule,
    SpinnerModule
  ],
  templateUrl: './product-form.component.html'
})
export class ProductFormComponent implements OnInit {
  productForm: FormGroup;
  isEditMode = false;
  loading = false;
  productId?: string;

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.productForm = this.fb.group({
      sku: ['', Validators.required],
      name: ['', Validators.required],
      description: ['', Validators.required],
      category: ['', Validators.required],
      unitPrice: [0, [Validators.required, Validators.min(0)]],
      minimumStock: [0, [Validators.required, Validators.min(0)]],
      currentStock: [0, [Validators.required, Validators.min(0)]]
    });
  }

  ngOnInit() {
    this.productId = this.route.snapshot.paramMap.get('id') ?? undefined;
    if (this.productId) {
      this.isEditMode = true;
      this.loadProduct();
    }
  }

  loadProduct() {
    if (this.productId) {
      this.loading = true;
      this.productService.getProduct(this.productId).subscribe({
        next: (product) => {
          this.productForm.patchValue(product);
          this.loading = false;
        },
        error: (error) => {
          console.error('Error loading product:', error);
          this.loading = false;
        }
      });
    }
  }

  onSubmit() {
    if (this.productForm.valid) {
      this.loading = true;
      const productData = this.productForm.value;

      const request = this.isEditMode && this.productId
        ? this.productService.updateProduct(this.productId, productData)
        : this.productService.createProduct(productData);

      request.subscribe({
        next: () => {
          this.router.navigate(['/products']);
        },
        error: (error) => {
          console.error('Error saving product:', error);
          this.loading = false;
        }
      });
    } else {
      this.markFormGroupTouched(this.productForm);
    }
  }

  onCancel() {
    this.router.navigate(['/products']);
  }

  isFieldValid(field: string): boolean {
    const control = this.productForm.get(field);
    return !control?.errors || !control?.touched;
  }

  isFieldInvalid(field: string): boolean {
    const control = this.productForm.get(field);
    return !!control?.errors && control?.touched;
  }

  private markFormGroupTouched(formGroup: FormGroup) {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();
      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }
}
