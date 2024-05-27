import { CommonModule, CurrencyPipe } from '@angular/common';
import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FrameComponent } from '@components/frame/frame.component';
import { PlanetaryMarketResponse } from '@model/response/economy/planetary-market-response';
import { CargoItemResponse } from '@model/response/player/cargo-item-response';
import { CrewResponse } from '@model/response/player/crew-response';
import { AuthService } from '@services/auth.service';
import { CrewService } from '@services/crew.service';
import { EconomyService } from '@services/economy.service';
import e from 'express';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { InputNumberModule } from 'primeng/inputnumber';
import { ToastModule } from 'primeng/toast';
import { Nullable } from 'primeng/ts-helpers';
import { Observable, catchError, throwError } from 'rxjs';

@Component({
  selector: 'spacetrader-trading',
  standalone: true,
  imports: [FrameComponent, CommonModule, InputNumberModule, ButtonModule, ToastModule],
  templateUrl: './trading.component.html',
  styleUrl: './trading.component.scss',
  providers: [MessageService, CurrencyPipe]
})
export class TradingComponent implements OnInit{
  info$: Observable<CrewResponse> = new Observable
  market$: Observable<PlanetaryMarketResponse> = new Observable
  cargo$: Observable<CargoItemResponse[]> = new Observable

  constructor(private crewService: CrewService, private auth: AuthService,
    private economyService: EconomyService, private messageService: MessageService,
  private currencyPipe: CurrencyPipe){}

  ngOnInit(): void {
    this.updateMarket()
  }

  buy(id: number, quantity: Nullable<number>){
    if(quantity === null){
      return
    }
    const request = {productMarketId: id, quantity: quantity?.valueOf() as number}
    this.economyService.buy(request).subscribe({
      next: ()=>{
        this.messageService.add({
          severity: "success",
          summary: "Se ha comprado con éxito"
        })
        this.updateMarket()
      },
      error: (err: HttpErrorResponse) =>{
        if(err.status === HttpStatusCode.PreconditionFailed){
          this.messageService.add({
            severity: "error",
            summary: "No hay espacio en la nave"
          })  
        }
        this.messageService.add({
          severity: "error",
          summary: "No se ha podido comprar"
        })
      }
    })
  }

  sell(id: number, quantity: Nullable<number>){
    if(quantity === null){
      return
    }
    const request = {cargoItemId: id, quantity: quantity?.valueOf() as number}
    this.economyService.sell(request).subscribe({
      next: ()=>{
        this.messageService.add({
          severity: "success",
          summary: "Se ha vendido con éxito"
        })
        this.updateMarket()
      },
      error: (err: HttpErrorResponse) =>{
        this.messageService.add({
          severity: "error",
          summary: "No se ha podido vender"
        })
      }
    })
  }

  handleError(err: HttpErrorResponse){
    this.messageService.add({
      severity: "error",
      summary: "Ha habido un error al obtener la información"
    })
    return throwError(()=> err)
  }


  getLabel(text: string, price: number, quantity: Nullable<number>){
    if(quantity == null || quantity == undefined)
      return `${text} ${this.currencyPipe.transform(0)}`
    const q = quantity as number
    return `${text} ${this.currencyPipe.transform(price*q)}`
  }

  private updateMarket(){
    this.info$ = this.crewService.getCrewStatus().pipe(catchError(err => this.handleError(err)))
    this.market$ = this.economyService.getPlanetaryMarket().pipe(catchError(err => this.handleError(err)))
    this.cargo$ = this.economyService.calculateCargo().pipe(catchError( err => this.handleError(err)))
  }
}
