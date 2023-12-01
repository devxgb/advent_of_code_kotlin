package org.example.day1

import org.example.common.readFile

fun main() {
    //Question 1
    var maxCalories = 0
    var sum = 0


    val lines = readFile("\\year2022\\day1\\input.txt")
    var i = 0
    while (true) {
        if(i >= lines.size) {
            if (sum > maxCalories) {
                maxCalories = sum
            }
            break
        }
        val line = lines[i]
        if (line.isEmpty()) {
            if (sum > maxCalories) {
                maxCalories = sum
            }
            sum = 0
        } else {
            sum += line.toInt()
        }
        i++
    }
    println(maxCalories)

    /********Question2*****************/
    var top3 = readFile("\\day1\\input.txt")
        .foldRight(MutableList(1) { mutableListOf() }) { str: String, list: MutableList<MutableList<String>> ->
            if (str.isEmpty()) {
                list.add(mutableListOf())
            } else {
                list[list.size - 1].add(str)
            }
            list
        }
        .map { list -> list.map { it.toInt() } }
        .map { it.reduce { sum, i -> sum + i } }
        //.onEach { println(it) }
        .foldRight(Top3(0, 0, 0)) { i: Int, top3: Top3 -> top3.update(i) }
    println(top3.max+top3.secondMax+top3.thirdMax)
}

/**
 * holds top 3 element
 */
class Top3(
    val max: Int,
    val secondMax: Int,
    val thirdMax: Int
) {
    fun update(num: Int): Top3 {
        return if (num == max || num == secondMax || num == thirdMax) {
            Top3(max, secondMax, thirdMax)
        } else if (num > max) {
            Top3(num, max, secondMax)
        } else if (num > secondMax) {
            Top3(max, num, secondMax)
        } else if (num > thirdMax) {
            Top3(max, secondMax, num)
        } else {
            Top3(max, secondMax, thirdMax)
        }
    }

    override fun toString(): String {
        return "max: $max, secondMax: $secondMax, thirdMax: $thirdMax"
    }
}
