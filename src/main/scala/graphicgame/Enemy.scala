package graphicgame

class Enemy (private var _x: Double, private var _y: Double, val level:Level) extends Entity {
    private var dead: Boolean = false
    private var dir: Int = 0
    def x: Double = _x
    def y: Double = _y
    def width: Double = 1.3
    def height: Double = 1.3

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

    def move(dir: String, delay: Double) = {
        var speed = 2
        var dx = 0.0
        var dy = 0.0
        dir match {
        case "u" => dy = -speed*delay
        case "d" => dy = speed*delay
        case "l" => dx = -speed*delay
        case "r" => dx = speed*delay
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
        if(level.players.length > 0){
            var up = ShortestPath.breadthFirstShortestPath(_x, _y - 1, level.players(0).x, level.players(0).y, enemy)
            var down = ShortestPath.breadthFirstShortestPath(_x, _y + 1, level.players(0).x, level.players(0).y, enemy)
            var left = ShortestPath.breadthFirstShortestPath(_x - 1, _y, level.players(0).x, level.players(0).y, enemy)
            var right = ShortestPath.breadthFirstShortestPath(_x + 1, _y, level.players(0).x, level.players(0).y, enemy)    

            if(up <= down && up <= left && up <= right) enemy.move("u", delay)
            if(left <= down && left <= up && left <= right) enemy.move("l", delay)
            if(right <= down && right <= left && right <= up) enemy.move("r", delay)
            if(down <= up && down <= left && down <= right) enemy.move("d", delay)
        }else{
            enemy.move()
        }
    }

    def initialLocation(): Unit = {
        var dx = 0.3
        var dy = 0.3
        while(!(moveAllowed(dx, dy))){
            dx += 0.3
            dy += 0.3
        }
        _x += dx
        _y += dy
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