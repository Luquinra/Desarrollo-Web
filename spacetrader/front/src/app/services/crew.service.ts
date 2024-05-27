import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { CrewResponse } from '@model/response/player/crew-response';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CrewService {

  constructor(private http: HttpClient) { }

  getCrewStatus(): Observable<CrewResponse>{
    return this.http.get<CrewResponse>(`${environment.apiUrl}/crew/mycrew`)
  }
}
