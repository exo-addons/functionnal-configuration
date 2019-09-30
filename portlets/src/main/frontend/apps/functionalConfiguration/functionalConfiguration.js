import FunctionalConfigurationApp from './components/functionalConfiguration.vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { library } from '@fortawesome/fontawesome-svg-core'
import { fas } from '@fortawesome/free-solid-svg-icons'

import axios from 'axios';
 
Vue.config.productionTip = false
library.add(fas);
Vue.component('font-awesome-icon', FontAwesomeIcon)

const lang = eXo.env.portal.language;
const url = `/functional-configuration-portlets/vuesLocales/locale_${lang}.json`;

const fallbackLang = 'en';
const fallbAckLangUrl = `/functional-configuration-portlets/vuesLocales/locale_${fallbackLang}.json`;


function fetchLangFileExist(langUrl) {
	return new Promise((success, error) => {
		axios.get(langUrl)
			.then(() => success())
			.catch(() => error());
	});
	
} 
export function init() {

	fetchLangFileExist(url)
		.then(() => loadVueWithPreferedLand())
		.catch(() => loadVueWithFallbackLand());

	function loadVueWithPreferedLand() {
		renderVueAppWithI18n(lang, url);
	}
		
	function loadVueWithFallbackLand() {
		renderVueAppWithI18n(fallbackLang, fallbAckLangUrl);
	}

	function renderVueAppWithI18n(lang, url) {
		exoi18n.loadLanguageAsync(lang, url).then(i18n => {
			new Vue({
				render: h => h(FunctionalConfigurationApp),
				i18n
			}).$mount('#functionalConfiguration');
		});
	}
}
