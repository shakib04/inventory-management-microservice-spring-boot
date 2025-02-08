import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {
  CardModule,
  GridModule,
  FormModule,
  ButtonModule,
  SpinnerModule
} from '@coreui/angular';
import {InventoryService} from "../../services/inventory.service";

@Component({
  selector: 'app-inventory-form',
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
  templateUrl: `inventory-item-form.component.html`
})
export class InventoryItemFormComponent implements OnInit {
  itemForm: FormGroup;
  isEditMode = false;
  loading = false;
  itemId?: string;

  constructor(
    private fb: FormBuilder,
    private inventoryService: InventoryService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.itemForm = this.fb.group({
      productId: ['', Validators.required],
      warehouseId: ['', Validators.required],
      quantity: [0, [Validators.required, Validators.min(0)]],
      reorderLevel: [0, [Validators.required, Validators.min(0)]],
      reorderQuantity: [0, [Validators.required, Validators.min(0)]]
    });
  }

  ngOnInit() {
    this.itemId = this.route.snapshot.paramMap.get('id') ?? undefined;
    if (this.itemId) {
      this.isEditMode = true;
      this.loadItem();
    }
  }

  loadItem() {
    if (this.itemId) {
      this.loading = true;
      this.inventoryService.getItem(this.itemId).subscribe({
        next: (item) => {
          this.itemForm.patchValue(item);
          this.loading = false;
        },
        error: (error) => {
          console.error('Error loading item:', error);
          this.loading = false;
        }
      });
    }
  }

  onSubmit() {
    if (this.itemForm.valid) {
      this.loading = true;
      const itemData = this.itemForm.value;

      const request = this.isEditMode && this.itemId
        ? this.inventoryService.updateItem(this.itemId, itemData)
        : this.inventoryService.createItem(itemData);

      request.subscribe({
        next: () => {
          this.router.navigate(['/inventory']);
        },
        error: (error) => {
          console.error('Error saving item:', error);
          this.loading = false;
        }
      });
    }
  }

  onCancel() {
    this.router.navigate(['/inventory']);
  }

  isFieldValid(field: string): boolean {
    const control = this.itemForm.get(field);
    return !control?.errors || !control?.touched;
  }

  isFieldInvalid(field: string): boolean {
    const control = this.itemForm.get(field);
    return !!control?.errors && control?.touched;
  }
}
