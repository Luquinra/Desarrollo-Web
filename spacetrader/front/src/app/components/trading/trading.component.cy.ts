import { CrewResponse } from "@model/response/player/crew-response"
import { appConfig } from "../../app.config"
import { TradingComponent } from "./trading.component"
import { PlanetaryMarketResponse } from "@model/response/economy/planetary-market-response"
import { CargoItemResponse } from "@model/response/player/cargo-item-response"
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

const planetaryMarketResponse: PlanetaryMarketResponse = {
    demandFactor: 10000,
    offerFactor: 10000,
    products: [
        { id: 1, buyPrice: 15000, stock: 50, product: { id: 1, img: "assets/products/1.png", name: "testProduct1", volume: 12 } },
        { id: 2, buyPrice: 50000, stock: 20, product: { id: 2, img: "assets/products/2.png", name: "testProduct2", volume: 13 } },
        { id: 3, buyPrice: 90000, stock: 30, product: { id: 3, img: "assets/products/3.png", name: "testProduct3", volume: 14 } },
        { id: 4, buyPrice: 18000, stock: 40, product: { id: 4, img: "assets/products/4.png", name: "testProduct4", volume: 15 } },
        { id: 5, buyPrice: 20000, stock: 11, product: { id: 5, img: "assets/products/5.png", name: "testProduct5", volume: 16 } }
    ]
}

const cargoItemResponse: CargoItemResponse[] = [
    { id: 1, product: { id: 6, img: "assets/products/6.png", name: "testProduct6", volume: 20 }, quantity: 5, sellPrice: 123 },
    { id: 2, product: { id: 7, img: "assets/products/9.png", name: "testProduct7", volume: 220 }, quantity: 50, sellPrice: 1223 }
]

