import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Auth } from '../../services/auth';
import { Router } from '@angular/router';
import { AuthValidators } from '../../../../shared/validators/auth.validator';
import { ToastService } from '../../../../shared/ui/toast/toast.service';
import { ErrorMapper } from '../../../../core/messages/error-mapper';
import { FORM_MESSAGES } from '../../../../core/messages/form-messages';

/** Login page: handles user authentication and form validation. */
@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(Auth);
  private readonly router = inject(Router);
  private readonly errorMapper = inject(ErrorMapper);
  private readonly toast = inject(ToastService);

  readonly FORM_MESSAGES = FORM_MESSAGES;

  // Reactive login form with custom validators
  readonly form = this.fb.group({
    emailOrUsername: ['', [AuthValidators.emailOrUsername]],
    password: ['', Validators.required],
  });

  /** Submits login credentials and handles success/error feedback. */
  submit() {
    if (this.form.invalid) return;

    const { emailOrUsername, password } = this.form.getRawValue();

    this.auth.login({
        username: emailOrUsername!,
        password: password!,
      })
      .subscribe({
        next: () => {
          // Show success toast on successful login
          this.toast.show('Connexion réussie', 'success');
        },
        error: (err) => {
          // Map backend error to a readable message
          const msg = this.errorMapper.mapIdentityError(err);
          this.toast.show(msg, 'error');
        },
      });
  }

  /** Navigates back to the home page. */
  goBack() {
    this.router.navigate(['/']);
  }
}
