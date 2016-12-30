## zipkin ##
[Zipkin](http://zipkin.io) is a distributed tracing system. It helps gather timing data needed to troubleshoot latency problems in microservice architectures. It manages both the collection and lookup of this data. Zipkin’s design is based on the [Google Dapper paper](http://research.google.com/pubs/pub36356.html).

This project includes a dependency-free library and a [spring-boot](http://projects.spring.io/spring-boot/) server. Storage options include in-memory, JDBC (mysql), Cassandra, and Elasticsearch.


## Brave ##

[![Build Status](https://travis-ci.org/openzipkin/brave.svg?branch=master)](https://travis-ci.org/openzipkin/brave)
[![Maven Central](https://img.shields.io/maven-central/v/io.zipkin.brave/brave.svg)](https://maven-badges.herokuapp.com/maven-central/io.zipkin.brave/brave)

Java Distributed Tracing implementation compatible with [Zipkin](http://zipkin.io).

Zipkin is based on [Dapper](http://research.google.com/pubs/pub36356.html).

dapper (dutch) = brave (english)... so that's where the name comes from.

## Brave introduction ##

More information on Distributed Tracing and OpenZipkin here: <https://openzipkin.github.io>

You can use brave if you use the JVM and:

*   You can't use [Finagle](https://github.com/twitter/finagle).
*   You don't want to add Scala as a dependency to your Java project.
*   You want out of the box integration support for [RESTEasy](http://resteasy.jboss.org), [Jersey](https://jersey.java.net), [Apache HttpClient](http://hc.apache.org/httpcomponents-client-4.3.x/index.html).

Brave is compatible with OpenZipkin and its back-end components (zipkin-collector, zipkin-query, zipkin-web). 


## Motan introduction ##
Motan is a remote procedure call(RPC) framework for rapid development of high performance distributed services.

# Features
- Create distributed services without writing extra code.
- Provides cluster support and integrate with popular service discovery services like [Consul][consul] or [Zookeeper][zookeeper]. 
- Supports advanced scheduling features like weighted load-balance, scheduling cross IDCs, etc.
- Optimization for high load scenarios, provides high availability in production environment.




## Maven artifacts ##

Maven artifacts for each release are published to Maven Central. 

## Changelog ##

For an overview of the available releases see [Github releases](https://github.com/kristofa/brave/releases).
As of release 2.0 we try to stick to [Semantic Versioning](http://semver.org).


