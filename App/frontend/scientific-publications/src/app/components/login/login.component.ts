import { Component, OnInit, ViewChild } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { LoginData } from 'src/app/models/login-data';
import { Router } from '@angular/router';
import { ToasterService } from 'src/app/services/toaster.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginData: LoginData;
  @ViewChild('lf', {static: false}) loginForm: any;

  constructor(
    private authService: AuthenticationService,
    private toastService: ToasterService,
    private router: Router
    ) {
    this.loginData = new LoginData();  
  }

  ngOnInit() {
  }

  login() {

    if(!this.loginForm.valid) {
      return;
    }

    this.authService.login(this.loginData).subscribe(
      data => {
        this.authService.rememberUser(data);
        this.router.navigate(['/home']);
      },
      err => {
        this.toastService.showErrorMessage(err);
      }
    )
  }

}
