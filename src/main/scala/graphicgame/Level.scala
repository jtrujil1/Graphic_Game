package graphicgame

class Level (val maze: Maze, private var _entities:Seq[Entity]){
    val odds = 0.3
    val currentLevel = this

    def entities: Seq[Entity] = {
        _entities.filter(stillHere)
    }

    def +=(e: Entity): Unit = _entities +:= e

    def updateAll(delay:Double): Unit = {
        for(i <- 0 until _entities.length){
            _entities(i).update(delay)
        }
    }

}