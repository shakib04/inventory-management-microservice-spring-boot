export interface Product {
  id: string;
  sku: string;
  name: string;
  description: string;
  category: string;
  unitPrice: number;
  minimumStock: number;
  currentStock: number;
}
