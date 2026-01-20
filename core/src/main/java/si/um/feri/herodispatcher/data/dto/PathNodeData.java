package si.um.feri.herodispatcher.data.dto;

import com.badlogic.gdx.utils.Array;

public class PathNodeData {
    public String id;
    public String type; // "main", "peripheral", "location"
    public float x;
    public float y;
    public Array<String> connections;
}
