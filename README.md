RtcNbClient
===========

Rational Team Concert Client for NetBeans IDE

Application building

1. Build RTC API libraries
Before we start building application we need to prepare RTC API libraries. Libs are prepared with python script. You can download Python 2.7 from http://python.org/ 
Navigate to RtcNbClientLibsWrapper and execute command
python script/deploy_libs.py
This will take a while as libraries need to be installed to local maven repository.

2. Build application
In order to build our project we need Apache Maven. Download it from http://maven.apache.org/download.html
Maven installation instructions can be found on Maven homepage or in README file in Maven package.
With Maven installed, we can run Maven command from root folder. 
First build needs to be run with libs wrapper profile
mvn install -P withLibsWrapper
next builds are done with command
mvn install	
Maven will start building application. At first run it will download all required packages, so it may take a while. 
Created package will be stored in Maven repository and in RtcNbClientApplication/target/ module directory
