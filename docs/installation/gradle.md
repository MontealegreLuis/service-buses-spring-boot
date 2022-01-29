# Gradle

_This guide assumes you already generated an access token as described in [this guide](https://github.com/MontealegreLuis/service-buses-spring-boot/blob/main/docs/installation/authentication.md)._

Create or update your `~/.gradle/gradle.properties` file with the following content.

```groovy
gpr.user=YOUR_USERNAME
gpr.token=YOUR_TOKEN
```

Alternatively, you can set environment variables `USERNAME` and `TOKEN`.

Declare this project's Maven repository within `repositores` in your `build.gradle` file.

```groovy
maven {
    url = uri("https://maven.pkg.github.com/montealegreluis/service-buses-spring-boot")
    credentials {
        username = project.findProperty("gpr.user") ?: System.getenv("USERNAME")
        password = project.findProperty("gpr.token") ?: System.getenv("TOKEN")
    }
}
```

Lastly, add the following entry to your `dependencies` in your `build.gradle` file.

```groovy
implementation 'com.montealegreluis:service-buses-spring-boot:1.0.0'
```

Please find what the latest version is [here](https://github.com/MontealegreLuis/service-buses-spring-boot/packages/1218842).
