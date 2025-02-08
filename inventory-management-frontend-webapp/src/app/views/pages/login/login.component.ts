import {Component} from '@angular/core';
import {NgStyle} from '@angular/common';
import {IconDirective, IconSetService} from '@coreui/icons-angular';
import {
  ButtonDirective,
  CardBodyComponent,
  CardComponent,
  CardGroupComponent,
  ColComponent,
  ContainerComponent,
  FormControlDirective,
  FormDirective,
  InputGroupComponent,
  InputGroupTextDirective,
  RowComponent,
  TextColorDirective
} from '@coreui/angular';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthService} from "../../../services/auth.service";
import {cilArrowRight, cilLockLocked, cilUser, cilWarning} from "@coreui/icons";
import {Router, RouterLink} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  standalone: true,
  imports: [ContainerComponent, RowComponent, ColComponent, CardGroupComponent, TextColorDirective, CardComponent, CardBodyComponent, FormDirective, InputGroupComponent, InputGroupTextDirective, IconDirective, FormControlDirective, ButtonDirective, NgStyle, ReactiveFormsModule, RouterLink]
})
export class LoginComponent {

  loginForm: FormGroup;
  errorMessage: string | null = null;

  constructor(private fb: FormBuilder, private authService: AuthService, private iconSet: IconSetService,
              private router: Router) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });

    // Register CoreUI icons
    this.iconSet.icons = {cilUser, cilLockLocked, cilArrowRight, cilWarning};
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.authService.authenticate(this.loginForm.value).subscribe({
        next: (response) => {
          console.log('Authentication successful:', response);
          const userData = this.authService.getUserData();
          console.log('User ID:', this.authService.getUserId());
          console.log('User Role:', this.authService.getUserRole());
          console.log('Username:', this.authService.getUsername());
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

}
