import { PlayerResponse } from "../player/player-response";
import { SpaceShipResponse } from "../player/spaceship-response";

export interface OtherCrewResponse{
    spaceship: SpaceShipResponse
    captain: PlayerResponse
}