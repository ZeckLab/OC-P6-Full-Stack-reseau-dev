import { Component, inject } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Auth } from '../../auth';
import { AuthValidators } from '../../validators/auth.validator';
import { ErrorMapper } from '../../../../core/messages/error-mapper';
import { ToastService } from '../../../../shared/ui/toast/toast.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.scss'
})
export class Register {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly auth = inject(Auth);
  private readonly errorMapper = inject(ErrorMapper);
  private readonly toast = inject(ToastService);

  // Reactive registration form with custom validators
  readonly form = this.fb.group({
    email: ['', AuthValidators.email],
    username: ['', [Validators.required, Validators.minLength(3)]],
    password: ['', AuthValidators.password],
  });

  // Handles form submission and triggers registration
  submit() {
    if (this.form.invalid) return;

    const { email, username, password } = this.form.getRawValue();

    this.auth.register({
      email: email!,
      username: username!,
      password: password!
    }).subscribe({
      next: () => this.toast.show('Inscription réussie ! Connectez-vous maintenant.', 'success'),
      error: (err) => {
        const errorMessage = this.errorMapper.mapIdentityError(err);
        this.toast.show(errorMessage, 'error');
      }
    });
  }

  // Navigate back to the home page
  goBack() {
    this.router.navigate(['/']);
  }
}