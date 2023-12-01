package org.example.aoc.year2022.day6

import org.example.common.readFile

fun main() {
    val string = readFile("\\year2022\\day6\\input.txt")[0]

    /***** Part1 *****/
    val packetStart = SignalProcessor(4)
    var start: Int = 0
    for ((i, char) in string.withIndex()) {
        if (packetStart.detect(char)) {
            start = i
            break
        }
    }
    println(start + 1)

    /***** Part2 *****/
    val msgStart = SignalProcessor(14)
    for ((i, char) in string.withIndex()) {
        if (msgStart.detect(char)) {
            start = i
            break
        }
    }
    println(start + 1)
}

class SignalProcessor(private val size: Int) {
    private val buffer = ArrayDeque<Char>()
    private var numberOfCharProcessed: Int = 0

    fun detect(char: Char): Boolean {
        numberOfCharProcessed++
        var isStart: Boolean
        if (numberOfCharProcessed < size) {
            buffer.addLast(char)
            isStart = false
        } else {
            isStart = false
            var counter = 0
            val arrayDeque: ArrayDeque<Char> = ArrayDeque(buffer.size)
            for (i in buffer.indices) {
                arrayDeque.addLast(buffer.first())
                if (char == buffer.removeFirst()) {
                    arrayDeque.clear()
                    break
                }
                counter++
            }
            arrayDeque.forEach { buffer.addLast(it) }
            buffer.addLast(char)
            if (counter >= size - 1) {
                isStart = true
            }
        }
        return isStart
    }

}

