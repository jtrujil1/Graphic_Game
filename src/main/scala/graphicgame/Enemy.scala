package graphicgame

class Enemy (private var _x: Double, private var _y: Double, val level:Level) extends Entity {
    private var dead: Boolean = false
    private var dir: Int = 0
    def x: Double = _x
    def y: Double = _y
    def width: Double = 1.5
    def height: Double = 1.5
    
    def initialLocation(): Unit = {
        while(!(moveAllowed(_x, _y))){
            _x += 0.3
        }
    } 

    def enemy = this

    def move():Unit = {
        var dx:Double = 0
        var dy:Double = 0
        var n = util.Random.nextInt(4) match {
            case 0 => dy += 0.3 //Up
            case 1 => dy -= 0.3 //Down
            case 2 => dx -= 0.3 //Left
            case 3 => dx += 0.3 //Right
        }

        if(moveAllowed(dx, dy)){
        _x += dx
        _y += dy
        }
    }

    def moveAllowed(dx: Double, dy: Double): Boolean = {
        level.maze.isClear(_x + dx, _y + dy, width, height, enemy)
    }

    def update(delay: Double): Unit = {
        enemy.move()
    }
    def postCheck(): Unit = ???
    def stillHere(): Boolean = true
}