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

    def move(dir: String) = {
        var dx = 0.0
        var dy = 0.0
        dir match {
        case "u" => dy = -0.1
        case "d" => dy = 0.1
        case "l" => dx = -0.1
        case "r" => dx = 0.1
        }

        if (moveAllowed(dx, dy)){
            _y += dy
            _x += dx
        }
    }

    def moveAllowed(dx: Double, dy: Double): Boolean = {
        level.maze.isClear(_x + dx, _y + dy, width, height, enemy)
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

        if(up <= down && up <= left && up <= right) enemy.move("u")
        if(left <= down && left <= up && left <= right) enemy.move("l")
        if(right <= down && right <= left && right <= up) enemy.move("r")
        if(down <= up && down <= left && down <= right) enemy.move("d")
    }

    def initialLocation(): Unit = {
        while(!(moveAllowed(_x, _y))){
            _x += 0.3
            _y += 0.3
        }
    }

    def stillHere(): Boolean = {
        var ret = true
        if(level.bullets.length > 0){
            for(i <- 0 until level.bullets.length){
                if(Entity.intersect(this, level.bullets(i))){
                    ret = false
                }
            }
        }
        return ret
    }
        

}