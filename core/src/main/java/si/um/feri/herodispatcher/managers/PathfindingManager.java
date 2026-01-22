package si.um.feri.herodispatcher.managers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import si.um.feri.herodispatcher.world.static_objects.PathGraph;
import si.um.feri.herodispatcher.world.static_objects.PathNode;
import si.um.feri.herodispatcher.world.static_objects.PathNodeType;

public class PathfindingManager {

    private final PathGraph graph;
    private final PathFinder pathFinder;

    // how close the click target must be to a LOCATION node to prefer it
    private static final float LOCATION_PREF_RADIUS = 120f;

    public PathfindingManager(PathGraph graph) {
        this.graph = graph;
        this.pathFinder = new PathFinder();
    }

    public PathGraph getGraph() {
        return graph;
    }

    public Array<PathNode> requestPath(Vector2 startPos, Vector2 targetPos) {
        if (startPos == null || targetPos == null) return new Array<>();

        PathNode startNode = graph.findClosestNode(startPos);

        PathNode targetNode = graph.findClosestNode(targetPos, PathNodeType.LOCATION, LOCATION_PREF_RADIUS);

        if (startNode == null || targetNode == null) return new Array<>();

        return pathFinder.findPath(startNode, targetNode);
    }
}
