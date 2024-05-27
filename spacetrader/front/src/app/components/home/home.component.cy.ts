import { HttpClientTestingModule } from "@angular/common/http/testing"
import { HomeComponent } from "./home.component"
import { CrewResponse } from "@model/response/player/crew-response"
import { OtherCrewResponse } from "@model/response/world/other-crew-response"
import { appConfig } from "../../app.config"
import { HttpStatusCode } from "@angular/common/http"

const crewWithPlanet: CrewResponse = {
    id: 1,
    actualCargoVolume: 5.5,
    cargo: [],
    credits: 1000,
    crewMembers: [
        { id: 1, img: "assets/portraits/portrait-1.jpg", role: "CAPTAIN", username: "testUser1" },
        { id: 2, img: "assets/portraits/portrait-2.jpg", role: "PILOT", username: "testUser2" },
        { id: 3, img: "assets/portraits/portrait-3.jpg", role: "TRADER", username: "testUser3" },
    ],
    planet: { id: 1, img: "assets/planets/1158715960.gif", inhabited: true, name: "testPlanet" },
    spaceship: { name: "testEnterprise", cargoCapacity: 15.0, img: "assets/spaceships/vehicle-2.png", maxSpeed: 123456789 },
    star: { id: 1, img: "assets/stars/546101511.gif", name: "testStar", x: 1, y: 1, z: 1 }
}

const crewWithoutPlanet: CrewResponse = {
    ...crewWithPlanet,
    planet: null as any,
}

const otherCrewResponse: OtherCrewResponse = {
    captain: { id: 5, img: "portrait-15.jpg", role: "CAPTAIN", username: "otherCaptain" },
    spaceship: { name: "test", cargoCapacity: 60, img: "assets/spaceships/vehicle-7.png", maxSpeed: 456 }
}

describe("HomeComponent", () => {

    it("On error should display toast", () => {
        Cypress.on('uncaught:exception', (err, runnable) => {
            return false
        })
        cy.intercept({ method: "GET", path: "/crew/mycrew" }, {
            statusCode: HttpStatusCode.Forbidden
        }).as("getCrewInfo")
        cy.intercept({ method: "GET", path: "/nav/star_status" }, {
            statusCode: HttpStatusCode.Forbidden
        }).as("getCrewInfo")
        cy.mount(HomeComponent, {
            imports: [HttpClientTestingModule], providers: [appConfig.providers]
        })

        cy.get('.p-toast-message-content').should("be.visible")
    })

    it("Should display correct information", () => {
        cy.intercept({ method: "GET", path: "/crew/mycrew" }, {
            statusCode: 200,
            body: crewWithPlanet
        }).as("getCrewInfo")
        cy.intercept({ method: "GET", path: "/nav/star_status" }, {
            statusCode: 200,
            body: [otherCrewResponse]
        }).as("getCrewInfo")
        cy.mount(HomeComponent, {
            imports: [HttpClientTestingModule], providers: [appConfig.providers]
        })

        cy.get('.planet-wrapper > .icon-wrapper > img').should("be.visible")
        cy.get('.star').should("be.visible")

        cy.get('.crewmembers-wrapper').children().should("have.length", 4)

        cy.get('.other-crew-info').contains("otherCaptain")
    })

    it("On planet null should display a text", ()=>{
        cy.intercept({ method: "GET", path: "/crew/mycrew" }, {
            statusCode: HttpStatusCode.Ok,
            body: crewWithoutPlanet
        }).as("getCrewInfo")
        cy.intercept({ method: "GET", path: "/nav/star_status" }, {
            statusCode: HttpStatusCode.Ok,
            body: [otherCrewResponse]
        }).as("getCrewInfo")
        cy.mount(HomeComponent, {
            imports: [HttpClientTestingModule], providers: [appConfig.providers]
        })

        cy.get(".planet-wrapper").contains("No estás en un planeta")
    })

    it("On otherCrew null should display a text", ()=>{
        cy.intercept({ method: "GET", path: "/crew/mycrew" }, {
            statusCode: HttpStatusCode.Ok,
            body: crewWithPlanet
        }).as("getCrewInfo")
        cy.intercept({ method: "GET", path: "/nav/star_status" }, {
            statusCode: HttpStatusCode.Ok,
            body: []
        }).as("getCrewInfo")
        cy.mount(HomeComponent, {
            imports: [HttpClientTestingModule], providers: [appConfig.providers]
        })

        cy.get(".cargo-wrapper").contains("No se ha detectado")
    })
})