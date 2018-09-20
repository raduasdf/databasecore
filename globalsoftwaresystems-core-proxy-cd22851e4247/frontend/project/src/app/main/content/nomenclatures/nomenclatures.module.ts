import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

import {TranslateModule} from '@ngx-translate/core';

import {FuseSharedModule} from '@fuse/shared.module';
import {SharedModule} from 'app/main/shared/shared.module';

import {NomenclaturesComponent, NomDialogAddOrEdit} from './nomenclatures.component';

const routes = [
	{
		path: 'ui/nomenclatures',
		component: NomenclaturesComponent
	}
];

@NgModule({
	declarations: [
		NomenclaturesComponent,
		NomDialogAddOrEdit
	],
	imports: [
		RouterModule.forChild(routes),
		TranslateModule,
		FuseSharedModule, 
		SharedModule
	],
	exports: [
		NomenclaturesComponent
	],
	entryComponents: [NomDialogAddOrEdit]
})

export class NomenclaturesModule {
    
}
