import {Component, Inject, AfterViewInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {FormControl} from '@angular/forms';
import {MatPaginator, MatSort, MatTableDataSource, MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import {Observable} from 'rxjs/Observable';
import {UserInfoService} from '../../../services/userinfo.service';
import 'rxjs/add/observable/merge';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/startWith';
import 'rxjs/add/operator/switchMap';

import {FuseTranslationLoaderService} from '@fuse/services/translation-loader.service';

import {locale as english} from './i18n/en';
import {locale as turkish} from './i18n/tr';

@Component({
	selector: 'user-administration',
	templateUrl: './user-administration.component.html',
	styleUrls: ['./user-administration.component.css']
})
export class UserAdministrationComponent implements AfterViewInit {
	displayedColumns: string[];
	userDatabase: GenericHttpDao | null;
	dataSource = new MatTableDataSource();

	resultsLength = 0;
	isLoadingResults = false;
	isRateLimitReached = false;

	length = 100;
	pageSize = 30;
	pageSizeOptions = [1, 5, 10, 30, 100];

	@ViewChild(MatPaginator) paginator: MatPaginator;
	@ViewChild(MatSort) sort: MatSort;

	constructor(private http: HttpClient, public dialog: MatDialog, private userInfoService: UserInfoService) {
	}

	ngAfterViewInit() {
		this.userDatabase = new GenericHttpDao(this.http)
		this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
		Observable.merge(this.sort.sortChange, this.paginator.page)
			.startWith(null)
			.switchMap(() => {
				return this.userDatabase!.getData(this.paginator.pageIndex, this.paginator.pageSize, this.sort.active, this.sort.direction);
			})
			.map(data => {
				this.isLoadingResults = false;
				this.isRateLimitReached = false;
				this.resultsLength = data.totalElements;
				return data.content;
			})
			.catch(() => {
				setTimeout(() => {
					this.isLoadingResults = false;
					this.isRateLimitReached = true;
				});
				return Observable.of([]);
			})
			.subscribe(data => {
				this.displayedColumns = ['number', 'login', 'startDate', 'endDate', 'roles', 'actions'];
				this.dataSource.data = data;
			});
	}

	public changeActive(event: any, user: any) {
		this.http.put(`/database-api/users/${!!user.endDate ? 'activate' : 'deactivate'}/${user.id}`, {})
			.subscribe(data => {
				user.endDate = (!!user.endDate ? null : new Date());
			});
	}

	public getRowIndex(index: number) {
		return (this.paginator.pageIndex * this.paginator.pageSize + index + 1);
	}

	public getChanges(changes: any) {
		this.isLoadingResults = true;
	}

	openDialog(type: string, model): void {
		let dialogRef = this.dialog.open(UserAdminDialog, {
			data: {type: type, userData: Object.assign({}, model)}
		});

		dialogRef.afterClosed().subscribe(result => {
			this.ngAfterViewInit();
		});
	}

	
}


export interface GenericResponseBody {
	totalPages: number;
	totalElements: number;
	number: number;
	size: number;
	numberOfElements: number;
	first: boolean;
	last: boolean;
	sort: any;
	content: any[];
}

export class GenericHttpDao {
	constructor(private http: HttpClient) {
	}

	getData(page: number, pageSize: number, sortKey: string, sortDirection: string): Observable<GenericResponseBody> {
		const requestForm = {
			'nrElements': pageSize,
			'page': page
		};
		requestForm['sorters'] = !!sortKey ? [
			{
				'key': sortKey,
				'direction': sortDirection.toUpperCase()
			}
		] : [];
		requestForm['filters'] = [];
		return this.http.post<GenericResponseBody>('/database-api/users/list', requestForm);
	}
}


@Component({
	selector: 'user-admin-dialog',
	templateUrl: 'user-admin-dialog.html'
})
export class UserAdminDialog {
	roles: string[];

	constructor(private http: HttpClient,
		public dialogRef: MatDialogRef<UserAdminDialog>,
		@Inject(MAT_DIALOG_DATA) public data: any
	) {
		http.get<string[]>('/database-api/users/roles').subscribe(data => {
			this.roles = data;
		});
	}

	onNoClick(): void {
		this.dialogRef.close();
	}

	save() {
		this.http.post('/database-api/users/save', this.data.userData)
			.subscribe(() => {this.dialogRef.close(this.data.userData);});
	}
}





