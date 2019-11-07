This project demonstrates using Chaos Toolkit to do chaos experiments against a spring boot application.

For more information about these tools, please see Chaos Toolkit and Chaos Monkey for Spring Boot documentation:
* https://docs.chaostoolkit.org/reference/tutorial/
* https://docs.chaostoolkit.org/drivers/spring/
* https://codecentric.github.io/chaos-monkey-spring-boot/


The project is made up of two packages:

### service
This package contains:
* **ChaosController** - the main entry point of the application.
    * Endpoints: 
        * /service/hello-resilient
        * /service/hello-brittle
* **ChaosService** - "Complex business logic" :) There are two methods here that both call RemoteClient to fetch an id.
    * _getMessageId_ - Includes resilience4j circuit breaker and fallback configuration.
    * _getMessageIdBrittle_ - Does not include any resiliency configuration.
* **RemoteClient** - Simple feign client to fetch data from a remote service

### remote
This package contains 1 class:
* **RemoteController** - a simple rest controller with 1 method. This is the service invoked by RemoteClient. This will be the assault point for chaos monkey.


## Install Required Software
* JDK 8
* Maven
* Chaos Toolkit:  https://chaostoolkit.org/
* Chaos Toolkit Spring Extension: https://chaostoolkit.org/extensions/spring



## Building and Starting the app

```
mvn clean package
java -jar targetchaos-demo-0.0.1-SNAPSHOT.jar
```

## Running the experiments


### experiment-resilient.json

Probes:
* Verify that /service/hello-resilient returns HTTP Status Code 200. This is the endpoint which executes a service with fallbacks.

Actions
* Enable Chaos Monkey
* Configure exception assault to attack RemoteController.random() method.

NOTE: Probes execute again after actions are taken. So once the assault is in place, it will reprobe /service/hello-resilient

Rollback
* Disable Chaos Monkey

To run:

`chaos run experiment-resilient.json`

This should execute successfully and have to deviations. Successful output is shown below:

```
[2019-11-07 17:23:01 INFO] Validating the experiment's syntax
[2019-11-07 17:23:01 INFO] Experiment looks valid
[2019-11-07 17:23:01 INFO] Running experiment: What if our controller throws an exception?
[2019-11-07 17:23:01 INFO] Steady state hypothesis: Application responds
[2019-11-07 17:23:01 INFO] Probe: we-can-query-resilient-endpoint
[2019-11-07 17:23:01 INFO] Steady state hypothesis is met!
[2019-11-07 17:23:01 INFO] Action: enable_chaosmonkey
[2019-11-07 17:23:01 INFO] Action: configure_assaults
[2019-11-07 17:23:01 INFO] Steady state hypothesis: Application responds
[2019-11-07 17:23:01 INFO] Probe: we-can-query-resilient-endpoint
[2019-11-07 17:23:02 INFO] Steady state hypothesis is met!
[2019-11-07 17:23:02 INFO] Let's rollback...
[2019-11-07 17:23:02 INFO] Rollback: disable_chaosmonkey
[2019-11-07 17:23:02 INFO] Action: disable_chaosmonkey
[2019-11-07 17:23:02 INFO] Experiment ended with status: completed

```

### experiment-brittle.json

Probes:
* Verify that /service/hello-brittle returns HTTP Status Code 200. This is the endpoint with no fallbacks.

Actions
* Enable Chaos Monkey
* Configure exception assault to attack RemoteController.random() method.

NOTE: Probes execute again after actions are taken. So once the assault is in place, it will reprobe /service/hello-brittle

Rollback
* Disable Chaos Monkey

To run:

`chaos run experiment-brittle.json`

This should execute but encounter a deviation. Output is shown below

```
[2019-11-07 17:26:25 INFO] Validating the experiment's syntax
[2019-11-07 17:26:25 INFO] Experiment looks valid
[2019-11-07 17:26:25 INFO] Running experiment: What if our controller throws an exception?
[2019-11-07 17:26:25 INFO] Steady state hypothesis: Application responds
[2019-11-07 17:26:25 INFO] Probe: we-can-query-brittle-endpoint
[2019-11-07 17:26:25 INFO] Steady state hypothesis is met!
[2019-11-07 17:26:25 INFO] Action: enable_chaosmonkey
[2019-11-07 17:26:25 INFO] Action: configure_assaults
[2019-11-07 17:26:25 INFO] Steady state hypothesis: Application responds
[2019-11-07 17:26:25 INFO] Probe: we-can-query-brittle-endpoint
[2019-11-07 17:26:25 CRITICAL] Steady state probe 'we-can-query-brittle-endpoint' is not in the given tolerance so failing this experiment
[2019-11-07 17:26:25 INFO] Let's rollback...
[2019-11-07 17:26:25 INFO] Rollback: disable_chaosmonkey
[2019-11-07 17:26:25 INFO] Action: disable_chaosmonkey
[2019-11-07 17:26:25 INFO] Experiment ended with status: deviated
[2019-11-07 17:26:25 INFO] The steady-state has deviated, a weakness may have been discovered
```

A critical message is logged showing that the brittle endpoint is no longer within tolerance.

