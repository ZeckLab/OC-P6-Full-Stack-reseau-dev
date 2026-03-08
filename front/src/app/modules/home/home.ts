import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

/** Public landing page with logo and navigation to authentication. */
@Component({
  selector: 'app-home',
  imports: [RouterModule],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {

}
