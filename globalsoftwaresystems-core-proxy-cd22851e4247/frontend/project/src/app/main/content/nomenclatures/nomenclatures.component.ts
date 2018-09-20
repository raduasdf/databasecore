import {Component, Inject, AfterViewInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {FormControl} from '@angular/forms';
import {MatPaginator, MatSort, MatTableDataSource, MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/observable/merge';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/startWith';
import 'rxjs/add/operator/switchMap';

import {FuseTranslationLoaderService} from '@fuse/services/translation-loader.service';

@Component({
	selector: 'nomenclatures',
	templateUrl: './nomenclatures.component.html',
	styleUrls: ['./nomenclatures.component.css']
})
export class NomenclaturesComponent implements AfterViewInit {
	displayedColumns: string[];
	caseDatabase: GenericHttpDao | null;
	dataSource = new MatTableDataSource();

	resultsLength = 0;
	isLoadingResults = false;
	isRateLimitReached = false;

	length = 100;
	pageSize = 30;
	pageSizeOptions = [1, 5, 10, 30, 100];

	allNomTypes: any[];
	nomTypeCode: string;
	nomTypeValue: string;

	@ViewChild(MatPaginator) paginator: MatPaginator;
	@ViewChild(MatSort) sort: MatSort;


	constructor(private http: HttpClient, public dialog: MatDialog, public router: Router) {
		http.get<any[]>('/database-api/nom/nomTypes').subscribe(data => {
			if (!!data && data.length > 0) {
				this.allNomTypes = data;
				this.nomTypeCode = data[0].code;
				this.nomTypeValue = data[0].value;
				this.ngAfterViewInit();
			}
		});
	}

	ngAfterViewInit() {
		this.caseDatabase = new GenericHttpDao(this.http, this.nomTypeCode)
		this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
		Observable.merge(this.sort.sortChange, this.paginator.page)
			.startWith(null)
			.switchMap(() => {
				return this.caseDatabase!.getData(this.paginator.pageIndex, this.paginator.pageSize, this.sort.active, this.sort.direction);
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
				this.displayedColumns = ['number', 'code', 'value', 'actions'];
				this.dataSource.data = data;
			});
	}

	public delete(id: number) {
		this.http.delete(`/database-api/nom/management/removeNomenclature/${id}`, {})
			.subscribe(() => {
				this.ngAfterViewInit();
			});
	}

	public getRowIndex(index: number) {
		return (this.paginator.pageIndex * this.paginator.pageSize + index + 1);
	}

	public getChanges(event: any, typeValue: string) {
		this.isLoadingResults = true;
		this.nomTypeValue = typeValue;
		this.ngAfterViewInit();
	}

	openDialog(type: string, model): void {
		model.typeCode = this.nomTypeCode;
		model.typeValue = this.nomTypeValue;
		console.log(model);
		let dialogRef = this.dialog.open(NomDialogAddOrEdit, {
			data: {type: type, nomData: Object.assign({}, model)}
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
	constructor(private http: HttpClient, private nomTypeCode?: String) {
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
		return this.http.post<GenericResponseBody>(`/database-api/nom/list?typeCode=${this.nomTypeCode}`, requestForm);
	}
}

@Component({
	selector: 'nom-dialog-add-or-edit',
	templateUrl: 'nom-dialog-add-or-edit.html'
})
export class NomDialogAddOrEdit {
	investigationReasons: any[];
	investigationTypes: any[];
	osVersions: any[];
	deviceModels: any[];

	constructor(private http: HttpClient,
		public dialogRef: MatDialogRef<NomDialogAddOrEdit>,
		@Inject(MAT_DIALOG_DATA) public data: any) {
		console.log("Data.nomdata: ", this.data.nomData);
	}

	onNoClick(): void {
		this.dialogRef.close();
	}

	save() {
		console.log(this.data.nomData);
		this.http.post('/database-api/nom/management/addNomenclature', this.data.nomData)
			.subscribe(() => {
				this.dialogRef.close(this.data.nomData);
			});
	}
}