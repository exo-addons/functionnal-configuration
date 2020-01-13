<template>
  <div>
      <form class="form-horizontal" :action="$preferences.actionURL" method="POST">

        <div class="control-group">
            <label class="control-label" for="portlet_name">Nom de la portlet : </label>
            <div class="controls">
                <input type="text" name="portlet_name" v-model="$preferences.portletName"/>
            </div>
        </div>


        <div class="control-group">
            <label class="control-label" for="portlet_name">Sélection du groupe : </label>
            <div class="controls">
                <select name="portlet_group" v-model="$preferences.portletGroup">
                    <option value="0"></option>
                    <option v-for="group of groups" :value="group">Groupe {{group}}</option>
                </select>
            </div>
        </div>


        <div class="control-group submit-form">
            <input type="submit" class="btn" value="Sauvegarder"/>
            <input type="button" class="btn" value="Fermer" onclick="javascript:eXo.webui.UIForm.submitForm('UIPortletForm','Close',true)"/>
        </div>
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
            .then(result => { this.groups = this.provideGroupsConfiguration(result.spaceConfigurations); })
            .catch(error => console.log(error));
    },
    methods: {
        provideGroupsConfiguration: function(spaceConfigurations) {
            console.log(spaceConfigurations);
            return (spaceConfigurations)
                ? [...new Set(spaceConfigurations.map(space => space.highlightConfiguration).filter(space => space.groupIdentifier).map(space => space.groupIdentifier))]
                : [];
        }
    }
}
</script>

<style scoped>

    .submit-form {
        text-align: center;
        margin-top: 40px;
    }

</style>
