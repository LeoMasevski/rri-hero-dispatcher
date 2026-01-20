package si.um.feri.herodispatcher.world.static_objects;

import com.badlogic.gdx.math.Vector2;
import si.um.feri.herodispatcher.world.static_objects.PathNodeType;

import java.util.Map;

public class PathGraph {

    private final Map<String, PathNode> nodesById;

    public PathGraph(Map<String, PathNode> nodesById) {
        this.nodesById = nodesById;
    }

    public PathNode getNode(String id) {
        return nodesById.get(id);
    }

    public Map<String, PathNode> getNodes() {
        return nodesById;
    }

    /**
     * Finds the closest node to a world position.
     * Used to snap heroes / crimes onto the graph.
     */
    public PathNode findClosestNode(Vector2 position) {
        PathNode closest = null;
        float bestDistSq = Float.MAX_VALUE;

        for (PathNode node : nodesById.values()) {
            float distSq = node.getPosition().dst2(position);
            if (distSq < bestDistSq) {
                bestDistSq = distSq;
                closest = node;
            }
        }

        return closest;
    }

    public PathNode findClosestNode(Vector2 position, PathNodeType preferredType, float preferredMaxDist) {
        PathNode closestPreferred = null;
        float bestPreferredDistSq = preferredMaxDist * preferredMaxDist;

        for (PathNode node : nodesById.values()) {
            if (preferredType != null && node.getType() == preferredType) {
                float d2 = node.getPosition().dst2(position);
                if (d2 < bestPreferredDistSq) {
                    bestPreferredDistSq = d2;
                    closestPreferred = node;
                }
            }
        }

        if (closestPreferred != null) return closestPreferred;

        // fallback: closest of any type
        return findClosestNode(position);
    }

}
