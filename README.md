Readme
=======

This addon is a merge of 2 exiting addon : https://github.com/exo-addons/highlight-spaces and https://github.com/exo-addons/activity-composer-configuration
It provides a user interface to :
- select hightlighted spaces, assign them to groups and display these groups anywhere thanks to a dedicated portlet
- configure spaces on which we disable activity composer
- configure if the composer is available for user stream
- disable/enable activities on documents upload/modifications
- disbale/enable terms and conditions approval on login

To install it :
./addon install function-configuration

Interface :

![configuration](https://user-images.githubusercontent.com/807839/64876696-015b5000-d650-11e9-8a5d-59ebd8e0d6b2.png)

Space Menu : 

![spaceNavigation](https://user-images.githubusercontent.com/807839/64876725-1506b680-d650-11e9-91ba-b7c5eb25d17f.png)

Compatibility
=============

* Versions 1.0.x are for 5.2.x 
* Versions 1.1.x and 1.2.x are for 5.3.x+

Changelog
=========

* 1.0.3 : Fix problem about document activities which can be displayed even if deactivated
* 1.1.0 : Fix incompatibility with eXo Platform 5.3.0
* 1.2.x : Add Terms and Conditions feature and allow to display highlighted spaces by group
