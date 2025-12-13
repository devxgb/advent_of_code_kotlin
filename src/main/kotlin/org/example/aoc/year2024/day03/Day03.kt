package org.example.aoc.year2024.day03

import org.example.common.InputReader

fun main() {
    val lines = InputReader().readInput("2024", "3")
    val day03 = Day03()
    println(day03.part1(lines))
    println(day03.part2(lines))
}

// TODO Improvements:
//  Make Lexer more generic
//  Make Parser work without duplicating list
class Day03 {
    fun part1(lines: List<String>): Int {
        val tokenList = Lexer(lines.joinToString(separator = "\n").asSequence()).getTokenSequence().toList()
        val result =
          List(6) { index -> tokenList.drop(index).dropLast(5 - index) }
            .fold(initial = List<List<Token>>(tokenList.size) { emptyList() }) { acc, element -> acc.zip(element) { a, b -> a.plus(b) } }
            .filter { each -> each.first() is InsMul }
            .map { each -> Parser(each) }
            .filter { each -> each.isValid }
            .sumOf { each -> each.parsedValue() }

        return result
    }

    fun part2(lines: List<String>): Int {
        val tokenList = Lexer2(lines.joinToString(separator = "\n").asSequence()).getTokenSequence().toList()
        val result =
          List(6) { index -> tokenList.drop(index).dropLast(5 - index) }
            .fold(initial = List<List<Token>>(tokenList.size) { emptyList() }) { acc, element -> acc.zip(element) { a, b -> a.plus(b) } }
            .filter { each -> each.first() is InsMul }
            .map { each -> Parser(each) }
            .filter { each -> each.isValid }
            .sumOf { each -> each.parsedValue() }

        return result
    }

    private class Lexer(private val charSequence: Sequence<Char>) {

        private class StateMachine {
            companion object {

                private interface State {
                    fun nextStateAndToken(nextChar: Char): Pair<State, List<Token>>
                }

                private class Started : State {
                    override fun nextStateAndToken(nextChar: Char): Pair<State, List<Token>> {
                        return when {
                            nextChar == '(' -> Started() to listOf(ParenOpen())
                            nextChar == ')' -> Started() to listOf(ParenClose())
                            nextChar == ',' -> Started() to listOf(Comma())
                            nextChar.isDigit() -> DetectedDigit.start(nextChar) to emptyList()
                            nextChar == 'm' -> DetectedM() to emptyList()
                            else -> DetectedBadToken.start(nextChar) to listOf()
                        }
                    }
                }

                private class DetectedDigit private constructor(private val digits: List<Char> = emptyList()) : State {
                    companion object {
                        fun start(char: Char): DetectedDigit = DetectedDigit(listOf(char))
                    }

                    private fun append(digit: Char): DetectedDigit {
                        require(digit.isDigit())
                        return DetectedDigit(digits.plus(digit))
                    }

                    private fun finalize(): Token {
                        require(digits.isNotEmpty())
                        return if (digits.size <= 3) {
                            TokenInt(digits.joinToString(separator = ""))
                        } else {
                            TokenBad(digits.joinToString(separator = ""))
                        }
                    }

                    override fun nextStateAndToken(nextChar: Char): Pair<State, List<Token>> {
                        return when {
                            nextChar.isDigit() -> this.append(nextChar) to emptyList()
                            nextChar == '(' -> Started() to listOf(finalize(), ParenOpen())
                            nextChar == ')' -> Started() to listOf(finalize(), ParenClose())
                            nextChar == ',' -> Started() to listOf(finalize(), Comma())
                            nextChar == 'm' -> DetectedM() to listOf(finalize())
                            else -> DetectedBadToken.start(nextChar) to listOf(finalize())
                        }
                    }
                }

                private class DetectedM : State {
                    override fun nextStateAndToken(nextChar: Char): Pair<State, List<Token>> {
                        return when {
                            nextChar == 'u' -> DetectedMU() to emptyList()
                            nextChar == '(' -> Started() to listOf(TokenBad("m"), ParenOpen())
                            nextChar == ')' -> Started() to listOf(TokenBad("m"), ParenClose())
                            nextChar == ',' -> Started() to listOf(TokenBad("m"), Comma())
                            nextChar.isDigit() -> DetectedDigit.start(nextChar) to listOf(TokenBad("m"))
                            nextChar == 'm' -> DetectedM() to listOf(TokenBad("m"))
                            else -> DetectedBadToken.start('m', nextChar) to emptyList()
                        }
                    }
                }

                private class DetectedMU : State {
                    override fun nextStateAndToken(nextChar: Char): Pair<State, List<Token>> {
                        return when {
                            nextChar == 'l' -> Started() to listOf(InsMul())
                            nextChar == '(' -> Started() to listOf(TokenBad("mu"), ParenOpen())
                            nextChar == ')' -> Started() to listOf(TokenBad("mu"), ParenClose())
                            nextChar == ',' -> Started() to listOf(TokenBad("mu"), Comma())
                            nextChar.isDigit() -> DetectedDigit.start(nextChar) to listOf(TokenBad("mu"))
                            nextChar == 'm' -> DetectedM() to listOf(TokenBad("mu"))
                            else -> DetectedBadToken.start('m', 'u', nextChar) to emptyList()
                        }
                    }
                }

                private class DetectedBadToken private constructor(private val chars: List<Char>) : State {
                    companion object {
                        fun start(vararg chars: Char): DetectedBadToken = DetectedBadToken(chars.toList())
                    }

                    private fun append(char: Char): DetectedBadToken {
                        return DetectedBadToken(chars.plus(char))
                    }

                    private fun finalize(): Token {
                        require(chars.isNotEmpty())
                        return TokenBad(chars.joinToString(separator = ""))
                    }

                    override fun nextStateAndToken(nextChar: Char): Pair<State, List<Token>> {
                        return when {
                            nextChar.isDigit() -> DetectedDigit.start(nextChar) to listOf(finalize())
                            nextChar == '(' -> Started() to listOf(finalize(), ParenOpen())
                            nextChar == ')' -> Started() to listOf(finalize(), ParenClose())
                            nextChar == ',' -> Started() to listOf(finalize(), Comma())
                            nextChar == 'm' -> DetectedM() to listOf(finalize())
                            else -> append(nextChar) to emptyList()
                        }
                    }
                }
            }

