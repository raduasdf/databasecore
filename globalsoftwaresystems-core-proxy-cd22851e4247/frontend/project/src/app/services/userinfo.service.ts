import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class UserInfoService {
	principalUrl = environment.user_info_url;

	constructor(private http: HttpClient) {
	}

	public getPrincipal(): Observable<any> {
		return this.http.get(`${this.principalUrl}`);
	}

}
