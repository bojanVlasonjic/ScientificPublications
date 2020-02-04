import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginData } from '../models/login-data';
import { UserDTO } from '../models/userDTO';
import { JsonPipe } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private http: HttpClient) { }

  /* Authentication controller requests */

  login(loginData: LoginData): Observable<any> {
    return this.http.post('api/auth/login', loginData);
  }

  register(userDTO: UserDTO): Observable<any> {
    return this.http.post('api/auth/register', userDTO);
  }


  /* Login utilities */

  rememberUser(user: UserDTO) {
    window.localStorage.setItem('user', JSON.stringify(user));
  }

  isLoggedIn(): boolean {
    
    return window.localStorage.getItem('user') != null;
  }

  logout(): void {
    window.localStorage.removeItem('user');
  }

  getUser(): UserDTO {
    return this.isLoggedIn() ? JSON.parse(window.localStorage.getItem('user')): null;
  }
 

}