describe("TradingComponent", () => {
    it("All buttons should be disabled by default", () => {
        cy.intercept({ method: "GET", path: "/crew/mycrew" }, {
            statusCode: 200,
            body: crewWithPlanet
        }).as("getCrewInfo")

        cy.intercept({ method: "GET", path: "/economy/planet_market" }, {
            statusCode: 200,
            body: planetaryMarketResponse
        }).as("getMarketInfo")

        cy.intercept({ method: "GET", path: "/economy/calculate_cargo" }, {
            statusCode: 200,
            body: cargoItemResponse
        }).as("getMarketInfo")

        cy.mount(TradingComponent, { providers: [appConfig.providers] })

        cy.get(".product-wrapper p-button > button").each(($element, index, $list) => {
            cy.wrap($element).should("be.disabled")
        });
    })

    it("Increasing stock should enable button", () => {
        cy.intercept({ method: "GET", path: "/crew/mycrew" }, {
            statusCode: 200,
            body: crewWithPlanet
        }).as("getCrewInfo")

        cy.intercept({ method: "GET", path: "/economy/planet_market" }, {
            statusCode: 200,
            body: planetaryMarketResponse
        }).as("getMarketInfo")

        cy.intercept({ method: "GET", path: "/economy/calculate_cargo" }, {
            statusCode: 200,
            body: cargoItemResponse
        }).as("getMarketInfo")

        cy.mount(TradingComponent, { providers: [appConfig.providers] })

        cy.get(':nth-child(1) > .product-wrapper > :nth-child(1) > .p-inputwrapper > .p-inputnumber > .p-inputtext')
            .type("1")

        cy.get(':nth-child(1) > .product-wrapper > :nth-child(1) > p-button.p-element > .p-ripple').should("be.enabled")
    })

    it("Increasing stock should increase the display label", ()=>{
        cy.intercept({ method: "GET", path: "/crew/mycrew" }, {
            statusCode: 200,
            body: crewWithPlanet
        }).as("getCrewInfo")

        cy.intercept({ method: "GET", path: "/economy/planet_market" }, {
            statusCode: 200,
            body: planetaryMarketResponse
        }).as("getMarketInfo")

        cy.intercept({ method: "GET", path: "/economy/calculate_cargo" }, {
            statusCode: 200,
            body: cargoItemResponse
        }).as("getMarketInfo")

        cy.mount(TradingComponent, { providers: [appConfig.providers] })

        cy.get(':nth-child(1) > .product-wrapper > :nth-child(1) > .p-inputwrapper > .p-inputnumber > .p-inputtext')
            .type("2")

        cy.get(':nth-child(1) > .product-wrapper > :nth-child(1) > p-button.p-element > .p-ripple > .p-button-label')
            .contains("246")
    })

    it("Should display an error on buy or sell error", ()=>{
        cy.intercept({ method: "GET", path: "/crew/mycrew" }, {
            statusCode: 200,
            body: crewWithPlanet
        }).as("getCrewInfo")

        cy.intercept({ method: "GET", path: "/economy/planet_market" }, {
            statusCode: 200,
            body: planetaryMarketResponse
        }).as("getMarketInfo")

        cy.intercept({ method: "GET", path: "/economy/calculate_cargo" }, {
            statusCode: 200,
            body: cargoItemResponse
        }).as("getMarketInfo")

        cy.intercept({method: "POST", path: "/economy/sell"},{
            statusCode: HttpStatusCode.Conflict
        })

        cy.mount(TradingComponent, { providers: [appConfig.providers] })

        cy.get(':nth-child(1) > .product-wrapper > :nth-child(1) > .p-inputwrapper > .p-inputnumber > .p-inputtext')
            .type("1")

        cy.get(':nth-child(1) > .product-wrapper > :nth-child(1) > p-button.p-element > .p-ripple').click()

        cy.get('.p-toast-message-content').contains("No se ha podido vender")
    })

    it("Should display an error on buy or sell error", ()=>{
        cy.intercept({ method: "GET", path: "/crew/mycrew" }, {
            statusCode: 200,
            body: crewWithPlanet
        }).as("getCrewInfo")

        cy.intercept({ method: "GET", path: "/economy/planet_market" }, {
            statusCode: 200,
            body: planetaryMarketResponse
        }).as("getMarketInfo")

        cy.intercept({ method: "GET", path: "/economy/calculate_cargo" }, {
            statusCode: 200,
            body: cargoItemResponse
        }).as("getMarketInfo")

        cy.intercept({method: "POST", path: "/economy/buy"},{
            statusCode: HttpStatusCode.Conflict
        })

        cy.mount(TradingComponent, { providers: [appConfig.providers] })

        cy.get(':nth-child(2) > .product-wrapper > :nth-child(1) > .p-inputwrapper > .p-inputnumber > .p-inputtext')
            .type("5")

        cy.get(':nth-child(2) > .product-wrapper > :nth-child(1) > p-button.p-element > .p-ripple').click()

        cy.get('.p-toast-message-content').contains("No se ha podido comprar")
    })

    it("Should display a success test on sell", ()=>{
        cy.intercept({ method: "GET", path: "/crew/mycrew" }, {
            statusCode: 200,
            body: crewWithPlanet
        }).as("getCrewInfo")

        cy.intercept({ method: "GET", path: "/economy/planet_market" }, {
            statusCode: 200,
            body: planetaryMarketResponse
        }).as("getMarketInfo")

        cy.intercept({ method: "GET", path: "/economy/calculate_cargo" }, {
            statusCode: 200,
            body: cargoItemResponse
        }).as("getMarketInfo")

        cy.intercept({method: "POST", path: "/economy/sell"},{
            statusCode: HttpStatusCode.Accepted
        })

        cy.mount(TradingComponent, { providers: [appConfig.providers] })

        cy.get(':nth-child(1) > .product-wrapper > :nth-child(1) > .p-inputwrapper > .p-inputnumber > .p-inputtext')
            .type("1")

        cy.get(':nth-child(1) > .product-wrapper > :nth-child(1) > p-button.p-element > .p-ripple').click()

        cy.get('.p-toast-message-content').contains("Se ha vendido con éxito")
    })

    it("Should display a success test on sell", ()=>{
        cy.intercept({ method: "GET", path: "/crew/mycrew" }, {
            statusCode: 200,
            body: crewWithPlanet
        }).as("getCrewInfo")

        cy.intercept({ method: "GET", path: "/economy/planet_market" }, {
            statusCode: 200,
            body: planetaryMarketResponse
        }).as("getMarketInfo")

        cy.intercept({ method: "GET", path: "/economy/calculate_cargo" }, {
            statusCode: 200,
            body: cargoItemResponse
        }).as("getMarketInfo")

        cy.intercept({method: "POST", path: "/economy/buy"},{
            statusCode: HttpStatusCode.Accepted
        })

        cy.mount(TradingComponent, { providers: [appConfig.providers] })

        cy.get(':nth-child(2) > .product-wrapper > :nth-child(1) > .p-inputwrapper > .p-inputnumber > .p-inputtext')
            .type("5")

        cy.get(':nth-child(2) > .product-wrapper > :nth-child(1) > p-button.p-element > .p-ripple').click()

        cy.get('.p-toast-message-content').contains("Se ha comprado con éxito")
    })

    it("Should display basic info", ()=>{
        cy.intercept({ method: "GET", path: "/crew/mycrew" }, {
            statusCode: 200,
            body: crewWithPlanet
        }).as("getCrewInfo")

        cy.intercept({ method: "GET", path: "/economy/planet_market" }, {
            statusCode: 200,
            body: planetaryMarketResponse
        }).as("getMarketInfo")

        cy.intercept({ method: "GET", path: "/economy/calculate_cargo" }, {
            statusCode: 200,
            body: cargoItemResponse
        }).as("getMarketInfo")
        cy.mount(TradingComponent, { providers: [appConfig.providers] })

        cy.get(':nth-child(1) > h2').contains("10000")

        cy.get('.individual-info > :nth-child(2) > h2').contains("10000")
        cy.get('.individual-info > :nth-child(3) > h2').contains("$1,000.00")
        cy.get('.individual-info > :nth-child(4) > h2').contains("5.5/15")
    })
})