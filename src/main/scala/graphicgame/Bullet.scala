package graphicgame

class Bullet(private var _x: Double, private var _y: Double, val level: Level, val dir: String) extends Entity {
  def x: Double = _x
  def y: Double = _y
  def height: Double = 0.6
  def width: Double = 1

  private var fallDelay = 0.0
  val fallInterval = 0.08
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
    fallDelay += delay
    moveDelay += delay
    println(delay)
    println(fallInterval)
    println(_x)
    var dx = 0.0
    var dy = 0.0
    dir match {
      case "u" => dy = -0.2
      case "d" => dy = 0.2
      case "l" => dx = -0.2
      case "r" => dx = 0.2
    }

    if (fallDelay >= fallInterval && moveAllowed(_x + dx, _y + dy)){
        _y += dy
        _x += dx
        fallDelay = 0.0
    }else{
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
