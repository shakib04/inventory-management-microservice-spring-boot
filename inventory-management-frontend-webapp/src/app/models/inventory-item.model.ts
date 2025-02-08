export interface InventoryItem {
  id?: string;
  productId: string;
  productName: string;
  warehouseId: string;
  quantity: number;
  reorderLevel: number;
  reorderQuantity: number;
  lastRestockDate: string;
}
