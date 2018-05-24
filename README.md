# friendlytalks-api

### Description

This API is the complete re-write of [Friendlytalks Node.js API](https://github.com/petivagyok16/friendlytalks/tree/master/server)
in Java + Spring 5

### How to install and run the project
- import root folder as a project to IntelliJ
- install maven dependencies
- install Lombok plugin inside IntelliJ (preferences/plugins/browse plugins)
- Run a MongoDB container using Docker: `docker run -p 27071:27017 -d mongo`
- Use your favorite mongo client to visit mongoDB e.g. [Robo 3T](https://robomongo.org/)
- If you experience issues during the startup consider to click `File/Invalidate Caches/restart`
 in IntelliJ and/or inside `Preferences/Settings` -> `Build/Execution, Deployment` -> `Compiler` -> `Annotation Processors`
 click `Enable annotation processing`
 - [API Documentation](https://documenter.getpostman.com/view/659719/friendlytalks-api/RW1boKPW#b19e879c-f8fb-454a-b575-ed663ff2a4a3)

### Requirements
- JDK 1.8
- IntelliJ or Eclipse
- Project language level 8 - Lambdas, type annotations etc.
- Target bytecode version: 1.8
