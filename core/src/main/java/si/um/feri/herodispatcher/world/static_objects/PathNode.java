package si.um.feri.herodispatcher.world.static_objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import si.um.feri.herodispatcher.world.static_objects.PathNodeType;

public class PathNode {

    private final String id;
    private final PathNodeType type;
    private final Vector2 position;
    private final Array<PathNode> neighbors;

    public PathNode(String id, PathNodeType type, float x, float y) {
        this.id = id;
        this.type = type;
        this.position = new Vector2(x, y);
        this.neighbors = new Array<>();
    }

    // --- Getters ---

    public String getId() {
        return id;
    }

    public PathNodeType getType() {
        return type;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Array<PathNode> getNeighbors() {
        return neighbors;
    }

    // --- Graph wiring ---

    public void addNeighbor(PathNode neighbor) {
        if (!neighbors.contains(neighbor, true)) {
            neighbors.add(neighbor);
        }
    }
}
