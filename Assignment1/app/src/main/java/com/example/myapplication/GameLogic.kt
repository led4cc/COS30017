package com.example.myapplication

data class GameState(val hold: Int = 0, val score: Int = 0, val fallen: Boolean = false)

fun zoneForHold(hold: Int): Int = when (hold) {
    in 1..3 -> 1
    in 4..6 -> 2
    in 7..9 -> 3
    else -> 0
}

fun climb(s: GameState): GameState {
    if (s.fallen || s.hold >= 9) return s
    val nextHold = s.hold + 1
    val add = when (nextHold) {
        in 1..3 -> 1
        in 4..6 -> 2
        else -> 3
    }
    return s.copy(hold = nextHold, score = minOf(18, s.score + add))
}

fun fall(s: GameState): GameState {
    if (s.hold == 0) return s                     // can't fall before hold 1
    if (s.hold >= 9) return s.copy(fallen = true) // no penalty at top
    return s.copy(fallen = true, score = maxOf(0, s.score - 3))
}

fun reset(): GameState = GameState()
