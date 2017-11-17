SETUP:
The libraries from lib/additional have to be imported first. The LQN Solver must be installed and added to the systems PATH.

RUN:
The tool can be run by calling HeadlessPerOpteryxRunner. A (valid) Configuration has to be provided.

BUILD:
The Maven project is currently not buildable with Maven. However, the project can be build by exporting a runnable jar with all dependencies in it. The line from plugin.properties has to be injected into the plugin.properties file of the exported jar, otherwise the optimization will not run!