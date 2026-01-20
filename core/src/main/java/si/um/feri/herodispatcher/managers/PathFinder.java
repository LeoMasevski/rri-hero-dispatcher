package si.um.feri.herodispatcher.managers;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import si.um.feri.herodispatcher.world.static_objects.PathNode;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class PathFinder {

    /**
     * A* pathfinding over the PathNode graph.
     *
     * Notes:
     * - Uses Euclidean distance for both edge cost and heuristic.
     * - PriorityQueue has no decrease-key, so we push new records for improved paths
     *   and skip stale records when they pop.
     */
    public Array<PathNode> findPath(PathNode start, PathNode goal) {
        if (start == null || goal == null) return new Array<>();

        Map<PathNode, PathNode> cameFrom = new HashMap<>();
        Map<PathNode, Float> gScore = new HashMap<>();

        PriorityQueue<NodeRecord> openSet =
            new PriorityQueue<>((a, b) -> Float.compare(a.f, b.f));

        gScore.put(start, 0f);
        openSet.add(new NodeRecord(start, 0f, heuristic(start, goal)));

        ObjectSet<PathNode> closedSet = new ObjectSet<>();

        while (!openSet.isEmpty()) {
            NodeRecord record = openSet.poll();
            PathNode current = record.node;

            // Skip stale/outdated queue entries
            Float bestKnownG = gScore.get(current);
            if (bestKnownG == null || record.g != bestKnownG) continue;

            if (current == goal) return reconstructPath(cameFrom, current);

            closedSet.add(current);

            for (PathNode neighbor : current.getNeighbors()) {
                if (closedSet.contains(neighbor)) continue;

                float tentativeG = gScore.get(current) + edgeCost(current, neighbor);

                Float neighborG = gScore.get(neighbor);
                if (neighborG == null || tentativeG < neighborG) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeG);

                    float f = tentativeG + heuristic(neighbor, goal);
                    openSet.add(new NodeRecord(neighbor, tentativeG, f));
                }
            }
        }

        return new Array<>(); // no path found
    }

    private float edgeCost(PathNode a, PathNode b) {
        return a.getPosition().dst(b.getPosition());
    }

    private float heuristic(PathNode from, PathNode goal) {
        return from.getPosition().dst(goal.getPosition());
    }

    private static class NodeRecord {
        final PathNode node;
        final float g;
        final float f;

        NodeRecord(PathNode node, float g, float f) {
            this.node = node;
            this.g = g;
            this.f = f;
        }
    }

    private Array<PathNode> reconstructPath(Map<PathNode, PathNode> cameFrom, PathNode current) {
        Array<PathNode> path = new Array<>();
        path.add(current);

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.insert(0, current);
        }

        return path;
    }
}
