import { HttpClientTestingModule, provideHttpClientTesting } from "@angular/common/http/testing"
import { JWT_OPTIONS, JwtHelperService } from "@auth0/angular-jwt"
import { HeaderComponent } from "./header.component"
import { MockAuthService } from "@testing/mock-auth.service"
import { AuthService } from "@services/auth.service"
import { provideRouter } from "@angular/router"

describe("HeaderComponent", () => {

    let mockAuthService: MockAuthService

    beforeEach("Mount component", () => {
        mockAuthService = new MockAuthService();
    })

    it("Should show 'navigation' and 'trading only for captains'", () => {
        cy.mount(HeaderComponent, {
            imports: [HttpClientTestingModule], providers: [provideHttpClientTesting(),
            { provide: JWT_OPTIONS, useValue: JWT_OPTIONS }, JwtHelperService,
            { provide: AuthService, useValue: mockAuthService },
            provideRouter([])
            ]
        })
        cy.get(':nth-child(2) > .nav-item').should("be.visible")
        cy.get(':nth-child(3) > .nav-item').should("be.visible")
    })

    it("Should show 'navigation' only for pilots", ()=>{
        mockAuthService.role = "PILOT"
        cy.mount(HeaderComponent, {
            imports: [HttpClientTestingModule], providers: [provideHttpClientTesting(),
            { provide: JWT_OPTIONS, useValue: JWT_OPTIONS }, JwtHelperService,
            { provide: AuthService, useValue: mockAuthService },
            provideRouter([])
            ]
        })
        cy.get(':nth-child(2) > .nav-item').contains("Navegar").should("be.visible")
        cy.get(':nth-child(3) > .nav-item').should("not.exist")
    })

    it("Should show 'navigation' only for pilots", ()=>{
        mockAuthService.role = "TRADER"
        cy.mount(HeaderComponent, {
            imports: [HttpClientTestingModule], providers: [provideHttpClientTesting(),
            { provide: JWT_OPTIONS, useValue: JWT_OPTIONS }, JwtHelperService,
            { provide: AuthService, useValue: mockAuthService },
            provideRouter([])
            ]
        })
        cy.get(':nth-child(2) > .nav-item').contains("Comerciar").should("be.visible")
        cy.get(':nth-child(3) > .nav-item').should("not.exist")
    })
})
