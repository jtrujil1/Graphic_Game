package graphicgame

import graphicgame.ArrayQueue
import collection.mutable

object ShortestPath {

  val offsets = Array((-1, 0), (1, 0), (0, -1), (0, 1))

  def breadthFirstShortestPath(sx: Double, sy: Double, ex: Double, ey: Int): Int = {
    val queue = new ArrayQueue[(Double, Double, Int)]()
    val visited = mutable.Set[(Double, Double)]((sx, sy))
    queue.enqueue((sx, sy, 0))
    while (!queue.isEmpty) {
      val (x, y, steps) = queue.dequeue()
      for ((offsetx, offsety) <- offsets) {
        val nx = x + offsetx
        val ny = y + offsety
        if (nx == ex && ny == ey) return steps + 1
        //if (nx >= 0 && nx < maze.length && ny >= 0 && ny < maze(nx).length && maze.isClear(nx, ny, Enemy.width, Enemy.height, Enemy) && !visited((nx, ny))) {
          queue.enqueue((nx, ny, steps + 1))
          visited += nx -> ny
        //}
      }
    }
    1000000000
  }
}
