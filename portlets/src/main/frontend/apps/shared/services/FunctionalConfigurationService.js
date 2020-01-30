import axios from "axios";

class FunctionalConfigurationService {

    constructor() {
        this.route = '/rest/functional-configuration';
    }

    getConfiguration(){
        return new Promise((resolve, reject) => {
            axios.get(this.route + "/configuration")
                .then((response) => resolve(response.data))
                .catch((error) => reject(error));
        });
    }

    getSpacesForGroup(groupIdentifier){
        return new Promise((resolve, reject) => {
            axios.get(`${this.route}/group/${groupIdentifier}/spaces`)
                .then((response) => resolve(response.data))
                .catch((error) => reject(error));
        });
    }

    putHideComposerActivities(isHidden) {

        const restRoute = this.route + "/composer-activity?hidden=" + isHidden;

        return new Promise((resolve, reject) => {
            axios.put(restRoute)
                .then((response) => resolve(response.data))
                .catch((error) => reject(error));
        });
    }

    putSpaceConfiguration(configuration) {

        const restRoute = this.route + "/configuration/space";

        return new Promise((resolve, reject) => {
            axios.put(restRoute, configuration)
                .then((response) => resolve(response.data))
                .catch((error) => reject(error));
        });
    }

    putHideDocumentActionActivities(isHidden) {
        const restRoute = this.route + "/document-activity?hidden=" + isHidden;

        return new Promise((resolve, reject) => {
            axios.put(restRoute)
                .then((response) => resolve(response.data))
                .catch((error) => reject(error));
        });
    }

    putTermsAndConditions(termsAndConditions) {
        const restRoute = this.route + "/terms-and-conditions";

        return new Promise((resolve, reject) => {
            axios.put(restRoute, termsAndConditions)
                .then((response) => resolve(response.data))
                .catch((error) => reject(error));
        });
    }
}

export default new FunctionalConfigurationService();