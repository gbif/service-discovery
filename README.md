Service Discovery
====================
`service-discovery` is a library that can be used to register a service into [Zookeeper](https://zookeeper.apache.org). 
Connectivity to Zookeeper is provided by Netflix's [Curator](http://curator.apache.org) library and its built in [Service Discovery](http://curator.apache.org/curator-x-discovery/index.html) framework.


Usage
-----
Add this file as dependency to your Maven project and include the following setting in your yaml configuration file:

```YAML
    service:
      zkHost: zk1:2181,zk2:2181,zk3:2181
      zkPath: dev/services
```
