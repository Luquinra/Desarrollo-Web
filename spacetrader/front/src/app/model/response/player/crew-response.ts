import { PlanetResponse } from "../world/planet-response";
import { StarResponse } from "../world/star-response";
import { CargoItemResponse } from "./cargo-item-response";
import { PlayerResponse } from "./player-response";
import { SpaceShipResponse } from "./spaceship-response";

export interface CrewResponse{
    id: number
    crewMembers: PlayerResponse[]
    spaceship: SpaceShipResponse
    credits: number
    cargo: CargoItemResponse[]
    star: StarResponse
    planet: PlanetResponse
    actualCargoVolume: number
}