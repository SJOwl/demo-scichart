# Sci chart sandbox Android application
## Features
Kotlin
[Kotlin DSL]()
[SciChart](https://www.scichart.com/downloads/) library
[Coroutines](https://github.com/Kotlin/kotlinx.coroutines/blob/master/coroutines-guide.md)
[Koin](https://insert-koin.io/)

## Task
[Read the task](https://github.com/SJOwl/demo-scichart/blob/master/test-task.pdf)

## Build
To run this app you need to set your SciChart [license key](https://www.scichart.com/licensing-scichart-android/).
After you get it paste it at `app/build.gradle.kts` to
```
buildConfigField("String", "SciChartLicenseKey", "\"<Your key here>\"")
```

## Screenshots
![Screenshot](./demo.png?raw=true)
