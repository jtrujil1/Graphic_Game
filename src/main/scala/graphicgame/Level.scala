package graphicgame

class Level(val maze: Maze, private var _entities: Seq[Entity]) {
  val currentLevel = this

  def entities: Seq[Entity] = {
    _entities
  }

  def enemies = entities.collect { case e: Enemy => e }

  def bullets = entities.collect { case b: Bullet => b }

  def players = entities.collect { case p: Player => p }

  def +=(e: Entity): Unit = _entities +:= e

  def spawnEnemies(enemy: Enemy): (Enemy, Enemy) = {
    var newPosition1 = findNewLoc(enemy)
    var newX = newPosition1._1
    var newY = newPosition1._2

    var newEnemy1 = new Enemy(newX, newY, currentLevel)
    newEnemy1.initialLocation()

    var newPosition2 = findNewLoc(enemy, newX, newY)
    newX = newPosition1._1
    newY = newPosition1._2

    var newEnemy2 = new Enemy(newX, newY, currentLevel)
    newEnemy2.initialLocation()

    (newEnemy1,newEnemy2)
  }

  def findNewLoc(enemy: Enemy, newX:Double = 0.0, newY:Double = 0.0): (Double, Double) = {
    var x = 0.0
    var y = 0.0
    if(players.length > 0){
      var dx = players(0).x - enemy.x
      var dy = players(0).y - enemy.y
      var distance = math.sqrt(dx * dx + dy * dy)
      do{
        x = (util.Random.nextInt(17) * 4 - 3).abs
        y = (util.Random.nextInt(17) * 4 - 3).abs
      }while (!(currentLevel.maze.isClear(x, y, enemy.width + 1, enemy.height + 1, enemy)) && 
            distance < 20 &&
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

  def updateAll(delay: Double): Unit = {
    var newEnemies = Seq[Enemy]()
    for(i <- 0 until enemies.length){
        if(enemies(i).stillHere == false && enemies.length < 18){
          var newEnem = spawnEnemies(enemies(i))
          newEnemies +:= newEnem._1
          newEnemies +:= newEnem._2
        }
    }

    for(i <- 0 until newEnemies.length) currentLevel += newEnemies(i)

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
