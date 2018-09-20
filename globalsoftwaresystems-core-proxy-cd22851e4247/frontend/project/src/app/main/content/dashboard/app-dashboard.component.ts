import {Component} from '@angular/core';

import {FuseTranslationLoaderService} from '@fuse/services/translation-loader.service';

import {locale as english} from './i18n/en';
import {locale as turkish} from './i18n/tr';

@Component({
	selector: 'app-dashboard',
	templateUrl: './app-dashboard.component.html',
	styleUrls: ['./app-dashboard.component.scss']
})
export class DashboardComponent {
	constructor(private fuseTranslationLoader: FuseTranslationLoaderService) {
		this.fuseTranslationLoader.loadTranslations(english, turkish);
	}
}
