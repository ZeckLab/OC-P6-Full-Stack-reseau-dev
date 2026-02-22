import { Component, inject, input } from '@angular/core';
import { Auth } from '../../../modules/auth/auth';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'header-desktop',
  imports: [RouterModule],
  templateUrl: './header-desktop.html',
  styleUrl: './header-desktop.scss',
})
export class HeaderDesktop {
  private readonly auth = inject(Auth);

  isAuthenticated = input<boolean>(false);

  logout() {
    this.auth.logout();
  }
}