            private var currentState: State = Started()

            fun next(char: Char): List<Token> {
                val (newState, tokens) = currentState.nextStateAndToken(char)
                currentState = newState
                return tokens
            }
        }

        private val tokenSequence: Sequence<Token> = sequence {
            val stateMachine = StateMachine()
            charSequence.forEach { char -> yieldAll(stateMachine.next(char)) }
        }

        fun getTokenSequence(): Sequence<Token> = tokenSequence
    }

    private class Lexer2(private val charSequence: Sequence<Char>) {

        private class StateMachine {
            companion object {

                private interface State {
                    fun nextStateAndToken(nextChar: Char): Pair<State, List<Token>>
                }

                private class Started : State {
                    override fun nextStateAndToken(nextChar: Char): Pair<State, List<Token>> {
                        return when {
                            nextChar == '(' -> Started() to listOf(ParenOpen())
                            nextChar == ')' -> Started() to listOf(ParenClose())
                            nextChar == ',' -> Started() to listOf(Comma())
                            nextChar.isDigit() -> DetectedDigit.start(nextChar) to emptyList()
                            nextChar == 'm' -> DetectedM() to emptyList()
                            nextChar == 'd' -> DetectedD() to emptyList()
                            else -> DetectedBadToken.start(nextChar) to listOf()
                        }
                    }
                }

                private class DetectedDigit private constructor(private val digits: List<Char> = emptyList()) : State {
                    companion object {
                        fun start(char: Char): DetectedDigit = DetectedDigit(listOf(char))
                    }

                    private fun append(digit: Char): DetectedDigit {
                        require(digit.isDigit())
                        return DetectedDigit(digits.plus(digit))
                    }

                    private fun finalize(): Token {
                        require(digits.isNotEmpty())
                        return if (digits.size <= 3) {
                            TokenInt(digits.joinToString(separator = ""))
                        } else {
                            TokenBad(digits.joinToString(separator = ""))
                        }
                    }

                    override fun nextStateAndToken(nextChar: Char): Pair<State, List<Token>> {
                        return when {
                            nextChar.isDigit() -> this.append(nextChar) to emptyList()
                            nextChar == '(' -> Started() to listOf(finalize(), ParenOpen())
                            nextChar == ')' -> Started() to listOf(finalize(), ParenClose())
                            nextChar == ',' -> Started() to listOf(finalize(), Comma())
                            nextChar == 'm' -> DetectedM() to listOf(finalize())
                            nextChar == 'd' -> DetectedD() to listOf(finalize())
                            else -> DetectedBadToken.start(nextChar) to listOf(finalize())
                        }
                    }
                }

                private class DetectedM : State {
                    override fun nextStateAndToken(nextChar: Char): Pair<State, List<Token>> {
                        return when {
                            nextChar == 'u' -> DetectedMU() to emptyList()
                            nextChar == '(' -> Started() to listOf(TokenBad("m"), ParenOpen())
                            nextChar == ')' -> Started() to listOf(TokenBad("m"), ParenClose())
                            nextChar == ',' -> Started() to listOf(TokenBad("m"), Comma())
                            nextChar.isDigit() -> DetectedDigit.start(nextChar) to listOf(TokenBad("m"))
                            nextChar == 'm' -> DetectedM() to listOf(TokenBad("m"))
                            nextChar == 'd' -> DetectedD() to listOf(TokenBad("m"))
                            else -> DetectedBadToken.start('m', nextChar) to emptyList()
                        }
                    }
                }

                private class DetectedMU : State {
                    override fun nextStateAndToken(nextChar: Char): Pair<State, List<Token>> {
                        return when {
                            nextChar == 'l' -> Started() to listOf(InsMul())
                            nextChar == '(' -> Started() to listOf(TokenBad("mu"), ParenOpen())
                            nextChar == ')' -> Started() to listOf(TokenBad("mu"), ParenClose())
                            nextChar == ',' -> Started() to listOf(TokenBad("mu"), Comma())
                            nextChar.isDigit() -> DetectedDigit.start(nextChar) to listOf(TokenBad("mu"))
                            nextChar == 'm' -> DetectedM() to listOf(TokenBad("mu"))
                            nextChar == 'd' -> DetectedD() to listOf(TokenBad("mu"))
                            else -> DetectedBadToken.start('m', 'u', nextChar) to emptyList()
                        }
                    }
                }

                private class DetectedBadToken private constructor(private val chars: List<Char>) : State {
                    companion object {
                        fun start(vararg chars: Char): DetectedBadToken = DetectedBadToken(chars.toList())
                    }

                    private fun append(char: Char): DetectedBadToken {
                        return DetectedBadToken(chars.plus(char))
                    }

                    private fun finalize(): Token {
                        require(chars.isNotEmpty())
                        return TokenBad(chars.joinToString(separator = ""))
                    }

                    override fun nextStateAndToken(nextChar: Char): Pair<State, List<Token>> {
                        return when {
                            nextChar.isDigit() -> DetectedDigit.start(nextChar) to listOf(finalize())
                            nextChar == '(' -> Started() to listOf(finalize(), ParenOpen())
                            nextChar == ')' -> Started() to listOf(finalize(), ParenClose())
                            nextChar == ',' -> Started() to listOf(finalize(), Comma())
                            nextChar == 'm' -> DetectedM() to listOf(finalize())
                            nextChar == 'd' -> DetectedD() to listOf(finalize())
                            else -> append(nextChar) to emptyList()
                        }
                    }
                }

                private class DetectedD : State {
                    override fun nextStateAndToken(nextChar: Char): Pair<State, List<Token>> {
                        return when {
                            nextChar == 'o' -> DetectedDO() to emptyList()
                            nextChar == '(' -> Started() to listOf(TokenBad("d"), ParenOpen())
                            nextChar == ')' -> Started() to listOf(TokenBad("d"), ParenClose())
                            nextChar == ',' -> Started() to listOf(TokenBad("d"), Comma())
                            nextChar.isDigit() -> DetectedDigit.start(nextChar) to listOf(TokenBad("d"))
                            nextChar == 'm' -> DetectedM() to listOf(TokenBad("d"))
                            nextChar == 'd' -> DetectedD() to listOf(TokenBad("d"))
                            else -> DetectedBadToken.start('d', nextChar) to emptyList()
                        }
                    }
                }

                private class DetectedDO : State {
                    override fun nextStateAndToken(nextChar: Char): Pair<State, List<Token>> {
                        return when {
                            nextChar == 'n' -> DetectedDON() to emptyList()
                            nextChar == '(' -> Started() to listOf(TokenBad("do"), ParenOpen())
                            nextChar == ')' -> Started() to listOf(TokenBad("do"), ParenClose())
                            nextChar == ',' -> Started() to listOf(TokenBad("do"), Comma())
                            nextChar.isDigit() -> DetectedDigit.start(nextChar) to listOf(TokenBad("do"))
                            nextChar == 'm' -> DetectedM() to listOf(TokenBad("do"))
                            nextChar == 'd' -> DetectedD() to listOf(TokenBad("do"))
                            else -> DetectedBadToken.start('d', 'o', nextChar) to emptyList()
                        }
                    }
                }

                private class DetectedDON : State {
                    override fun nextStateAndToken(nextChar: Char): Pair<State, List<Token>> {
                        return when {
                            nextChar == '\'' -> `DetectedDON'`() to emptyList()
                            nextChar == '(' -> Started() to listOf(TokenBad("don"), ParenOpen())
                            nextChar == ')' -> Started() to listOf(TokenBad("don"), ParenClose())
                            nextChar == ',' -> Started() to listOf(TokenBad("don"), Comma())
                            nextChar.isDigit() -> DetectedDigit.start(nextChar) to listOf(TokenBad("don"))
                            nextChar == 'm' -> DetectedM() to listOf(TokenBad("don"))
                            nextChar == 'd' -> DetectedD() to listOf(TokenBad("don"))
                            else -> DetectedBadToken.start('d', 'o', 'n', nextChar) to emptyList()
                        }
                    }
                }

                private class `DetectedDON'` : State {
                    override fun nextStateAndToken(nextChar: Char): Pair<State, List<Token>> {
                        return when {
                            nextChar == 't' -> `DetectedDON'T`() to emptyList()
                            nextChar == '(' -> Started() to listOf(TokenBad("don'"), ParenOpen())
                            nextChar == ')' -> Started() to listOf(TokenBad("don'"), ParenClose())
                            nextChar == ',' -> Started() to listOf(TokenBad("don'"), Comma())
                            nextChar.isDigit() -> DetectedDigit.start(nextChar) to listOf(TokenBad("don'"))
                            nextChar == 'm' -> DetectedM() to listOf(TokenBad("don'"))
                            nextChar == 'd' -> DetectedD() to listOf(TokenBad("don'"))
                            else -> DetectedBadToken.start('d', 'o', 'n', '\'', nextChar) to emptyList()
                        }
                    }
                }

                private class `DetectedDON'T` : State {
                    override fun nextStateAndToken(nextChar: Char): Pair<State, List<Token>> {
                        return when {
                            nextChar == '(' -> `DetectedDON'T(`() to emptyList()
                            nextChar == ')' -> Started() to listOf(TokenBad("don't"), ParenClose())
                            nextChar == ',' -> Started() to listOf(TokenBad("don't"), Comma())
                            nextChar.isDigit() -> DetectedDigit.start(nextChar) to listOf(TokenBad("don't"))
                            nextChar == 'm' -> DetectedM() to listOf(TokenBad("don't"))
                            nextChar == 'd' -> DetectedD() to listOf(TokenBad("don't"))
                            else -> DetectedBadToken.start('d', 'o', 'n', '\'', 't', nextChar) to emptyList()
                        }
                    }
                }

                private class `DetectedDON'T(` : State {
                    override fun nextStateAndToken(nextChar: Char): Pair<State, List<Token>> {
                        return when {
                            nextChar == '(' -> Started() to listOf(TokenBad("don't("), ParenOpen())
                            nextChar == ')' -> `DetectedDON'T()`() to emptyList()
                            nextChar == ',' -> Started() to listOf(TokenBad("don't("), Comma())
                            nextChar.isDigit() -> DetectedDigit.start(nextChar) to listOf(TokenBad("don't("))
                            nextChar == 'm' -> DetectedM() to listOf(TokenBad("don't("))
                            nextChar == 'd' -> DetectedD() to listOf(TokenBad("don't"))
                            else -> DetectedBadToken.start('d', 'o', 'n', '\'', 't', '(', nextChar) to emptyList()
                        }
                    }
                }

                private class `DetectedDON'T()` : State {
                    override fun nextStateAndToken(nextChar: Char): Pair<State, List<Token>> {
                        return when {
                            nextChar == 'd' -> `DetectedDON'T()D`() to emptyList()
                            else -> `DetectedDON'T()`() to emptyList()
                        }
                    }
                }

                private class `DetectedDON'T()D` : State {
                    override fun nextStateAndToken(nextChar: Char): Pair<State, List<Token>> {
                        return when {
                            nextChar == 'o' -> `DetectedDON'T()DO`() to emptyList()
                            nextChar == 'd' -> `DetectedDON'T()D`() to emptyList()
                            else -> `DetectedDON'T()`() to emptyList()
                        }
                    }
                }

                private class `DetectedDON'T()DO` : State {
                    override fun nextStateAndToken(nextChar: Char): Pair<State, List<Token>> {
                        return when {
                            nextChar == '(' -> `DetectedDON'T()DO(`() to emptyList()
                            nextChar == 'd' -> `DetectedDON'T()D`() to emptyList()
                            else -> `DetectedDON'T()`() to emptyList()
                        }
                    }
                }

                private class `DetectedDON'T()DO(` : State {
                    override fun nextStateAndToken(nextChar: Char): Pair<State, List<Token>> {
                        return when {
                            nextChar == ')' -> Started() to listOf(TokenDontDo())
                            nextChar == 'd' -> `DetectedDON'T()D`() to emptyList()
                            else -> `DetectedDON'T()`() to emptyList()
                        }
                    }
                }
            }

