package graphicgame

class Level (val maze: Maze, private var _entities:Seq[Entity]){
    val odds = 0.3
    val currentLevel = this

    def entities: Seq[Entity] = {
        _entities
    }

    def enemies = entities.collect { case e: Enemy => e }

    def bullets = entities.collect { case b: Bullet => b }

    def players = entities.collect { case p: Player => p }

    def +=(e: Entity): Unit = _entities +:= e

    // def enemyCreator() = {
    //     var newEnemy = new Enemy (util.Random.nextInt(10)*5 - 3, util.Random.nextInt(10)*5 - 3, currentLevel)
    //     currentLevel += newEnemy
    // }

    var counter = 0

    def updateAll(delay:Double): Unit = {
        _entities = _entities.filter(_.stillHere)
        for(i <- 0 until _entities.length){
                _entities(i).update(delay)
        }

        // counter += 1

        // if(counter > 14){
        //     enemyCreator()
        //     counter = 0
        // }
    }

}