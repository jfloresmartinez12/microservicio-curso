import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import 'rxjs/add/operator/finally';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  title = 'Demo';
  authenticated = false;
  greeting = {};

  constructor(private http: HttpClient) {
    this.authenticate();
  }

  //1
  authenticate() {
    //1.1
    this.http.get('user').subscribe(response => {
        if (response['name']) {
            this.authenticated = true;
            //1.2
            this.http.get('resource').subscribe(data => this.greeting = data);
        } else {
            this.authenticated = false;
        }
    }, () => { this.authenticated = false; });

  }
  logout() {
      this.http.post('logout', {}).finally(() => {
          this.authenticated = false;
      }).subscribe();
  }

}
