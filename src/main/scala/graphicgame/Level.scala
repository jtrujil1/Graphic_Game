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
    var newX = 0.0
    var newY = 0.0
    while (!(currentLevel.maze.isClear(newX, newY, enemy.width, enemy.height, enemy))) {
      newX = util.Random.nextInt(10) * 5 - 3
      newY = util.Random.nextInt(10) * 5 - 3
    }

    var newEnemy1 = new Enemy(newX, newY, currentLevel)
    currentLevel += newEnemy1
    println("created enemy 1")
    println(newX, newY)

    newX = 0.0
    newY = 0.0
    while (!(currentLevel.maze.isClear(newX, newY, enemy.width, enemy.height, enemy))) {
      newX = util.Random.nextInt(10) * 5 - 3
      newY = util.Random.nextInt(10) * 5 - 3
    }

    var newEnemy2 = new Enemy(newX, newY, currentLevel)
    currentLevel += newEnemy2
    println("created enemy 2")
    println(newX, newY)
  }

  def updateAll(delay: Double): Unit = {
    for(i <- 0 until enemies.length){
        if(enemies(i).stillHere == false && enemies.length < 6){
            spawnEnemies(enemies(i))
        }
    }
    _entities = _entities.filter(_.stillHere)
    for (i <- 0 until _entities.length) {
      _entities(i).update(delay)
    }

    println(_entities.length)

  }

}
