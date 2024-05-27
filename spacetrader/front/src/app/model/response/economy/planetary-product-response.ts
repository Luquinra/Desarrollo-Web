import { ProductResponse } from "./product-response"

export interface PlanetaryProductResponse{
    id: number
    product: ProductResponse
    stock: number
    buyPrice: number
}