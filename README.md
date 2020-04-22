This addon is a merge of 2 existing addons : https://github.com/exo-addons/highlight-spaces and https://github.com/exo-addons/activity-composer-configuration.
It provides a user interface to :
- select hightlighted spaces, assign them to groups and display these groups anywhere thanks to a dedicated portlet
- configure spaces on which we disable activity composer
- configure if the composer is available for user stream
- disable/enable activities on documents upload/modifications
- disbale/enable terms and conditions approval on login

Installation
============

The following command must be executed from the root folder of the eXoPlatfominstannce to install it:

    ./addon install exo-function-configuration

User Guide
==========

Hightlighted spaces
-------------------

The addon allows to highlight spaces in the menu.
For each space, when clicking on the Edit button, the following options can be set:
* is the space highlighted or not, by checking the checkbox
* the highlighted group the space belongs to - this is optional, a higlighted to belong to no group (10 groups are available, and cannot be changed)
* the rank of the highlighted space

// screenshot admin screen

When a space is set as highlighted but not assigned to a group, it is removed from the "My Spaces" list and displayed above it in the left navigation:

// screenshot of left navigation

When a space is set as highlighted and assigned to a group, it is removed from the "My Spaces" list and a portlet must be added in the layout to display it by following these steps:
* go to *Administration > Applications*
* go to *Portlets*
* select the portlet "Highlighted spaces portlet"

// screenshot Applications registry

* assign it in a category by clicking the action link "Click here to add into categories"
* go to the site/page where you want to display the highlighted spaces and edit its layout
* drag and drop the portlet at the desired location
* edit the portlet
* in the Edit mode tab, set the *Portlet name* and select the group you want to display

// screenshot Edit mode

* save

The highlighted spaces of the selected group are now displayed.

// screenshot Highlighted spaces of group

The porltet name set in the portlet edition is used as the group title.


Spaces Activity composer deactivation
-------------------------------------

The addon allows to hide the activity composer in the spaces activity stream.
This configuration is done per space in the admin screen:

// screenshot admin screen

User Activity composer deactivation
-----------------------------------

The addon allows to hide the activity composer in the user activity stream:

// screenshot admin screen

Document activities deactivation
--------------------------------

Each time a document is created or uploaded in the platform, an activity is posted.
This can flood the stream when a lot of documents are uploaded.
This addon allows to disable these activities by activating the option in the admin screen:

// screenshot admin screen

When activiating, only new activities won't be posted, old ones are not deleted.
Also, activities not generated during the deactivation will not be created back if the documents activities posting is enabled back.

Terms and conditions
--------------------

A terms and conditions approval screen can be enabled when users login:

// screenshot admin screen

The content of the terms and conditions must be stored in a web content, located in the `collbaoration` workspace.
The full path of this web content must be filled in the input field.

Once activated, and if at least a first version of the web content has been published, when a user logins for the first time, the terms and conditions screen is displayed and must be approved.

![terms-and-conditions](https://user-images.githubusercontent.com/342645/79979320-a0c3a980-84a1-11ea-8b09-e75b2537686c.png)

Each time a new version of the web content is published, all the users must approved again the terms and coonditions.

Compatibility
=============

* Versions 1.0.x are for 5.2.x 
* Versions 1.1.x and 1.2.x are for 5.3.x+

Changelog
=========

* 1.0.3 : Fix problem about document activities which can be displayed even if deactivated
* 1.1.0 : Fix incompatibility with eXo Platform 5.3.0
* 1.2.x : Add Terms and Conditions feature and allow to display highlighted spaces by group
