<template>
  <div id="functionConfiguration-content">
      <!--        configuration.hideDocumentActionActivities SWICH         -->
      <div class="notification">
          <div v-for="notification in notifications" class="alert" role="alert" v-if="notification.visible" :class="notification.cssClass">
              {{notification.title}}
          </div>
      </div>
    <div class="custom-control custom-switch hide-switches">
      <input
        type="checkbox"
        class="custom-control-input"
        id="hideDocumentActionActionActivitiesSwitch"
        v-model="configuration.hideDocumentActionActivities"
        @change="changeHideDocumentActionActivities"
      />
      <label
        class="custom-control-label"
        for="hideDocumentActionActionActivitiesSwitch"
      >{{ $t('functionalConfiguration.hideDocumentActionActivities') }}</label>
    </div>
      <!--        configuration.activityComposerVisible SWICH         -->
    <div class="custom-control custom-switch hide-switches">
      <input
        type="checkbox"
        class="custom-control-input"
        id="userActivityComposer"
        v-model="configuration.hideComposerActivities"
        @change="changeHideComposerActivities"
      />
      <label
        class="custom-control-label"
        for="userActivityComposer"
      >{{ $t('functionalConfiguration.hideComposerActivities') }}</label>
    </div>

    <br/>
    <br/>
 

    <!-- Search in table input and clear btn -->
    <div class="col-6 col-xl-4">
      <div class="input-append">
        <input type="text" :placeholder="$t('functionalConfiguration.table.search')" v-model="spaceFilter" />
        <button class="btn" type="button" @click="clearSearch">{{$t('functionalConfiguration.table.clear')}}</button>
      </div>
    </div>


    <!-- Spaces table -->
    <div class="table-wrapper">
      <table class="uiGrid table table-hover table-striped">
        <thead>
          <tr>
            <th @click="orderDisplayName">{{$t('functionalConfiguration.table.displayName')}}
              <font-awesome-icon v-if="displayNameOrder === 0" :icon="['fas', 'sort']" />
              <font-awesome-icon v-if="displayNameOrder === 1" :icon="['fas', 'sort-down']" />
              <font-awesome-icon v-if="displayNameOrder === -1" :icon="['fas', 'sort-up']" />
            </th>
            <th>{{$t('functionalConfiguration.table.description')}}</th>
            <th>{{$t('functionalConfiguration.table.hideActivityComposer')}}</th>
            <th @click="orderHighlightConfiguration">{{$t('functionalConfiguration.table.highLightSpaceorder')}}
              <font-awesome-icon v-if="highlightConfigurationOrder === 0" :icon="['fas', 'sort']" />
              <font-awesome-icon v-if="highlightConfigurationOrder === 1" :icon="['fas', 'sort-down']" />
              <font-awesome-icon v-if="highlightConfigurationOrder === -1" :icon="['fas', 'sort-up']" />
            </th>
            <th>{{$t('functionalConfiguration.table.actions')}}</th>
          </tr>
        </thead>
        <tbody>
          
          <!-- Empty data -->
          <tr v-if="filteredSpaces.length <= 0"><td class="empty" colspan="5">{{$t('functionalConfiguration.table.empty')}}</td></tr>
          <tr v-for="space in filteredSpaces" :key="space.id">
            <!--VUE-->
            <!--       SHOW displayName in view mode         -->
            <td v-if="!space.edition">{{space.displayName}}</td>
            <!--       SHOW description in view mode         -->
            <td v-if="!space.edition">{{decoder(space.description)}}</td>
            <!--       SHOW showActivity in view mode         -->
            <td v-if="!space.edition">
              <span v-if="space.activityComposerVisible">{{$t('functionalConfiguration.table.acitivityComposer.visible')}}</span>
              <span v-else>{{$t('functionalConfiguration.table.acitivityComposer.notVisible')}}</span>
            </td>
            <!--       SHOW order in view mode         -->
            <td v-if="space.highlightConfiguration && space.highlightConfiguration.highlight && !space.edition">
              {{space.highlightConfiguration.order}}
            </td>
                <!--       SHOW order if empty in view mode         -->
            <td v-else-if="!space.edition"></td>
                <!--       SHOW button EDIT in view mode         -->
            <td v-if="!space.edition && !isEditing">
              <a @click="openEdition(space)" class="actionIcon" data-toggle="tooltip" :title="$t('functionalConfiguration.table.edit')">
                <i class="uiIconEdit uiIconLightGray"></i>
              </a>
            </td>
                <!--       SHOW button EDIT in DISABLED view mode         -->
            <td v-else-if="!space.edition && isEditing">
              <button class="btn btn-outline-dark edition-buttons" disabled>
              <font-awesome-icon :icon="['fas', 'edit']" />
              </button>
            </td>

            <!-- /// EDITION /// -->
            <!--       SHOW displayName in edition mode         -->
            <td v-if="space.edition">
                <p>{{currentSpaceSaved.displayName}}</p>
            </td>
                <!--       SHOW description in edition mode         -->
            <td v-if="space.edition">
                <p>{{decoder(currentSpaceSaved.description)}}</p>
            </td>
                <!--       SHOW buttons in edition         -->
            <td v-if="space.edition">
                <div class="custom-control custom-switch">
                <input
                    class="custom-control-input"
                    id="activityComposerVisibleSwitchEdit"
                    type="checkbox"
                    v-model="currentSpaceSaved.activityComposerVisible"
                />
                <label class="custom-control-label" for="activityComposerVisibleSwitchEdit"></label>
                </div>
            </td>
                <!--       SHOW checkbox and input for order in edition mode        -->
            <td v-if="space.edition">
                <div class="input-group">
                <div class="input-group-prepend">
                    <div class="input-group-text">
                    <input
                        aria-label="Checkbox for following text input"
                        type="checkbox"
                        v-model="currentSpaceSaved.highlightConfiguration.highlight"
                    />
                    </div>
                </div>
                <input
                    aria-label="Text input with checkbox"
                    class="form-control"
                    type="number"
                    v-model="currentSpaceSaved.highlightConfiguration.order"
                />
                </div>
            </td>
            <!--       SHOW buttons in edition mode        -->
            <td v-if="space.edition">

              <!-- Save line edition -->
              <a @click="save(space)" class="actionIcon" data-toggle="tooltip" :title="$t('functionalConfiguration.table.save')">
                <i class="uiIconSave uiIconLightGray"></i>
              </a>

              <!-- Cancel line edition -->
              <a @click="cancelEdit(space)" class="actionIcon" data-toggle="tooltip" :title="$t('functionalConfiguration.table.cancel')">
                <i class="uiIconClose uiIconLightGray"></i>
              </a>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style scoped src="bootstrap/dist/css/bootstrap.min.css"></style>

