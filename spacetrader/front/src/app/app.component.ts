import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'spacetrader-root',
  standalone: true,
  imports: [RouterOutlet],
  template: `
    <div class="stars"></div>
    <div class="twinkling"></div>
    <div class="clouds"></div>
    <router-outlet/>
  `,
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'spacetrader';
}
