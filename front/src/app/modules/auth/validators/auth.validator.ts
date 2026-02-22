import { AbstractControl, ValidationErrors } from '@angular/forms';

export class AuthValidators {
  private static readonly EMAIL_REGEX = /^[^\s@]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

  static emailOrUsername(control: AbstractControl): ValidationErrors | null {
    const value = control.value as string;

    if (!value) return { required: true };

    if (value.includes('@')) {
      return AuthValidators.EMAIL_REGEX.test(value) ? null : { emailInvalid: true };
    }

    return null;
  }

  static email(control: AbstractControl): ValidationErrors | null {
    const value = control.value as string;

    if (!value) return { required: true };

    return AuthValidators.EMAIL_REGEX.test(value) ? null : { emailInvalid: true };
  }

  static password(control: AbstractControl): ValidationErrors | null {
    const value = control.value as string;

    if (!value) return { required: true };

    const hasLowercase = /[a-z]/.test(value);
    const hasUppercase = /[A-Z]/.test(value);
    const hasDigit = /\d/.test(value);
    const hasSpecial = /[^a-zA-Z0-9]/.test(value);
    const hasMinLength = value.length >= 8;

    const valid = hasLowercase && hasUppercase && hasDigit && hasSpecial && hasMinLength;

    return valid ? null : {
          passwordWeak: {
            hasLowercase,
            hasUppercase,
            hasDigit,
            hasSpecial,
            hasMinLength,
          },
    };
  }
}
