import { Component, OnInit, ViewChild } from '@angular/core';
import { UserDTO } from 'src/app/models/user-dto';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { ToasterService } from 'src/app/services/toaster.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  registerData: UserDTO;
  confirmedPassword: string;
  passwordsMatch: boolean;

  @ViewChild("rf", {static: false}) registerForm: any;

  constructor(
    private authService: AuthenticationService,
    private toastService: ToasterService,
    private router: Router
    ) { 
    this.registerData = new UserDTO();
    this.confirmedPassword = "";
    this.passwordsMatch = true;
  }

  ngOnInit() {
  }

  checkPasswordMatch() {
    this.passwordsMatch = this.confirmedPassword === this.registerData.password;
  }

  register() {
    if(!this.registerForm.valid || this.confirmedPassword != this.registerData.password) {
      return;
    }

    this.authService.register(this.registerData).subscribe(
      data => {
        this.toastService.showMessage('Success', 'Registration successful');
        this.router.navigate(['/login']);
      },
      err => {
        this.toastService.showErrorMessage(err);
      }
    );

  }

}
