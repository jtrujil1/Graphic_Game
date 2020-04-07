package graphicgame

import scalafx.scene.input.KeyCode

object KeyData {
    val KeyPressed = 1001
    val KeyReleased = 1002
    val Up = 0
    val Down = 1
    val Left = 2
    val Right = 3
    val W = 4
    val A = 5
    val S = 6
    val D = 7

    val codeToInt: Map[KeyCode, Int] = Map(KeyCode.Up -> Up, KeyCode.Down -> Down, KeyCode.Left -> Left, KeyCode.Right -> Right,
                                        KeyCode.W -> W, KeyCode.A -> A, KeyCode.S -> S, KeyCode.D -> D)
}
