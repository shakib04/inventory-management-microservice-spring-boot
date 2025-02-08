export interface OrderItemRequest {
  productId: string;
  quantity: number;
}

export interface OrderRequest {
  customerId: string;
  items: OrderItemRequest[];
}
