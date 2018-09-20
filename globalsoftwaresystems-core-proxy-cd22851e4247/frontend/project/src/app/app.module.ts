import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {RouterModule, Routes} from '@angular/router';
import {TranslateModule} from '@ngx-translate/core';
import 'hammerjs';

import {FuseModule} from '@fuse/fuse.module';
import {FuseSharedModule} from '@fuse/shared.module';

import {fuseConfig} from './fuse-config';

import {AppComponent} from './app.component';
import {FuseMainModule} from './main/main.module';
import {FuseSampleModule} from './main/content/sample/sample.module';

import {DashboardModule} from './main/content/dashboard/app-dashboard.module';
import {NomenclaturesModule} from './main/content/nomenclatures/nomenclatures.module';
import {UserAdministrationModule} from './main/content/user-administration/user-administration.module';
import {MyUserAdministrationModule} from './main/content/myuser-administration/user-administration.module';


const appRoutes: Routes = [
	{
		path: '**',
		redirectTo: 'ui/dashboard'
	}
];

@NgModule({
	declarations: [
		AppComponent
	],
	imports: [
		BrowserModule,
		BrowserAnimationsModule,
		HttpClientModule,
		RouterModule.forRoot(appRoutes),
		TranslateModule.forRoot(),

		// Fuse Main and Shared modules
		FuseModule.forRoot(fuseConfig),
		FuseSharedModule,
		FuseMainModule,
		FuseSampleModule,
		DashboardModule,
		NomenclaturesModule,
		UserAdministrationModule,
		MyUserAdministrationModule
	],
	bootstrap: [
		AppComponent
	]
})
export class AppModule {
}