<script>
import functionalConfigurationService from "../services/functionalConfigurationService";

export default {
  data() {
    return {
      highlightConfigurationOrder: SORT_STATE.NONE,
      displayNameOrder: SORT_STATE.ASC,
      isEditing: false,
      configuration: {},
      currentSpaceSaved: {},
      spaceFilter: "",
      spaces: [],
      notifications: []
    };
  },
  created() {
    const self = this;

    functionalConfigurationService
      .getConfiguration()
      .then(data => (self.configuration = data));
  },
  methods: {
      //  Edit hideDocumentActionActivities
    changeHideDocumentActionActivities() {
      functionalConfigurationService
        .putHideDocumentActionActivities(
          this.configuration.hideDocumentActionActivities
        )
        .then(response => {
            this.successResponse();
        })
        .catch(error => {
            this.failedResponse();
        });
    },
    //  Edit activityComposerVisible
    changeHideComposerActivities() {
      functionalConfigurationService
        .putHideComposerActivities(this.configuration.hideComposerActivities)
        .then(response => {
            this.successResponse();
            }
        )
          .catch(error => {
              this.failedResponse();
          });
    },
      // open the edition for a line
    openEdition(space) {
      this.isEditing = true;
      this.currentSpaceSaved = this.deepCloneObject(space);
      this.$set(space, "edition", true);
    },
      // stop the edition
    cancelEdit(space) {
      this.isEditing = false;
      this.$set(space, "edition", false);
    },
      // save a space
    save(space) {
        const self = this;

        functionalConfigurationService.putSpaceConfiguration(self.currentSpaceSaved)
          .then(data => {

            space.activityComposerVisible = data.activityComposerVisible;
            space.highlightConfiguration = data.highlightConfiguration;
        
            self.cancelEdit(space);
            delete self.currentSpaceSaved;

              this.successResponse();
          })
            .catch(error => {
                this.failedResponse();
            });
    },
      // copy a space content for save
    deepCloneObject(obj) {
      return JSON.parse(JSON.stringify(obj));
    },
      // clear the search input
    clearSearch() {
      this.spaceFilter = "";
    },
      successResponse(){
          this.toastSuccess(this.$t('functionalConfiguration.toast.success.message'));
      },
      failedResponse(){
          this.toastDanger(this.$t('functionalConfiguration.toast.fail.message'));
      },
      toastSuccess(message) {
          this.makeToast(message, TOAST_TYPE.SUCCESS);
      },
      toastDanger(message) {
          this.makeToast(message, TOAST_TYPE.DANGER);
      },
      makeToast(title, cssClass) {
          const DEFAULT_TOAST_DELAY = 5000;

          const notification = { id: this.notifications.length, title: title, visible: true, cssClass: cssClass };
          this.notifications.push(notification);

          setTimeout(() => { notification.visible = false; }, DEFAULT_TOAST_DELAY);
      },
      orderDisplayName(){
          this.highlightConfigurationOrder = SORT_STATE.NONE;
          if(this.displayNameOrder === SORT_STATE.ASC){
              this.displayNameOrder = SORT_STATE.DESC
          } else {
      // change order in diplayName
              this.displayNameOrder = SORT_STATE.ASC
          }
      },
      // change order in highlightConfiguration
      orderHighlightConfiguration(){
          this.displayNameOrder = SORT_STATE.NONE;
          if(this.highlightConfigurationOrder === SORT_STATE.ASC){
              this.highlightConfigurationOrder = SORT_STATE.DESC
          } else {
              this.highlightConfigurationOrder = SORT_STATE.ASC
          }
      },
      decoder (str) {
        var text = document.createElement('textarea');
        text.innerHTML = str;
        return text.value;
      }

  },
  computed: {
      // filter spaces
    filteredSpaces: function() {
      if (!this.configuration || !this.configuration.spaceConfigurations) {
        return [];
      }

      const diplayNameAndDescriptionFilter = space => {
        const displayName = space.displayName ? space.displayName : "";
        const description = space.description ? space.description : "";

        return (
          displayName.toLowerCase().includes(this.spaceFilter.toLowerCase()) ||
          description.toLowerCase().includes(this.spaceFilter.toLowerCase())
          );
      }

        var spaces = this.configuration.spaceConfigurations.filter(diplayNameAndDescriptionFilter);
        if (this.displayNameOrder === SORT_STATE.ASC){
            spaces = spaces.sort((a, b) => a.displayName.localeCompare(b.displayName));
        } else if(this.displayNameOrder === SORT_STATE.DESC){
            spaces = spaces.sort((b, a) => a.displayName.localeCompare(b.displayName));
        }

        if (this.highlightConfigurationOrder === SORT_STATE.ASC){
            spaces = spaces.sort(function(a, b) {
                if (!a.highlightConfiguration.highlight && !b.highlightConfiguration.highlight) {
                    //spaces are not highlighted, sort on name
                    return a.displayName.localeCompare(b.displayName);
                } else if (a.highlightConfiguration.highlight && !b.highlightConfiguration.highlight) {
                    //space a is hightlighted, space b is not, so a should be after b
                    return 1;
                } else if (!a.highlightConfiguration.highlight && b.highlightConfiguration.highlight) {
                    //space a is not hightlighted, space b is, so b should be after a
                    return -1;
                } else if (a.highlightConfiguration.order==b.highlightConfiguration.order) {
                    //space are hightlighted with same rank, sort on name;
                    return a.displayName.localeCompare(b.displayName);
                } else {
                    //space are hightlighted, with different rank
                    return a.highlightConfiguration.order>b.highlightConfiguration.order;
                }
            });
        } else if(this.highlightConfigurationOrder === SORT_STATE.DESC){
            spaces = spaces.sort(function(a, b) {
                if (!b.highlightConfiguration.highlight && !a.highlightConfiguration.highlight) {
                    //spaces are not highlighted, sort on name
                    return b.displayName.localeCompare(a.displayName);
                } else if (b.highlightConfiguration.highlight && !a.highlightConfiguration.highlight) {
                    //space b is hightlighted, space a is not, so b should be after a
                    return 1;
                } else if (!b.highlightConfiguration.highlight && a.highlightConfiguration.highlight) {
                    //space b is not hightlighted, space a is, so a should be after b
                    return -1;
                } else if (b.highlightConfiguration.order==a.highlightConfiguration.order) {
                    //space are hightlighted with same rank, sort on name;
                    return b.displayName.localeCompare(a.displayName);
                } else {
                    //space are hightlighted, with different rank
                    return b.highlightConfiguration.order>a.highlightConfiguration.order;
                }
            });
        }

        return spaces;
    }
  }

};
const TOAST_TYPE = {
    SUCCESS: 'alert-success',
    DANGER: ' alert-danger',
}

const SORT_STATE = {
    DESC: -1,
    NONE: 0,
    ASC: 1
}
</script>

<style scoped>

.form-control {
  height: 40px;
}
/* .table-search{
    margin-left: 5px;
} */
.hide-switches {
  margin: 20px;
}
.spaces-table-container {
  margin-left: 0 !important;
}
.table th,
.table td {
  vertical-align: inherit !important;
  text-align: center !important;
}

.input-append {
  width: 100%;
}

.table-wrapper {
    padding: 0 16px;
}
.notification {
    position: absolute;
    min-width: 300px;
    max-width: 300px;
    right: 10px;
    top: 67px;
}
</style>
