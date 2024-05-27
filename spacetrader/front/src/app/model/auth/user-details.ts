export interface UserDetails{
    username: string
    role: PlayerType
    token: string
}

export type PlayerType = "PILOT" | "CAPTAIN" | "TRADER"