import { Component, input, output } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { FORM_MESSAGES } from '../../../../core/messages/form-messages';

/** Displays the editable profile form and emits a save event when valid. */
@Component({
  selector: 'app-account-info-edit',
  imports: [ReactiveFormsModule],
  templateUrl: './account-info-edit.html',
  styleUrls: ['./account-info-edit.scss'],
})
export class AccountInfoEdit {
  form = input.required<FormGroup>();
  canSave = input.required<boolean>();

  readonly FORM_MESSAGES = FORM_MESSAGES;

  save = output<void>();

  submit() {
    if (this.canSave()) {
      this.save.emit();
    }
  }
}
