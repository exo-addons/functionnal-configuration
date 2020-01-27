<template>
    <div class="highlightedSpacesGroup">
        <span class="portletName">{{$root.portletName}}</span>
        <ul>
            <li class="portletSpace" v-for="space in spaces" :key="space.id">
                <a class="portletSpaceLink" :href="space.spaceUri" >{{space.displayName}}</a>
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


<style scoped>

    .portletName {
        color: #ffffff;
        padding-left:15px;
        font-weight:bold;
    }

    .portletSpace {
        color: #ffffff;
        padding-left:15px;
    }

    .portletSpaceLink {
        color:#ffffff;
        margin-left:15px;
    }
</style>