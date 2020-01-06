import axios from "axios";

class FunctionalConfigurationService {

    constructor() {
        this.route = '/rest/functional-configuration';
    }

    getConfiguration() {
        const bouchon = {"hideComposerActivities":false,"spaceConfigurations":[
                            {"id":"1","displayName":"espace1","description":null,"groupIdentifier":1},
                            {"id":"2","displayName":"espace2","description":null,"groupIdentifier":2},
                            {"id":"2","displayName":"espace3","description":null,"groupIdentifier":""},
                            {"id":"2","displayName":"espace4","description":null,"groupIdentifier":null},
                            {"id":"2","displayName":"espace5","description":null},
                            {"id":"2","displayName":"espace6","description":null,"groupIdentifier":1},
                            {"id":"2","displayName":"espace7","description":null,"groupIdentifier":6}]};
        return new Promise((resolve, reject) => {
            axios.get(this.route + "/configuration")
//                .then((response) => resolve(response.data))
                .then((response) => resolve(bouchon))
                .catch((error) => reject(error));
        });
    }

    getSpacesForGroup(groupIdentifier){
        return new Promise((resolve, reject) => {
            axios.get(`${this.route}/group/${groupIdentifier}/spaces`)
                .then((response) => resolve(response.data))
//                .then((response) => resolve(bouchon))
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
}

export default new FunctionalConfigurationService();