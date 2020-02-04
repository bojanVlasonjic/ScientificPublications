import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from './services/authentication.service';
import { UserDTO } from './models/userDTO';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(private router: Router, private authService: AuthenticationService) {
  }

  get getUser(): UserDTO {
    return this.authService.getUser();
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/home']);
  }

  getUrl(): string {
    return this.router.url.split('/')[1];
  }
}
