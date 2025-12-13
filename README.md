# Advent-of-code solutions in Kotlin

<img src="https://adventofcode.com/favicon.png" alt="Advent of code logo" width="64"/>
<br/>

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="/asset/Kotlin-Full-Color-Logo-on-Black-RGB.svg">
  <source media="(prefers-color-scheme: light)" srcset="/asset/Kotlin-Full-Color-Logo-on-White-RGB.svg">
  <img alt="Kotlin logo" src="/asset/Kotlin-Full-Color-Logo-on-White-RGB.svg" width="256">
</picture>

## Starter script

Includes a gradle task which creates .kt files and .txt files for code, test and input in proper folder.

1. Run gradle task
    ```shell
    ./gradlew aocCreateStarterFile
    ```
2. Enter year(example: 2024) and day(example: 04) when prompted.

## Init script

When cloning the repo for the very first time, run following gradle task to create the folder structure for input files.

```shell
./gradlew aocInitInputFolder
```

The input files are not commited to the repo
as [requested by the creator of AOC](https://adventofcode.com/2025/about#faq_copying). So this init script helps to
create the folder structure.
