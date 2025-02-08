import {Routes} from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        loadComponent: () => import('./inventory-item.component').then(m => m.InventoryListComponent),
        data: {
          title: $localize`Inventory Items`
        }
      },
      {
        path: 'create',
        loadComponent: () => import('./inventory-item-form.component').then(m => m.InventoryItemFormComponent),
        data: {
          title: $localize`Create Inventory Items`
        }
      },
      {
        path: 'edit/:id',
        loadComponent: () => import('./inventory-item-form.component').then(m => m.InventoryItemFormComponent),
        data: {
          title: $localize`Edit Inventory Items`
        }
      }
    ]
  }
];

