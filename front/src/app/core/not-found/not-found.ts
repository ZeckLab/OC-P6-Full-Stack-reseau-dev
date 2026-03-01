import { Component } from "@angular/core";
import { RouterModule } from "@angular/router";

@Component({
  selector: 'app-not-found',
  imports: [RouterModule],
  template: `
    <div class="not-found">
      <h1>404</h1>
      <p>Page introuvable</p>
      <a routerLink="/">Retour à l’accueil</a>
    </div>
  `,
  styles: [`
    .not-found {
      text-align: center;
      margin-top: 4rem;
    }
    h1 {
      font-size: 4rem;
      margin-bottom: 1rem;
      color: #7763c5;
    }
    a {
      color: #7763c5;
      font-weight: 600;
    }
  `]
})
export class NotFound {}
