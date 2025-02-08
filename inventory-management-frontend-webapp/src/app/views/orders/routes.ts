import {Routes} from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        loadComponent: () => import('./order-list.component').then(m => m.OrderListComponent),
        data: {
          title: $localize`Orders`
        }
      },
      {
        path: 'create',
        loadComponent: () => import('./order-form.component').then(m => m.OrderFormComponent),
        data: {
          title: $localize`Create Order`
        }
      },
      {
        path: 'edit/:id',
        loadComponent: () => import('./order-form.component').then(m => m.OrderFormComponent),
        data: {
          title: $localize`Edit Order`
        }
      }
    ]
  }
];

