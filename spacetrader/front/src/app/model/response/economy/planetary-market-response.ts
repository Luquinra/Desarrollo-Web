import { PlanetaryProductResponse } from "./planetary-product-response";

export interface PlanetaryMarketResponse{
    products: PlanetaryProductResponse[]
    demandFactor: number
    offerFactor: number
}