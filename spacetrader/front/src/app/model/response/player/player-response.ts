import { PlayerType } from "@model/auth/user-details";

export interface PlayerResponse{
    id: number
    role: PlayerType
    username: string
    img: string
}