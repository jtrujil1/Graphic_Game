package graphicgame

class Bullet(private var _x: Double, private var _y: Double, val level: Level, val dir: String) extends Entity {
  def x: Double = _x
  def y: Double = _y
  def height: Double = 0.6
  def width: Double = 1

  def currentBullet = this

  def move(delay: Double, dir: String) = {
    var dx = 0.0
    var dy = 0.0
    dir match {
      case "u" => dy = -0.2
      case "d" => dy = 0.2
      case "l" => dx = -0.2
      case "r" => dx = 0.2
    }

    if (moveAllowed(_x + dx, _y + dy)){ 
        _y += dy
        _x += dx
    }
    if(moveAllowed(_x + dx, _y + dy) == false){
      stopped = true
    }
  }

  def moveAllowed(dx: Double, dy: Double): Boolean = {
    level.maze.isClear(dx, dy, width, height, currentBullet) //&& intersects)
  }

  def initialLocation(): Unit = {
      while(!(moveAllowed(_x, _y))){
          _x += 0.3
          _y += 0.3
      }
  }

  def intersects(): Boolean = {
        for(i <- 0 until level.entities.length){
            if(Entity.intersect(this, level.entities(i)) && level.entities(i) != this && 
              level.bullets.contains(level.entities(i))){
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

  def postCheck(): Unit = ???

  def update(delay: Double): Unit = {
    move(delay, dir)
  }
}
