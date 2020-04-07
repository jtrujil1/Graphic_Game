package graphicgame

trait Entity extends Serializable {
  def x: Double
  def y: Double
  def width: Double
  def height: Double
  def level: Level
  
  def update(delay: Double): Unit
  def stillHere(): Boolean
  def buildPassable: PassableEntity
}

object Entity {
  def intersect(e1: Entity, e2: Entity): Boolean = {
    ((e1.x-e2.x).abs < (e1.width+e2.width)/2) && ((e1.y-e2.y).abs < (e1.height+e2.height)/2)
  }

  // object EntityType extends Enumeration {
  //   type EntityType = Value
  //   val Player, Enemy, Bullet = Value
    
  //   def entityType: EntityType.Value = match {
  //      case 800 = Player
  //      case 801 = Enemy
  //      case 802 = Bullet
  //   }

  // }
}