package org.example.aoc.year2022.day7

fun main() {

}

class Dir(val parent: Dir?, val dirs: List<Dir>, private var files: List<File>) {

    private var size: Int = 0

    class File(val size: Int)

    fun addFile(file: File) {
        this.files = this.files.plus(file)
        this.size = this.size + file.size
    }

}

class FileSystem() {
    var root: Dir = Dir(null, emptyList(), emptyList())
        private set

    var pwd: Dir = root
        private set


}