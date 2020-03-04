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

  def spawnEnemies(enemy: Enemy) {
    var newPosition1 = findNewLoc(enemy)
    var newX = newPosition1._1
    var newY = newPosition1._2

    println(newX, newY)

    var newEnemy1 = new Enemy(newX, newY, currentLevel)
    currentLevel += newEnemy1
    println("created enemy 1")
    println(newX, newY)

    var newPosition2 = findNewLoc(enemy, newX, newY)
    newX = newPosition1._1
    newY = newPosition1._2

    var newEnemy2 = new Enemy(newX, newY, currentLevel)
    currentLevel += newEnemy2
    println("created enemy 2")
    println(newX, newY)
  }

  def findNewLoc(enemy: Enemy, newX:Double = 0.0, newY:Double = 0.0): (Double, Double) = {
    var x = 0.0
    var y = 0.0
    if(players.length > 0){
      while (!(currentLevel.maze.isClear(x, y, enemy.width, enemy.height, enemy)) && 
            //math.sqrt(math.pow((newX - players(0).x), 2) + math.pow((newY - players(0).y), 2)) < 6 &&
            ((newX - x).abs < 1 && (newY - y).abs < 1) &&
            (x <= 0 && y <= 0)){
        x = util.Random.nextInt(10) * 5 - 3
        y = util.Random.nextInt(10) * 5 - 3
      }
    }else{
      while (!(currentLevel.maze.isClear(x, y, enemy.width, enemy.height, enemy)) &&
            ((newX - x).abs < 1 && (newY - y).abs < 1) &&
            (x <= 0 && y <= 0)) {
        x = util.Random.nextInt(10) * 5 - 3
        y = util.Random.nextInt(10) * 5 - 3
      }
    }

    (x, y)
  }

  def updateAll(delay: Double): Unit = {
    for(i <- 0 until enemies.length){
        if(enemies(i).stillHere == false && enemies.length < 10){
            spawnEnemies(enemies(i))
        }
    }

    //if(player.foreach(_.stillHere) == false)
    _entities = _entities.filter(_.stillHere)
    for (i <- 0 until _entities.length) {
      _entities(i).update(delay)
    }

    //println(_entities.length)
  }

}
