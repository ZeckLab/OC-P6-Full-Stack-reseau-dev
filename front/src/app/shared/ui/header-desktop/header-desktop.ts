import { Component, inject, input } from '@angular/core';
import { Auth } from '../../../modules/auth/services/auth';
import { RouterModule } from '@angular/router';

/** Desktop header displaying navigation links when the user is authenticated. */
@Component({
  selector: 'header-desktop',
  imports: [RouterModule],
  templateUrl: './header-desktop.html',
  styleUrl: './header-desktop.scss',
})
export class HeaderDesktop {
  private readonly auth = inject(Auth);

  // Whether the user is authenticated (passed from parent layout)
  isAuthenticated = input<boolean>(false);

  logout() {
    this.auth.logout();
  }
}
