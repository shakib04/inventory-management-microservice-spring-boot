import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Router} from '@angular/router';
import {
  BadgeModule,
  ButtonModule,
  CardModule,
  FormControlDirective,
  FormLabelDirective,
  GridModule,
  PaginationModule,
  SpinnerModule,
  TableModule
} from '@coreui/angular';
import {InventoryItem} from "../../models/inventory-item.model";
import {InventoryService} from "../../services/inventory.service";
import {FormsModule} from "@angular/forms";
import {IconDirective} from "@coreui/icons-angular";


@Component({
  selector: 'app-inventory-list',
  standalone: true,
  imports: [
    CommonModule,
    CardModule,
    GridModule,
    TableModule,
    ButtonModule,
    BadgeModule,
    SpinnerModule,
    PaginationModule,
    FormsModule,
    FormControlDirective,
    FormLabelDirective,
    IconDirective
  ],
  templateUrl: 'inventory-item.component.html'
})
export class InventoryListComponent implements OnInit {
  items: InventoryItem[] = [];
  filteredItems: InventoryItem[] = [];
  loading = true;
  currentPage = 1;
  pageSize = 10;
  totalPages = 0;
  totalElements = 0;

  filters = {
    productName: '',
    quantityThreshold: null as number | null
  };

  constructor(
    private inventoryService: InventoryService,
    private router: Router
  ) {
  }

  ngOnInit() {
    this.loadItems();
  }

  loadItems() {
    this.loading = true;
    this.inventoryService.getItems(this.currentPage - 1, this.pageSize, this.filters).subscribe({
      next: (response) => {
        this.items = response.content;
        this.applyFilters();
        this.totalPages = response.totalPages;
        this.totalElements = response.totalElements;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading inventory items:', error);
        this.loading = false;
      }
    });
  }

  applyFilters() {
    this.currentPage = 1; // Reset to first page when filtering
    this.loadItems();
  }

  onFilterChange() {
    this.applyFilters();
  }

  resetFilters() {
    this.filters = {
      productName: '',
      quantityThreshold: null
    };
    this.applyFilters();
  }


  addItem() {
    this.router.navigate(['/inventory-items/create']);
  }

  editItem(item: InventoryItem) {
    this.router.navigate(['/inventory-items/edit', item.id]);
  }

  deleteItem(id: string | undefined) {
    if (id && confirm('Are you sure you want to delete this item?')) {
      this.inventoryService.deleteItem(id).subscribe({
        next: () => this.loadItems(),
        error: (error) => console.error('Error deleting item:', error)
      });
    }
  }

  onPageChange(page: number) {
    this.currentPage = page;
    this.loadItems();
  }
}
