import { Component } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { PasswordModule } from 'primeng/password';
import { MessageModule } from 'primeng/message';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ToastModule } from 'primeng/toast';
import { AuthService } from '@services/auth.service';
import { Router } from '@angular/router';
import { LoginRequest } from '@model/request/login-request';
import { MessageService } from 'primeng/api';
import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';

@Component({
  selector: 'spacetrader-login',
  standalone: true,
  imports: [ButtonModule, PasswordModule, MessageModule, ReactiveFormsModule, InputTextModule,
    ToastModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
  providers: [MessageService]
})
export class LoginComponent {

  loginForm: FormGroup
  loading = false

  constructor(private formBuilder: FormBuilder, private auth: AuthService, private router: Router, private messageService: MessageService){
    if (this.auth.isLogged) {
      this.router.navigate(['/home'])
    }
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    })
  }

  login(){
    this.auth.login(this.loginForm.value as LoginRequest).subscribe({
      next: ()=>{
        this.router.navigate(["/home"])
      },error: (err: HttpErrorResponse)=>{
        if(err.status === HttpStatusCode.Forbidden){
          this.messageService.add({
            severity: "error",
            summary: "Usuario o contraseña incorrectos"
          })
        }else{
          this.messageService.add({
            severity: "error",
            summary: "Ha ocurrido un error al iniciar sesión"
          })
        }
      }
    })
  }

}
