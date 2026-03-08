import { Component, computed, effect, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Users } from '../../services/users';
import { AuthValidators } from '../../../../shared/validators/auth.validator';
import { ToastService } from '../../../../shared/ui/toast/toast.service';
import { ErrorMapper } from '../../../../core/messages/error-mapper';
import { Topics } from '../../../topics/services/topics';
import { TopicList } from '../../../topics/components/list/topic-list';
import { toSignal } from '@angular/core/rxjs-interop';
import { UpdateUserRequest } from '../../models/user.request';
import { AccountInfoView } from '../../components/view/account-info-view';
import { AccountInfoEdit } from '../../components/edit/account-info-edit';
import { TopicWithSubscription } from '../../../topics/models/topic.response';

/** Displays and manages the user's profile, editable fields and topic subscriptions. */
@Component({
  selector: 'app-account',
  imports: [
    ReactiveFormsModule,
    TopicList,
    AccountInfoView,
    AccountInfoEdit
  ],
  templateUrl: './account.html',
  styleUrls: ['./account.scss'],
})
export class Account {
  private readonly fb = inject(FormBuilder);
  private readonly usersFacade = inject(Users);
  private readonly toast = inject(ToastService);
  private readonly errorMapper = inject(ErrorMapper);
  private readonly topicsFacade = inject(Topics);

  // All Topics with subscription status --> Only the topics the user is subscribed to
  topics = this.topicsFacade.topicsWithSubscription;
  subscribedTopics = computed(() => this.topics().filter((t) => t.subscribed));

  // User
  user = this.usersFacade.user;
  editMode = signal(false);

  // Form
  readonly form = this.fb.group({
    email: ['', [Validators.required, AuthValidators.email]],
    username: ['', [Validators.required, Validators.minLength(3)]],
    password: ['', AuthValidators.password(false)],
  });

  // Initial form values (used to detect modified fields)
  initialValue = this.form.getRawValue();

  // Form changes converted to a signal
  formChanges = toSignal(this.form.valueChanges, { initialValue: null });

  // Tracks which fields have been modified
  modifiedFields = signal<Set<keyof UpdateUserRequest>>(new Set());

  // Whether the form can be saved
  canSave = signal(false);

  constructor() {
    // Load topics and user profile on init
    this.topicsFacade.getAllWithSubscription().subscribe();
    this.usersFacade.loadProfile().subscribe();

    // --- Effect: update form when user data changes ---
    effect(() => {
      const u = this.user();
      if (!u) return;

      this.form.patchValue({
        email: u.email,
        username: u.username,
        password: '',
      });

      this.initialValue = this.form.getRawValue();
      this.modifiedFields.set(new Set());
      this.canSave.set(false);
      this.form.markAsPristine();
      this.form.markAsUntouched();
    });

    // --- Effect: detect modified fields and update canSave ---
    effect(() => {
      this.formChanges();
      const fields = Object.keys(this.initialValue) as (keyof UpdateUserRequest)[];
      const changed = new Set<keyof UpdateUserRequest>();

      for (const key of fields) {
        const current = this.form.controls[key].value;
        const initial = this.initialValue[key];
        if (current !== initial && current !== '') {
          changed.add(key);
        }
      }

      this.modifiedFields.set(changed);
      this.canSave.set(this.form.valid && changed.size > 0);
    });
  }

  enableEdit() {
    this.editMode.set(true);
  }

  // Saves modified fields and updates the profile
  save() {
    const patch: UpdateUserRequest = {};
    const controls = this.form.controls;

    this.modifiedFields().forEach((field) => {
      patch[field] = controls[field].value ?? undefined;
    });

    this.usersFacade.updateProfile(patch).subscribe({
      next: () => {
        this.toast.show('Profil mis à jour', 'success');
        this.usersFacade.loadProfile().subscribe();
        this.editMode.set(false);
      },
      error: (err) => {
        const errorMessage = this.errorMapper.mapIdentityError(err);
        this.toast.show(errorMessage, 'error');
      },
    });
  }

  // Handles topic subscription toggle
  onToggle(topic: TopicWithSubscription) {
    this.topicsFacade.toggleSubscription(topic).subscribe();
  }
}
