import { Routes } from '@angular/router';
import { authGuard } from '@guards/auth.guard';
import { pilotGuard } from '@guards/pilot.guard';
import { traderGuard } from '@guards/trader.guard';

export const routes: Routes = [
    { path: "", redirectTo: "login", pathMatch: "full" },
    { path: "login", loadComponent: () => import("@components/login/login.component").then(c => c.LoginComponent), title: "Iniciar sesión" },
    {
        path: "home", loadComponent: () => import("@components/home/home.component")
            .then(c => c.HomeComponent), title: "Inicio", canActivate: [authGuard]
    },
    { path: "e404", loadComponent: () => import("@components/e404/e404.component").then(c => c.E404Component), title: "404" },
    {
        path: "trade", loadComponent: () => import("@components/trading/trading.component")
            .then(c => c.TradingComponent), title: "Comercio", canActivate: [authGuard, traderGuard]
    },
    {
        path: "navigate", loadComponent: () => import("@components/navigation/navigation.component")
            .then(c => c.NavigationComponent), title: "Navegación", canActivate: [authGuard, pilotGuard]
    },
    { path: "**", redirectTo: "e404" }
];
