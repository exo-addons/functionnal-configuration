<template>
    <div>
        <span style="color: #ffffff;padding-left:15px;font-weight:bold;">{{$root.portletName}}</span>
        <ul>
            <li style="color: #ffffff;padding-left:15px;" v-for="space in spaces">
                <a :href="space.spaceUri" style="color:#ffffff;margin-left:15px;">{{space.displayName}}</span></a>
            </li>
        </ul>

    </div>
</template>

<script>
import FunctionalConfigurationService from "../../../shared/services/FunctionalConfigurationService";

export default {
    data: function () {
        return {
            spaces: []
        }
    },
    created() {

        this.groupId = this.$root.portletGroup;

        FunctionalConfigurationService.getSpacesForGroup(this.groupId)
            .then(result => this.spaces = result.sort((a, b) => a.highlightConfiguration.order - b.highlightConfiguration.order))
            .catch(error => console.log(error));
    },
}
</script>
