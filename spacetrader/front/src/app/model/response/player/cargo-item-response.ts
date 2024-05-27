import { ProductResponse } from "../economy/product-response"

export interface CargoItemResponse{
    id: number
    quantity: number
    product: ProductResponse
    sellPrice: number
}