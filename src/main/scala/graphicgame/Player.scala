package graphicgame


class Player (private var _x: Double, private var _y: Double, val level: Level) extends Entity {
    def currentPlayer = this

    def x: Double = _x
    def y: Double = _y
    def width: Double = 1.1
    def height: Double = 1.3

    def initialLocation(): Unit = {
        while(!(moveAllowed(_x, _y))){
            _x += 0.3
            _y += 0.3
        }
    } 

    private var movingUp: Boolean = false
    private var movingDown: Boolean = false
    private var movingLeft: Boolean = false
    private var movingRigth: Boolean = false
    private var firingUp: Boolean = false

    def moveUpPressed():Unit = movingUp = true
    def moveDownPressed():Unit = movingDown = true
    def moveLeftPressed():Unit = movingLeft = true
    def moveRightPressed():Unit = movingRigth = true
    def fireUpPressed():Unit = firingUp = true

    def moveUpReleased():Unit = movingUp = false
    def moveDownReleased():Unit = movingDown = false
    def moveLeftReleased():Unit = movingLeft = false
    def moveRightReleased():Unit = movingRigth = false
    def fireUpReleased():Unit = firingUp = false

    def move(dx: Double, dy: Double, moveAllowed: Boolean): Unit = {
        if(moveAllowed){
            _x += dx
            _y += dy
        }
    }

    def moveAllowed(x:Double, y:Double): Boolean = {
        level.maze.isClear(x, y, width, height, currentPlayer)
        //if(intersect)
          //  false
    }

    def update(delay: Double): Unit = {
        if (movingUp) move(0, -0.2, moveAllowed(_x, _y - 0.2))
        if (movingDown) move(0, 0.2, moveAllowed(_x, _y + 0.2))
        if (movingLeft) move(-0.2, 0, moveAllowed(_x - 0.2, _y))
        if (movingRigth) move(0.2, 0, moveAllowed(_x + 0.2, _y))
    }
    def postCheck(): Unit = ???// You can delete this if you don't use it.
    def stillHere(): Boolean = true// This is how you should remove entites from the level.

}