import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FrameComponent } from '@components/frame/frame.component';
import { CrewResponse } from '@model/response/player/crew-response';
import { OtherCrewResponse } from '@model/response/world/other-crew-response';
import { AuthService } from '@services/auth.service';
import { CrewService } from '@services/crew.service';
import { NavigationService } from '@services/navigation.service';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { Observable, catchError, throwError } from 'rxjs';

@Component({
  selector: 'spacetrader-home',
  standalone: true,
  imports: [CommonModule, FrameComponent, ToastModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  providers: [MessageService]
})
export class HomeComponent implements OnInit {

  info$: Observable<CrewResponse> = new Observable
  otherCrews$: Observable<OtherCrewResponse[]> = new Observable

  constructor(private crewService: CrewService, private auth: AuthService,
    private navigationService: NavigationService, private messageService: MessageService) { }

  ngOnInit(): void {
    this.info$ = this.crewService.getCrewStatus().pipe(catchError((err: HttpErrorResponse) => {
      this.messageService.add({
        severity: "error",
        summary: "Ha habido un error al obtener info"
      })
      return throwError(() => err)
    }))
    this.otherCrews$ = this.navigationService.getCrewsOnTheSameStar().pipe(catchError((err: HttpErrorResponse) => {
      this.messageService.add({
        severity: "error",
        summary: "Ha habido un error al obtener info"
      })
      return throwError(() => err)
    }))
  }

  getUsername() {
    return this.auth.userDetails?.username
  }
}
