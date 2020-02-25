package graphicgame

class Projectile(private var _x: Double, private var _y: Double, val level: Level) extends Entity {
  def x: Double = _x
  def y: Double = _y
  def height: Double = 1
  def width: Double = 1

  def currentProjectile = this

  def postCheck(): Unit = ???
  def stillHere(): Boolean = ???

  def move() = ???

  def update(delay: Double): Unit = currentProjectile.move()
  
}
