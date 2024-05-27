import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { BuyRequest } from '@model/request/economy/buy-request';
import { SellRequest } from '@model/request/economy/sell-request';
import { PlanetaryMarketResponse } from '@model/response/economy/planetary-market-response';
import { CargoItemResponse } from '@model/response/player/cargo-item-response';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EconomyService {
  
  constructor(private http: HttpClient) { }
  
  getPlanetaryMarket(): Observable<PlanetaryMarketResponse>{
    return this.http.get<PlanetaryMarketResponse>(`${environment.apiUrl}/economy/planet_market`)
  }
  
  buy(request: BuyRequest): Observable<any>{
    return this.http.post(`${environment.apiUrl}/economy/buy`, request)
  }
  
  sell(request: SellRequest): Observable<any>{
    return this.http.post(`${environment.apiUrl}/economy/sell`, request)
  }
  calculateCargo(): Observable<CargoItemResponse[]> {
    return this.http.get<CargoItemResponse[]>(`${environment.apiUrl}/economy/calculate_cargo`)
  }
}
