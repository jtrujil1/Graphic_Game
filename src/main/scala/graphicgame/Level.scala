package graphicgame

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class Level(val maze: Maze, private var _entities: Seq[Entity]) {
  val currentLevel = this

  def entities: Seq[Entity] = {
    _entities
  }

  def ghosts = entities.collect { case e: Ghost => e }

  def demons = entities.collect { case d: Demon => d }

  def bullets = entities.collect { case b: Bullet => b }

  def players = entities.collect { case p: Player => p }

  def fire = entities.collect { case f: DemonFire => f}

  def +=(e: Entity): Unit = _entities +:= e

  def spawnEnemies(enemy: Entity, t: String): (Entity, Entity) = {
    var newX, newY, newX2, newY2 = 0.0
    val f1 = Future{
      var newPosition1 = findNewLoc(enemy)
      newX = newPosition1._1
      newY = newPosition1._2
    }
    
    val f2 = Future{
      var newPosition2 = findNewLoc(enemy, newX, newY)
      newX2 = newPosition2._1
      newY2 = newPosition2._2
    }

    Await.result(f1, 3.seconds)
    Await.result(f2, 3.seconds)

    if(t == "g"){
      var newGhost1 = new Ghost(newX, newY, currentLevel)
      newGhost1.initialLocation()
      var newGhost2 = new Ghost(newX2, newY2, currentLevel)
      newGhost2.initialLocation()
      (newGhost1,newGhost2)
    }else{
      var newDemon1 = new Demon(newX, newY, currentLevel)
      newDemon1.initialLocation()
      var newDemon2 = new Demon(newX2, newY2, currentLevel)
      newDemon2.initialLocation()
      (newDemon1,newDemon2)
    }
  }

  def findNewLoc(enemy: Entity, newX:Double = 0.0, newY:Double = 0.0): (Double, Double) = {
    var x = 0.0
    var y = 0.0
    if(players.length > 0){
      do{
        x = (util.Random.nextInt(17) * 4 - 3).abs
        y = (util.Random.nextInt(17) * 4 - 3).abs
      }while (!(currentLevel.maze.isClear(x, y, enemy.width + 1, enemy.height + 1, enemy)) && 
            findClosestPlayers(x, y) &&
            ((newX - x).abs < 1 && (newY - y).abs < 1))
    }else{
      do{
        x = (util.Random.nextInt(17) * 4 - 3).abs
        y = (util.Random.nextInt(17) * 4 - 3).abs
      }while (!(currentLevel.maze.isClear(x, y, enemy.width + 1, enemy.height +1, enemy)) &&
            ((newX - x).abs < 1 && (newY - y).abs < 1))
    }

    (x, y)
  }

  def findClosestPlayers(ex: Double, ey: Double): Boolean = {
    var ret = false
    for(i <- 0 until players.length){
      var dx = players(i).x - ex
      var dy = players(i).y - ey
      var distance = math.sqrt(dx * dx + dy * dy)
      if(distance < 60){
        ret = true
      }
    }
    ret
  }

  def updateAll(delay: Double): Unit = {
      var newGhosts = Seq[Entity]()
      for(i <- 0 until ghosts.length){
          if(ghosts(i).stillHere == false && ghosts.length < 14){
            var newEnem = spawnEnemies(ghosts(i), "g")
            newGhosts +:= newEnem._1
            newGhosts +:= newEnem._2
          }
      }
      for(i <- 0 until newGhosts.length){
        currentLevel += newGhosts(i)
      }

      var newDemons = Seq[Entity]()
      if(demons.filter(_.stillHere).length < 2){
        var tempDemon = new Demon(50, 21, currentLevel)
        var newEnem = spawnEnemies(tempDemon, "d")
        newDemons +:= newEnem._1
        newDemons +:= newEnem._2
      }
      for(i <- 0 until newDemons.length){
        println("new demon")
        currentLevel += newDemons(i)
      }

    _entities = _entities.filter(_.stillHere)
    for (i <- 0 until _entities.length) {
      _entities(i).update(delay)
    }
  }

  def buildPassable: PassableLevel = {
    val entities = for (entity <- _entities) yield {
      entity.buildPassable
    }
    PassableLevel(maze, entities)
  }
 
}
