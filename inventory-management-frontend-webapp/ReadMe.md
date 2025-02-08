# Inventory Management System - Frontend Documentation

## Overview
A web-based inventory management system built with Angular and CoreUI, featuring order management, product tracking, and inventory control.

## Technology Stack
- Angular 19
- Angular 19 requires Node.js LTS version ^18.19.1 or ^20.11.1
- CoreUI Angular Components
- JWT Authentication
- RESTful API Integration

## Features
### Authentication
- JWT-based authentication
- Role-based access control
- Secure password management

### Order Management
- Create new orders
- View order list in card layout
- Order status tracking
- Order item management
- Pagination and filtering

### Product Management
- Product CRUD operations
- Product list with filtering
- Stock level monitoring
- Product categorization

### Inventory Control
- Stock tracking
- Reorder level management
- Stock alerts
- Inventory history

## Project Structure
```
src/
├── app/
│   ├── auth/
│   │   ├── guards/
│   │   ├── services/
│   │   └── components/
│   ├── orders/
│   │   ├── components/
│   │   ├── models/
│   │   └── services/
│   ├── products/
│   │   ├── components/
│   │   ├── models/
│   │   └── services/
│   ├── shared/
│   │   ├── components/
│   │   └── services/
│   └── core/
│       ├── interceptors/
│       └── services/
```

## Installation
1. Clone the repository
```bash
git clone <repository-url>
```

2. Install dependencies
```bash
npm install
```

3. Start development server
```bash
ng serve
```

## API Integration
The application connects to backend services at `/api` endpoints:
- `/api/auth` - Authentication endpoints
- `/api/orders` - Order management
- `/api/products` - Product management
- `/api/inventory` - Inventory management

## Components

### Order Components
- `OrderListComponent` - Displays orders in card layout
- `OrderFormComponent` - Create/Edit orders
- Models:
  ```typescript
  interface Order {
    id: string;
    orderNumber: string;
    status: OrderStatus;
    totalAmount: number;
    items: OrderItem[];
  }
  ```

### Product Components
- `ProductListComponent` - Product management interface
- `ProductFormComponent` - Product creation/editing
- Models:
  ```typescript
  interface Product {
    id: string;
    sku: string;
    name: string;
    description: string;
    category: string;
    unitPrice: number;
    minimumStock: number;
    currentStock: number;
  }
  ```

### Authentication
- JWT token management
- Role-based route guards
- Interceptors for API requests

## Services

### AuthService
Handles user authentication and token management:
```typescript
class AuthService {
  login(credentials: any): Observable<any>
  logout(): void
  getToken(): string
  isAuthenticated(): boolean
}
```

### OrderService
Manages order operations:
```typescript
class OrderService {
  getOrders(page: number, size: number): Observable<any>
  createOrder(order: OrderRequest): Observable<any>
  updateOrder(id: string, order: any): Observable<any>
  deleteOrder(id: string): Observable<void>
}
```

## Styling
- CoreUI components for consistent UI
- Responsive design
- Custom SCSS for component styling

## Error Handling
- HTTP interceptors for error catching
- User-friendly error messages
- Form validation

## Security
- JWT token authentication
- Route guards for protected routes
- XSS protection
- CSRF protection

## Future Enhancements
1. Advanced filtering options
2. Export functionality
3. Real-time stock updates
4. Dashboard analytics
5. Batch operations

## Contributing
1. Fork the repository
2. Create a feature branch
3. Commit changes
4. Push to the branch
5. Create a Pull Request

## License
MIT License

Let me know if you need any clarification or have questions!
