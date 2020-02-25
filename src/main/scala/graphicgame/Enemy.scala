package graphicgame

class Enemy (private var _x: Double, private var _y: Double, val level:Level) extends Entity {
    private var dead: Boolean = false
    private var dir: Int = 0
    def x: Double = _x
    def y: Double = _y
    def width: Double = 1.5
    def height: Double = 1.5

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

    val moveInterval = 0.05
    private var moveDelay = 0.0

    def move(delay: Double, dir: String) = {
        //println(delay,moveDelay, dir)
        moveDelay += delay
        var dx = 0.0
        var dy = 0.0
        dir match {
        case "u" => dy = -0.2
        case "d" => dy = 0.2
        case "l" => dx = -0.2
        case "r" => dx = 0.2
        }

        //println(moveInterval, moveAllowed(dx, dy))
        if (moveAllowed(dx, dy) && moveDelay >= moveInterval){
            _y += dy
            _x += dx
            moveDelay = 0.0
        }
    }

    def moveAllowed(dx: Double, dy: Double): Boolean = {
        level.maze.isClear(_x + dx, _y + dy, width, height, enemy) //&& intersects)
    }

    def intersects: Boolean = {
        for(i <- 0 until level.entities.length){
            if(Entity.intersect(this, level.entities(i)) && level.entities(i) != this){
                return false
                while(moveAllowed(_x, _y) == false)
                       _x += 0.3
            }
        }
        return true
    }

    def update(delay: Double): Unit = {
        var up = ShortestPath.breadthFirstShortestPath(_x, _y - 1, level.players(0).x, level.players(0).y, enemy)
        var down = ShortestPath.breadthFirstShortestPath(_x, _y + 1, level.players(0).x, level.players(0).y, enemy)
        var left = ShortestPath.breadthFirstShortestPath(_x - 1, _y, level.players(0).x, level.players(0).y, enemy)
        var right = ShortestPath.breadthFirstShortestPath(_x + 1, _y, level.players(0).x, level.players(0).y, enemy)

        // if(up == down || up == left || up == right) if(up != 1000000000) up += 2
        // if(down == up || down == left || down == right) if(down != 1000000000) down -= 1
        // if(left == up || left == down || left == right) if(left != 1000000000) left += 1
        // if(right == up || right == down || right == left) if(right != 1000000000) right -= 2

        println(up, down, left, right)        

        if(up <= down && up <= left && up <= right) enemy.move(delay, "u")
        if(left <= down && left <= up && left <= right) enemy.move(delay, "l")
        if(right <= down && right <= left && right <= up) enemy.move(delay, "r")
        if(down <= up && down <= left && down <= right) enemy.move(delay, "d")
    }

    def postCheck(): Unit = ???

    def stillHere(): Boolean = {
        var ret = true
        // if(level.bullets.length > 0){
        //     for(i <- 0 until level.bullets.length){
        //         if(Entity.intersect(this, level.bullets(i))){
        //             ret = false
        //         }
        //     }
        // }
        return ret
    }
        

}