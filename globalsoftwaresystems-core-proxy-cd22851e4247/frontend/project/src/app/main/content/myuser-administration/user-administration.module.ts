import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {TranslateModule} from '@ngx-translate/core';

import {FuseSharedModule} from '@fuse/shared.module';
import {SharedModule} from 'app/main/shared/shared.module';

import {MyUserAdministrationComponent, MyUserAdminDialog} from './user-administration.component';
import {UserInfoService} from '../../../services/userinfo.service';

const routes = [
	{
		path: 'ui/myuser-administration',
		component: MyUserAdministrationComponent
	}
];

@NgModule({
	declarations: [
		MyUserAdministrationComponent,
		MyUserAdminDialog
	], 
	imports: [
		RouterModule.forChild(routes),
		TranslateModule,
		FuseSharedModule,
		SharedModule
	],
	exports: [
		MyUserAdministrationComponent
	],
	entryComponents: [MyUserAdminDialog],
	providers: [UserInfoService]
})

export class MyUserAdministrationModule {
}
