package graphicgame

class Demon (private var _x: Double, private var _y: Double, val level:Level) extends Entity {
    private var dead: Boolean = false
    private var dir: Int = 0
    def x: Double = _x
    def y: Double = _y
    def width: Double = 1.3
    def height: Double = 1.3
    val speed = 4
    private var direction = "u"
    private var canMove = true
    val directionsArr = Array("u", "d", "l", "r")
    private var count = 0

    def enemy = this

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
            canMove = true
        }else canMove = false
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
            enemy.move(direction, delay)
            if(!canMove){
                direction = directionsArr(util.Random.nextInt(4))
            }
            if(count%11 == 0){
                if(count%10 == 4){
                    var fire1 = new DemonFire(_x, _y - 1, level, "u")
                    var fire2 = new DemonFire(_x - 1, _y, level, "l")
                    var fire3 = new DemonFire(_x, _y + 1, level, "d")
                    var fire4 = new DemonFire(_x + 1, _y, level, "r")
                    level += fire1
                    level += fire2
                    level += fire3
                    level += fire4
                }
            }
            if(count > 100000) count = 1
            count += 1
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
        var player: Player = null
        if(level.bullets.length > 0){
            for(i <- 0 until level.bullets.length){
                if(Entity.intersect(this, level.bullets(i))){
                    ret = false
                    if(player == null) player = level.bullets(i).shooter
                }
            }
        }
        if(player != null) player.score += 200
        return ret
    }

    def buildPassable = PassableEntity(803, _x, _y, width, height)
        
}