            private var currentState: State = Started()

            fun next(char: Char): List<Token> {
                val (newState, tokens) = currentState.nextStateAndToken(char)
                currentState = newState
                return tokens
            }
        }

        private val tokenSequence: Sequence<Token> = sequence {
            val stateMachine = StateMachine()
            charSequence.forEach { char -> yieldAll(stateMachine.next(char)) }
        }

        fun getTokenSequence(): Sequence<Token> = tokenSequence
    }

    /** E-> ins_mul paren_open int comma int paren_close */
    private class Parser(private val tokenList: List<Token>) {
        companion object {
            class InvalidInstructionException(tokenList: List<Token>) : Exception("Invalid instruction: ${tokenList.joinToString(separator = "|") { it.value }}")
        }

        val isValid: Boolean
        private val parsedValue: Int?

        init {
            if (tokenList.size == 6) {
                val token2 = tokenList[2]
                val token4 = tokenList[4]
                if (
                  (tokenList[0] is InsMul) && (tokenList[1] is ParenOpen) && (token2 is TokenInt) && (tokenList[3] is Comma) && (token4 is TokenInt) && (tokenList[5] is ParenClose)
                ) {
                    isValid = true
                    parsedValue = token2.value.toInt() * token4.value.toInt()
                } else {
                    isValid = false
                    parsedValue = null
                }
            } else {
                isValid = false
                parsedValue = null
            }
        }

        fun parsedValue(): Int = parsedValue ?: throw InvalidInstructionException(tokenList)
    }

    private sealed interface Token {
        val value: String
    }

    private class InsMul : Token {
        override val value: String = "mul"
    }

    private class ParenOpen : Token {
        override val value: String = "("
    }

    private class ParenClose : Token {
        override val value: String = ")"
    }

    private class Comma : Token {
        override val value: String = ","
    }

    private class TokenInt(override val value: String) : Token

    private open class TokenBad(override val value: String) : Token

    private class TokenDontDo : TokenBad("don't()do()")
}
