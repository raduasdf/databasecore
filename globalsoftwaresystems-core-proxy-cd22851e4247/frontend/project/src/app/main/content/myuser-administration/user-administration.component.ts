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
import { filterQueryId } from '@angular/core/src/view/util';
import { $ } from 'protractor';

@Component({
	selector: 'myuser-administration',
	templateUrl: './user-administration.component.html',
	styleUrls: ['./user-administration.component.css']
})
export class MyUserAdministrationComponent implements AfterViewInit {
	displayedColumns: string[];
	userDatabase: GenericHttpDao | null;
	dataSource = new MatTableDataSource();
	highlighted = {};

	resultsLength = 0;
	isLoadingResults = false;
	isRateLimitReached = false;
	isAdd = false;
	isEditing = {};

	length = 100;
	pageSize = 30;
	pageSizeOptions = [1, 5, 10, 30, 100];

	filteredColumns = ['userName', 'password', 'email', 'address', 'nomItem.value'];
	filterValues = ['', '', '', '', ''];
	nrOfColumns = 5;

	genders: string[];

	@ViewChild(MatPaginator) paginator: MatPaginator;
	@ViewChild(MatSort) sort: MatSort;

	constructor(private http: HttpClient, public dialog: MatDialog, private userInfoService: UserInfoService) {
		http.get<string[]>('/database-api/nom/nomListByCode/GENDER').subscribe(data => {
			this.genders = data;
			console.log(data)
		});
	}

	ngAfterViewInit() {
		this.userDatabase = new GenericHttpDao(this.http)
		this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
		Observable.merge(this.sort.sortChange, this.paginator.page)
			.startWith(null)
			.switchMap(() => {
				return this.userDatabase!.getData(this.paginator.pageIndex, this.paginator.pageSize,
					 this.sort.active, this.sort.direction,
					 this.filteredColumns, this.filterValues);
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
				this.displayedColumns = ['number', 'id', 'userName', 'password', 'email', 'address', 'gender', 'actions'];
				this.dataSource.data = data;
			});


		let nrOfColumns = this.nrOfColumns;
		let filterValues = this.filterValues;
		let filteredColumns = this.filteredColumns;

		this.dataSource.filterPredicate = function(data, filter: string): boolean {
			let filterResult = true;

			for (let i = 0; i < nrOfColumns; i++ ) {
				filterResult = filterResult 
							&& data[filteredColumns[i]].toLowerCase().includes(filterValues[i]);
			}
			console.log(filterResult)
			return filterResult;
		};

	}

	redoInit() {
		this.userDatabase = new GenericHttpDao(this.http)
		this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
		Observable.merge(this.sort.sortChange, this.paginator.page)
			.startWith(null)
			.switchMap(() => {
				return this.userDatabase!.getData(this.paginator.pageIndex, this.paginator.pageSize,
					 this.sort.active, this.sort.direction,
					 this.filteredColumns, this.filterValues);
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
				this.displayedColumns = ['number', 'id', 'userName', 'password', 'email', 'address', 'gender', 'actions'];
				this.dataSource.data = data;
			});

	}

	public changeActive(event: any, user: any) {
		this.http.put(`/database-api/myusers/${!!user.endDate ? 'activate' : 'deactivate'}/${user.id}`, {})
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
		if(type.localeCompare("edit") == 0) {
			this.isAdd = false;
			console.log("edit");
		} else {
			this.isAdd = true;
			console.log("add");

		}
		let dialogRef = this.dialog.open(MyUserAdminDialog, {
			data: {type: type, userData: Object.assign({}, model)}
		});

		dialogRef.afterClosed().subscribe(result => {
			this.ngAfterViewInit();
		});
	}

	applyFilter(filterValue: string, columnIndex: number) {
		this.filterValues[columnIndex] = filterValue;
		this.redoInit();
	}

	x = 0;
	testFunc(event, row){
		console.log(this.x++ + "\n");
		console.log(event.target);
		console.log("\n");
		console.log(row);
		console.log("\n");

	}


	saveEdit(row) {

		// this.http.put('/database-api/myusers/users', this.data.userData)
		// 	.subscribe(() => {this.dialogRef.close(this.data.userData);});
		this.http.put('/database-api/myusers/users', row)
			.subscribe(data => {
				
			});
		console.log(row);
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

	getData(page: number, pageSize: number, sortKey: string, sortDirection: string,
		 filterKeys: string[], filterValues: string[]): Observable<GenericResponseBody> {
		let requestForm = {
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
		for(let i = 0; i < 5; i++) {
			requestForm['filters'].push( {
				'key': filterKeys[i],
				'type': 'string',
	 			'value': filterValues[i],
	 			'op': ':',
	 			'builder': null
			});
		};

		return this.http.post<GenericResponseBody>('/database-api/myusers/list', requestForm);
	}
}


@Component({
	selector: 'user-admin-dialog',
	templateUrl: 'user-admin-dialog.html'
})
export class MyUserAdminDialog {
	genders: string[];

	constructor(private http: HttpClient,
		public dialogRef: MatDialogRef<MyUserAdminDialog>,
		@Inject(MAT_DIALOG_DATA) public data: any
	) {
		http.get<string[]>('/database-api/nom/nomListByCode/GENDER').subscribe(data => {
			this.genders = data;
		});
		
	}

	onNoClick(): void {
		this.dialogRef.close();
	}

	save() {
		this.http.post('/database-api/myusers/users', this.data.userData)
			.subscribe(() => {this.dialogRef.close(this.data.userData);});
	}

	saveEdit() {

		this.http.put('/database-api/myusers/users', this.data.userData)
			.subscribe(() => {this.dialogRef.close(this.data.userData);});
			console.log(this.data.userData);

	}
}





