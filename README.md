# SQuAT Docker

Docker images for the microservices version of SQuAT.

## Contents

The repository contains the sources for the following three SQuAT components and respective Docker containers: 
- Negotiator
- Modifiability bot
- Performance bot (squat-tool)

Also included are The PCM instance for tests with the performance bot (ExtendedSimpleTacticsExample) and the source code of the hadless PerOpteryx implementation. Main classes for testing are included.

The repository is connected to Docker Cloud (https://cloud.docker.com/) for automatically building and provisioning the SQuAT Docker containers. They are hosted at https://hub.docker.com/u/squatteam/

## Howto run SQuAT using the public Docker containers

After cloning the repository, you should be able to execute the current configuration by executing

```bash
docker-compose up
```

## Configurung automatic build of Docker containers

For each Docker Container which should be built automatically, an own Docker Cloud repository is required.
To set up an autobuild, you can link the Docker Cloud repository (DCR) with a GitHub or BitBucket repository.

Once you have created a DCR linked to a source code repository you can "Configure Automated Builds" in the Builds tab on Docker Cloud. In the section "BUILD RULES" you can define the rules to build the Docker Container.

### Example modifiability-bot:
To trigger the build of the Docker Container with every push to the source code repositorys' master, use the following rule:

Parameter:		Source Type		Source		Docker Tag		Dockerfile location		Build Context			Autobuild	Build Caching
Value:			Branch			master		latest			Dockerfile				/squat.modifiability	ON			ON

Description:
Source Type selects whether the container should be build from a branch or a tag.
Source sets the branch/ tag of the source code repository, the docker container should be built from.
Dockerfile location sets the path and the name of the Dockerfile, which should be used to build the docker container. The Dockerfile location has to be set relatively to the Build Context.
Build Context specifies the path to the Dockerfile in the source code repository.
Autobuild enables/ disables the Containers' autobuild after the current rule.
Build Caching speeds up the build of the Docker Container by caching the build steps.

Further information on https://docs.docker.com/docker-cloud/builds/automated-build/
