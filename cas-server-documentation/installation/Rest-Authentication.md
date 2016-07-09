---
layout: default
title: CAS - REST Authentication
---

# REST Authentication

REST authentication is enabled by including the following dependencies in the WAR overlay:

```xml
<dependency>
    <groupId>org.apereo.cas</groupId>
    <artifactId>cas-server-support-rest-authentication</artifactId>
    <version>${cas.version}</version>
</dependency>
```

This allows the CAS server to reach to a remote REST endpoint via a `POST` for verification of credentials.
Credentials are passed via an `Authorization` header whose value is `Basic XYZ` where XYZ is a
Base64 encoded version of the credentials. The response that is returned must be accompanied by a `200`
status code where the body should contain `id` and `attributes` fields, the latter being optional,
which represent the authenticated principal for CAS. 

## Configuration

To see the relevant list of CAS properties, please [review this guide](Configuration-Properties.html).