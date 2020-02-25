package graphicgame

import graphicgame.ArrayQueue
import collection.mutable

object ShortestPath {

  val offsets = Array((-1, 0), (1, 0), (0, -1), (0, 1))

  def breadthFirstShortestPath(sx: Double, sy: Double, ex: Double, ey: Double, entity: Entity): Int = {
    val entMaze = entity.level.maze
    if(entMaze.isClear(sx, sy, entity.width, entity.height, entity)){
      val queue = new ArrayQueue[(Double, Double, Int)]()
      val visited = mutable.Set[(Double, Double)]((sx, sy))
      queue.enqueue((sx, sy, 0))
      while (!queue.isEmpty) {
        val (x, y, steps) = queue.dequeue()
        for ((offsetx, offsety) <- offsets) {
          val nx = x + offsetx
          val ny = y + offsety
          if (nx >= 0 && nx < entMaze.width && ny >= 0 && ny < entMaze.height &&
              entMaze.isClear(nx, ny, entity.width, entity.height, entity) && !visited((nx, ny))) {
            if ((nx - ex).abs < 1 && (ny - ey).abs < 1) return steps + 1
            queue.enqueue((nx, ny, steps + 1))
            visited += nx -> ny
          }
        }
      }
    }
    1000000000
  }

}
