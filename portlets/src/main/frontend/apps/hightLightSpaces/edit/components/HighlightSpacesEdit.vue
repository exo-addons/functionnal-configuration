<template>
  <div>
      <form :action="$preferences.actionURL" method="POST">
        <label>Titre du bloc</label>
        <input type="text" name="portlet_name" value="toto" v-model="$preferences.portletName"/>

        <select name="portlet_group" v-model="$preferences.portletGroup">
            <option value="0"></option>
            <option v-for="group of groups" :value="group">Groupe {{group}}</option>
        </select>


        <input type="submit" value="Sauvegarder"/>
      </form>
  </div>
</template>

<script>
import FunctionalConfigurationService from "../../../shared/services/FunctionalConfigurationService";

export default {
    data() {
        return {
            groups: []
        }
    },
    created() {
        FunctionalConfigurationService.getConfiguration()
            .then(result => { this.groups = this.provideGroupsConfiguration(result.spaceConfigurations); console.log(this.groups); })
            .catch(error => console.log(error));
    },
    methods: {
        provideGroupsConfiguration: function(spaceConfigurations) {
            return (spaceConfigurations)
                ? [...new Set(spaceConfigurations.filter(space => space.groupIdentifier).map(space => space.groupIdentifier))]
                : [];
        }
    }
}
</script>
