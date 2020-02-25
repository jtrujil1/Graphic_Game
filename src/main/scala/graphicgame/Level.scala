package graphicgame

class Level (val maze: Maze, private var _entities:Seq[Entity]){
    val odds = 0.3
    val currentLevel = this

    def entities: Seq[Entity] = {
        _entities.filter(_.stillHere)
    }

    def enemies = entities.collect { case e: Enemy => e }

    def bullets = entities.collect { case b: Bullet => b }

    def players = entities.collect { case p: Player => p }

    def +=(e: Entity): Unit = _entities +:= e

    def updateAll(delay:Double): Unit = {
        for(i <- 0 until _entities.length){
            _entities(i).update(delay)
        }
    }

}