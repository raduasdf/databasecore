import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {TranslateModule} from '@ngx-translate/core';

import {FuseSharedModule} from '@fuse/shared.module';
import {SharedModule} from 'app/main/shared/shared.module';

import {UserAdministrationComponent, UserAdminDialog} from './user-administration.component';
import {UserInfoService} from '../../../services/userinfo.service';

const routes = [
	{
		path: 'ui/user-administration',
		component: UserAdministrationComponent
	}
];

@NgModule({
	declarations: [
		UserAdministrationComponent,
		UserAdminDialog
	],
	imports: [
		RouterModule.forChild(routes),
		TranslateModule,
		FuseSharedModule,
		SharedModule
	],
	exports: [
		UserAdministrationComponent
	],
	entryComponents: [UserAdminDialog],
	providers: [UserInfoService]
})

export class UserAdministrationModule {
}
