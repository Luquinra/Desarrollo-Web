import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'spacetrader-e404',
  standalone: true,
  imports: [ButtonModule, RouterModule],
  templateUrl: './e404.component.html',
  styleUrl: './e404.component.scss'
})
export class E404Component {
}
