import {Routes} from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        loadComponent: () => import('./product.component').then(m => m.ProductComponent),
        data: {
          title: $localize`Products`
        }
      },
      {
        path: 'create',
        loadComponent: () => import('./product-form.component').then(m => m.ProductFormComponent),
        data: {
          title: $localize`Create Product`
        }
      },
      {
        path: 'edit/:id',
        loadComponent: () => import('./product-form.component').then(m => m.ProductFormComponent),
        data: {
          title: $localize`Edit Product`
        }
      }
    ]
  }
];

