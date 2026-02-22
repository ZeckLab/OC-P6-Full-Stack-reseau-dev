import { Component, inject, signal } from "@angular/core";
import { HeaderResolver } from "./shared/ui/header-resolver/header-resolver";
import { Toast } from './shared/ui/toast/toast';
import { Router, RouterOutlet } from "@angular/router";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, HeaderResolver, Toast],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('mdd-front');
  private readonly router = inject(Router);


showHeader = signal(false);

  constructor() {
    this.router.events.subscribe(() => {
      const url = this.router.url;

      // List of routes where the header should be hidden : home page
      const noHeaderRoutes = ['/'];

      this.showHeader.set(!noHeaderRoutes.includes(url));
    });

  }
}
