package graphicgame

class Ghost (private var _x: Double, private var _y: Double, val level:Level) extends Entity {
    private var dead: Boolean = false
    private var dir: Int = 0
    def x: Double = _x
    def y: Double = _y
    def width: Double = 1
    def height: Double = 1.3
    val speed = 5.5

    def enemy = this

    def move(delay: Double):Unit = {
        var speedUp = 1.0
        if(delay < 0.00005) speedUp = 20
        var dx:Double = 0
        var dy:Double = 0
        var n = util.Random.nextInt(4) match {
            case 0 => dy += speed*delay*speedUp //Up
            case 1 => dy -= speed*delay*speedUp //Down
            case 2 => dx -= speed*delay*speedUp //Left
            case 3 => dx += speed*delay*speedUp //Right
        }

        if(moveAllowed(dx, dy)){
        _x += dx
        _y += dy
        }
    }

    def move(dir: String, delay: Double, ignore: Boolean = false) = {
        var dx = 0.0
        var dy = 0.0
        dir match {
        case "u" => dy = -speed*delay
        case "d" => dy = speed*delay
        case "l" => dx = -speed*delay
        case "r" => dx = speed*delay
        }

        if (moveAllowed(dx, dy) || ignore){
            _y += dy
            _x += dx
        }
    }

    def move(newX: Double, newY: Double, delay: Double) = {
        if(moveAllowed(newX, newY)){
            _x += newX
            _y += newY
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
            var cp = findClosestPlayer(x, y)
            var dx = cp.x - _x
            var dy = cp.y - _y
            var distance = math.sqrt(dx * dx + dy * dy)

            if(distance < 2.5){
                enemy.move(dx/distance*speed*delay, dy/distance*speed*delay, delay)
            }else{
                var up = ShortestPath.breadthFirstShortestPath(_x, _y - 1, cp.x, cp.y, enemy)
                var down = ShortestPath.breadthFirstShortestPath(_x, _y + 1, cp.x, cp.y, enemy)
                var left = ShortestPath.breadthFirstShortestPath(_x - 1, _y, cp.x, cp.y, enemy)
                var right = ShortestPath.breadthFirstShortestPath(_x + 1, _y, cp.x, cp.y, enemy)    

                if(up <= down && up <= left && up <= right) enemy.move("u", delay)
                if(left <= down && left <= up && left <= right) enemy.move("l", delay)
                if(right <= down && right <= left && right <= up) enemy.move("r", delay)
                if(down <= up && down <= left && down <= right) enemy.move("d", delay)
            }
        }else{
            // println("delay:" + delay)
            // if(delay >= 1/25000.0) 
            enemy.move(delay)
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
        var player:Player = null
        if(level.bullets.length > 0){
            for(i <- 0 until level.bullets.length){
                if(Entity.intersect(this, level.bullets(i))){
                    if (player == null) player = level.bullets(i).shooter
                    ret = false
                }
            }
        }
        if(player != null) player.score += 50
        return ret
    }

    def findClosestPlayer(ex: Double, ey: Double): Player = {
        var closestPlayer: Player = null
        var minDistance = 300.0
        for(i <- 0 until level.players.length){
            var dx = level.players(i).x - _x
            var dy = level.players(i).y - _y
            var distance = math.sqrt(dx * dx + dy * dy)
            if(distance < minDistance){
                minDistance = distance
                closestPlayer = level.players(i)
            }
        }
        closestPlayer
    }

    def buildPassable = PassableEntity(801, _x, _y, width, height)
        
}