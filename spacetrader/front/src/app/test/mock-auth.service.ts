import { PlayerType, UserDetails } from "@model/auth/user-details"
import { Ripple } from "primeng/ripple"

export class MockAuthService {
    userData:UserDetails

    constructor(role: PlayerType = "CAPTAIN"){
        this.userData = { username:"testUser", token:"jwt", role: role}
    }

    public get userDetails() {
        return this.userData
    }

    public get isLogged() {
        return this.userData !== null
    }

    public set role(role: PlayerType){
        this.userData.role = role
    }
}