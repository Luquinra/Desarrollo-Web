import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { TravelRequest } from '@model/request/travel/travel-request';
import { WormholeResponse } from '@model/response/travel/wormhole-response';
import { OtherCrewResponse } from '@model/response/world/other-crew-response';
import { PlanetResponse } from '@model/response/world/planet-response';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NavigationService {

  constructor(private http: HttpClient) { }

  getCrewsOnTheSameStar(): Observable<OtherCrewResponse[]>{
    return this.http.get<OtherCrewResponse[]>(`${environment.apiUrl}/nav/star_status`)
  }

  getClosestStars(): Observable<WormholeResponse[]>{
    return this.http.get<WormholeResponse[]>(`${environment.apiUrl}/nav/status`)
  }

  getStarPlanets(): Observable<PlanetResponse[]>{
    return this.http.get<PlanetResponse[]>(`${environment.apiUrl}/nav/star_planets`)
  }

  navigate(request: TravelRequest): Observable<any>{
    return this.http.post(`${environment.apiUrl}/nav/travel`, request)
  }
}
