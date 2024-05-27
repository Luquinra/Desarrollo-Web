export interface TravelRequest{
    starOrPlanetId: number
    type: TravelType
}

export type TravelType = "PLANET" | "STAR"