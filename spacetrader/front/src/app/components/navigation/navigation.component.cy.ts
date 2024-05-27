import { WormholeResponse } from "@model/response/travel/wormhole-response"
import { appConfig } from "../../app.config"
import { NavigationComponent } from "./navigation.component"
import { PlanetResponse } from "@model/response/world/planet-response"
import { HttpStatusCode } from "@angular/common/http"

const navResponse: WormholeResponse[] = [
    {destinationStar: {id: 1, img: "assets/stars/148197341.gif", name: "testStar1", x:1 , y:1, z:1}, travelTime: 1},
    {destinationStar: {id: 2, img: "assets/stars/3095869067.gif", name: "testStar2", x:1 , y:1, z:1}, travelTime: 2},
    {destinationStar: {id: 3, img: "assets/stars/3663477929.gif", name: "testStar3", x:1 , y:1, z:1}, travelTime: 3},
    {destinationStar: {id: 4, img: "assets/stars/4213315666.gif", name: "testStar4", x:1 , y:1, z:1}, travelTime: 4},
    {destinationStar: {id: 5, img: "assets/stars/546101511.gif", name: "testStar5", x:1 , y:1, z:1}, travelTime: 5},
    {destinationStar: {id: 6, img: "assets/stars/1690261463.gif", name: "testStar6", x:1 , y:1, z:1}, travelTime: 6},
]

const planetNavResponse: PlanetResponse[] = [
    {id: 1, img: "assets/planets/1116035287.gif", inhabited: true, name: "testPlanet1"},
    {id: 2, img: "assets/planets/2909608145.gif", inhabited: true, name: "testPlanet2"},
    {id: 3, img: "assets/planets/3795658838.gif", inhabited: true, name: "testPlanet3"}
]

describe("Navigation component", ()=>{

    it("Stars should contain 6 childs", ()=>{
        cy.intercept({method: "GET", path:"/nav/status"}, {
            statusCode: HttpStatusCode.Ok,
            body: navResponse
        }).as("getStars")
        cy.intercept({method: "GET", path:"/nav/star_planets"}, {
            statusCode: HttpStatusCode.Ok,
            body: planetNavResponse
        })
        cy.mount(NavigationComponent,{providers:[appConfig.providers]})

        cy.get(':nth-child(2) > .star-container').children().should("have.length", 6)
    })

    it("Planets should contain 3 childs", ()=>{
        cy.intercept({method: "GET", path:"/nav/status"}, {
            statusCode: HttpStatusCode.Ok,
            body: navResponse
        }).as("getStars")
        cy.intercept({method: "GET", path:"/nav/star_planets"}, {
            statusCode: HttpStatusCode.Ok,
            body: planetNavResponse
        })
        cy.mount(NavigationComponent,{providers:[appConfig.providers]})

        cy.get(':nth-child(3) > .star-container').children().should("have.length", 3)
    })

    it("Star container should show a load text", ()=>{
        cy.intercept({method: "GET", path:"/nav/status"}, {
            statusCode: HttpStatusCode.Ok,
            body: navResponse,
            delay: 1000
        }).as("getStars")
        cy.intercept({method: "GET", path:"/nav/star_planets"}, {
            statusCode: HttpStatusCode.Ok,
            body: planetNavResponse,
        })
        cy.mount(NavigationComponent,{providers:[appConfig.providers]})
        cy.get('.wrapper > :nth-child(2) > .ng-star-inserted').contains("Cargando")
        cy.wait("@getStars")
    })

    it("Star container should show a load text", ()=>{
        cy.intercept({method: "GET", path:"/nav/status"}, {
            statusCode: HttpStatusCode.Ok,
            body: navResponse,
        }).as("getStars")
        cy.intercept({method: "GET", path:"/nav/star_planets"}, {
            statusCode: HttpStatusCode.Ok,
            body: planetNavResponse,
            delay: 1000
        })
        cy.mount(NavigationComponent,{providers:[appConfig.providers]})
        cy.get(':nth-child(3) > .ng-star-inserted').contains("Cargando")
        cy.wait("@getStars")
    })
})