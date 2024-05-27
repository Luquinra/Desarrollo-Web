import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { PlayerType } from '@model/auth/user-details';
import { AuthService } from '@services/auth.service';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'spacetrader-header',
  standalone: true,
  imports: [RouterModule, ButtonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  constructor(private auth: AuthService, private router: Router) { }

  logout() {
    this.auth.logout().subscribe({
      next: () => {
        this.router.navigate(["/login"])
      }
    })
  }

  getPlayerType(): PlayerType{
    return this.auth.userDetails?.role as PlayerType
  }
}
