import { Component, input, output } from '@angular/core';
import { User } from '../../models/user.model';

/** Displays the user's profile information in read-only mode. */
@Component({
  selector: 'app-account-info-view',
  template: `
    <div class="profile-view">
      <p><strong>Nom d'utilisateur :</strong> {{ user()?.username }}</p>
      <p><strong>Email :</strong> {{ user()?.email }}</p>
      <p><strong>Mot de passe :</strong> ********</p>

      <button (click)="edit.emit()">Modifier</button>
    </div>
  `,
  styles: [
    `
      .profile-view {
        margin: 0.5rem 0;
        padding-top: 1rem;
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 0.5rem;

        p {
          font-size: 1.1rem;
          text-align: center;
          margin: 0.3rem;
        }
      }

      button {
        margin-top: 1rem;
        padding: 0.8rem 1.2rem;
        border-radius: 0.5rem;
        background: #7763c5;
        color: white;
        font-weight: 600;
        border: none;
        cursor: pointer;
      }
    `,
  ],
})
export class AccountInfoView {
  user = input<User | null>();
  edit = output<void>();
}
