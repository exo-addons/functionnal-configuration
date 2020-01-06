<template>
    <div>
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
    data() {
        return {
            spaces: []
        }
    },
    created() {
        const groupId = this.$preferences.portletGroup;

        FunctionalConfigurationService.getSpacesForGroup(groupId)
            .then(result => this.spaces = result )
            .catch(error => console.log(error));
    },
}
</script>
