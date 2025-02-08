import {Component} from '@angular/core';
import {IconDirective, IconSetService} from '@coreui/icons-angular';
import {
  ButtonDirective,
  CardBodyComponent,
  CardComponent,
  ColComponent,
  ContainerComponent,
  FormControlDirective,
  FormDirective,
  InputGroupComponent,
  InputGroupTextDirective,
  RowComponent,
  TextColorDirective
} from '@coreui/angular';
import {AbstractControl, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthService} from "../../../services/auth.service";
import {Router} from "@angular/router";
import {cilArrowRight, cilLockLocked, cilUser, cilWarning} from "@coreui/icons";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
  standalone: true,
  imports: [ContainerComponent, RowComponent, ColComponent, TextColorDirective, CardComponent, CardBodyComponent, FormDirective, InputGroupComponent, InputGroupTextDirective, IconDirective, FormControlDirective, ButtonDirective, FormsModule, ReactiveFormsModule]
})
export class RegisterComponent {

  registerForm: FormGroup
  errorMessage: string | null = null;

  constructor(private fb: FormBuilder, private authService: AuthService, private iconSet: IconSetService,
              private router: Router) {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', [
        Validators.required,
        // Validators.minLength(8),
        // Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/)
      ]],
      repeatPassword: ['', Validators.required],
      emailId: ['', Validators.required],
    });

    this.registerForm.addValidators(this.isPasswordMatched())
    // Register CoreUI icons
    this.iconSet.icons = {cilUser, cilLockLocked, cilArrowRight, cilWarning};
  }

  onSubmit() {
    if (this.registerForm.valid) {
      this.authService.register(this.registerForm.value).subscribe({
        next: (response) => {
          console.log('Authentication successful:', response);
          // Handle success (e.g., store token, redirect)
          this.router.navigate(['/']);
        },
        error: (error) => {
          console.error('Authentication failed:', error);
          this.errorMessage = 'Invalid credentials';
        }
      });
    }
  }

  isPasswordMatched() {
    return (control: AbstractControl) => {
      const password = control.get('password');
      const confirmPassword = control.get('repeatPassword');

      if (!password || !confirmPassword) return null;

      if (password.value !== confirmPassword.value) {
        confirmPassword.setErrors({ passwordMismatch: true });
        return { passwordMismatch: true };
      }

      return null;
    };
  }
}
