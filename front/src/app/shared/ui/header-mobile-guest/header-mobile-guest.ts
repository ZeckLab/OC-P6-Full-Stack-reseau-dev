import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';

/** Mobile-only header for guest routes, with a back button to return to home. */
@Component({
  selector: 'header-mobile-guest',
  imports: [],
  templateUrl: './header-mobile-guest.html',
  styleUrl: './header-mobile-guest.scss',
})
export class HeaderMobileGuest {
  private readonly router = inject(Router);

  goBack() {
    this.router.navigate(['/']);
  }
}
