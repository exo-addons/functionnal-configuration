import FunctionalConfigurationApp from './components/functionalConfiguration.vue'

const lang = eXo.env.portal.language;
const url = `/functional-configuration-portlets/vuesLocales/locale_${lang}.json`;

export function init() {
	exoi18n.loadLanguageAsync(lang, url).then(i18n => {
		new Vue({
			render: h => h(FunctionalConfigurationApp),
			i18n
		}).$mount('#functionalConfiguration');
	});
}