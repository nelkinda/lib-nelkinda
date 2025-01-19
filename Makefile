export GIT_SHA?=$(shell git rev-parse HEAD)
export GIT_TIMESTAMP?=$(shell TZ=UTC0 git show --quiet --date='format-local:%Y-%m-%dT%H:%M:%SZ' --format=%cd HEAD)
export BUILD_NUMBER?=Unknown (local)
export BUILD_TIMESTAMP?=$(shell date -u +"%Y-%m-%dT%H:%M:%SZ")

GRADLE:=./gradlew </dev/null

.PHONY: all
## all:	Builds and tests the project.
all:
	$(GRADLE) \
		build \

.PHONY: pitest
## pitest:	Runs mutation tests.
pitest:
	$(GRADLE) \
		pitest \

.PHONY: pipeline
## pipeline:	Runs the same thing as the pipeline.
pipeline: all

.PHONY: owaspScan
## owaspScan:	Runs the OWASP dependency check.
##              It's recommended to setup NVD_API_KEY in the environment (.User.mk).
##              For the Nelkinda key, ask Nelkinda sysadmins.
##              Keys can be obtained from https://nvd.nist.gov/developers/request-an-api-key
owaspScan:
	$(GRADLE) dependencyCheckAnalyze

.PHONY: clean
## clean:	Removes all generated files.
clean::
	$(GRADLE) clean

.PHONY: help
## help:	Prints this help text.
help:
	@sed -En 's/^## ?//p' $(MAKEFILE_LIST)
