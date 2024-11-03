setlocal EnableDelayedExpansion

set year=%1
set day=%2

set year_folder=year%year%
set day_folder=day%day%
set code_file_name=Day%day%.kt
set test_file_name=Day%day%Test.kt

@REM ###################################################################################################################
cd ./src/main/kotlin/org/example/aoc

if not exist "%year_folder%" mkdir %year_folder%
cd %year_folder%

if not exist "%year_folder%" mkdir %day_folder%
cd %day_folder%

@REM ###################################################################################################################
echo package org.example.aoc.year%year%.%day_folder%>> %code_file_name%
echo:>> %code_file_name%
echo import org.example.common.readFile>> %code_file_name%
echo:>> %code_file_name%
echo fun main() {>> %code_file_name%
echo     val lines = readFile("\\year%year%\\day%day%\\input.txt")>> %code_file_name%
echo     val day%day% = Day%day%()>> %code_file_name%
echo     println(day%day%.part1(lines))>> %code_file_name%
echo     println(day%day%.part2(lines))>> %code_file_name%
echo }>> %code_file_name%
echo:>> %code_file_name%
echo class Day%day% {>> %code_file_name%
echo     fun part1(lines: List^<String^>): Int {>> %code_file_name%
echo         return ^0>> %code_file_name%
echo     }>> %code_file_name%
echo:>> %code_file_name%
echo     fun part2(lines: List^<String^>): Int {>> %code_file_name%
echo         return ^0>> %code_file_name%
echo     }>> %code_file_name%
echo }>> %code_file_name%
@REM ###################################################################################################################

cd ../../../../../../../../


@REM ###################################################################################################################
cd ./src/main/resources

if not exist "%year_folder%" mkdir %year_folder%
cd %year_folder%

if not exist "%year_folder%" mkdir %day_folder%
cd %day_folder%

echo: >> input.txt
@REM ###################################################################################################################

cd ../../../../../


@REM ###################################################################################################################
cd ./src/test/kotlin/org/example/aoc

if not exist "%year_folder%" mkdir %year_folder%
cd %year_folder%

if not exist "%year_folder%" mkdir %day_folder%
cd %day_folder%

@REM ###################################################################################################################
echo package org.example.aoc.year%year%.day%day%>> %test_file_name%
echo:>> %test_file_name%
echo import org.example.common.readTestFile>> %test_file_name%
echo import org.junit.jupiter.api.Test>> %test_file_name%
echo import kotlin.test.assertEquals>> %test_file_name%
echo:>> %test_file_name%
echo class Day%day%Test {>> %test_file_name%
echo:>> %test_file_name%
echo     private val lines = readTestFile("\\year%year%\\day%day%\\test_input.txt")>> %test_file_name%
echo     private val day%day% = Day%day%()>> %test_file_name%
echo:>> %test_file_name%
echo     @Test>> %test_file_name%
echo     fun part1Test() {>> %test_file_name%
echo         assertEquals(0, day%day%.part1(lines))>> %test_file_name%
echo     }>> %test_file_name%
echo:>> %test_file_name%
echo     @Test>> %test_file_name%
echo     fun part2Test() {>> %test_file_name%
echo         assertEquals(0, day%day%.part2(lines))>> %test_file_name%
echo     }>> %test_file_name%
echo }>> %test_file_name%
@REM ###################################################################################################################

cd ../../../../../../../../


@REM ###################################################################################################################
cd ./src/test/resources

if not exist "%year_folder%" mkdir %year_folder%
cd %year_folder%

if not exist "%year_folder%" mkdir %day_folder%
cd %day_folder%

echo: >> test_input.txt
@REM ###################################################################################################################