import { LoginComponent } from "./login.component"
import { HttpClientTestingModule, provideHttpClientTesting } from '@angular/common/http/testing'
import { JWT_OPTIONS, JwtHelperService } from "@auth0/angular-jwt"

describe("LoginComponent", () => {
    beforeEach("Mount component", () => {
        cy.mount(LoginComponent, {imports:[HttpClientTestingModule],providers: [provideHttpClientTesting(),
            { provide: JWT_OPTIONS, useValue: JWT_OPTIONS }, JwtHelperService
        ] })
    })

    it("Login button should be disabled by default", () => {
        cy.get('p-button > button').should("be.disabled")
    })

    it("Button should activate when username and password are not blank", () => {
        cy.get("#username").type("user")
        cy.get('.p-password > .p-inputtext').type("123456")

        cy.get('p-button > button').should("be.enabled")
    })

    it("Login field should display a text if is invalid", () => {
        cy.get("#username").type("user").clear()
        cy.get(':nth-child(1) > p-message.p-element > .p-inline-message').should("be.visible")
    })

    it("Password field should display a text if is invalid", () => {
        cy.get('.p-password > .p-inputtext').type("123456").clear()
        cy.get(':nth-child(2) > p-message.p-element > .p-inline-message').should("be.visible")
    })
})
