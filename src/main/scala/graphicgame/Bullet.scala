package graphicgame

class Bullet(private var _x: Double, private var _y: Double, val level: Level, val dir: String, val shooter: Player) extends Entity {
  def x: Double = _x
  def y: Double = _y
  def height: Double = 0.25
  def width: Double = 0.25

  def currentBullet = this

  def move(delay: Double, dir: String) = {
    var speed = 10
    var dx = 0.0
    var dy = 0.0
    dir match {
      case "u" => dy = -speed*delay
      case "d" => dy = speed*delay
      case "l" => dx = -speed*delay
      case "r" => dx = speed*delay
    }

    if (moveAllowed(_x + dx, _y + dy)){ 
        _y += dy
        _x += dx
    }else{
      stopped = true
    }
  }

  def moveAllowed(dx: Double, dy: Double): Boolean = {
    level.maze.isClear(dx, dy, width, height, currentBullet)
  }

  def initialLocation(): Unit = {
      while(!(moveAllowed(_x, _y))){
          _x += 0.3
          _y += 0.3
      }
  }

  def intersects(): Boolean = {
        for(i <- 0 until level.entities.length){
            if(Entity.intersect(this, level.entities(i)) && (level.ghosts.contains(level.entities(i)) ||
              level.demons.contains(level.entities(i)))){
                return true
            }
        }
        return false
    }

  var stopped = false
  def stillHere(): Boolean = {
    if(stopped || intersects())
      false
    else
      true
  }

  def update(delay: Double): Unit = {
    move(delay, dir)
  }

  def buildPassable = PassableEntity(802, _x, _y, width, height)
}
