<template>
    <div class="highlightedSpacesGroup">
        <span class="portletName">{{$root.portletName}}</span>
        <ul>
            <li class="portletSpace" v-for="space in spaces" :key="space.id" :class="isSpaceSelected(space) ? 'spaceItemSelected' : ''">
                <a class="portletSpaceLink" :href="space.uri">{{space.displayName}}</a>
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
            .then(highlightedSpaces => this.spaces = highlightedSpaces.sort((a, b) => a.order - b.order))
            .catch(error => console.log(error));
    },
    methods: {
      isSpaceSelected(space) {
        return space.uri === window.location.pathname || window.location.pathname.startsWith(space.uri + '/');
      }
    }
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