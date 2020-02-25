package graphicgame

class Bullet(private var _x: Double, private var _y: Double, val level: Level, val dir: String) extends Entity {
  def x: Double = _x
  def y: Double = _y
  def height: Double = 0.6
  def width: Double = 1

  val moveInterval = 0.05
  private var moveDelay = 0.0
  def currentBullet = this

  def postCheck(): Unit = ???

  var stopped = false
  def stillHere(): Boolean = {
    if(stopped)
      false
    else
      true
  }

  def move(delay: Double, dir: String) = {
    moveDelay += delay
    var dx = 0.0
    var dy = 0.0
    dir match {
      case "u" => dy = -0.2
      case "d" => dy = 0.2
      case "l" => dx = -0.2
      case "r" => dx = 0.2
    }

    if (moveDelay >= moveInterval && moveAllowed(_x + dx, _y + dy)){
        _y += dy
        _x += dx
        moveDelay = 0.0
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

  def update(delay: Double): Unit = {
    move(delay, dir)
  }
}
