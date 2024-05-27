import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FrameComponent } from '@components/frame/frame.component';
import { TravelType } from '@model/request/travel/travel-request';
import { WormholeResponse } from '@model/response/travel/wormhole-response';
import { PlanetResponse } from '@model/response/world/planet-response';
import { NavigationService } from '@services/navigation.service';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { Observable } from 'rxjs';

@Component({
  selector: 'spacetrader-navigation',
  standalone: true,
  imports: [CommonModule, FrameComponent, ToastModule],
  templateUrl: './navigation.component.html',
  styleUrl: './navigation.component.scss',
  providers: [MessageService]
})
export class NavigationComponent implements OnInit{

  closestStars$: Observable<WormholeResponse[]> = new Observable
  starPlanets$: Observable<PlanetResponse[]> = new Observable

  constructor(private navigationService: NavigationService, private messageService: MessageService){}

  ngOnInit(): void {
    this.closestStars$ = this.navigationService.getClosestStars()
    this.starPlanets$ = this.navigationService.getStarPlanets()
  }

  navigate(id: number, type: TravelType){
    this.navigationService.navigate({starOrPlanetId: id, type: type}).subscribe({
      next: ()=>{
        this.messageService.add({
          severity: "success",
          summary: "Se ha navegado al destino correctamente"
        })
        setTimeout(()=> window.location.reload(), 1000)
      },
      error: (err: HttpErrorResponse)=>{
        this.messageService.add({
          severity: "error",
          summary: "No se ha podido navegar al destino"
        })
      }
    })
  }

}
