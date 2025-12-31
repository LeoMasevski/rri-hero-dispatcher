package si.um.feri.herodispatcher.world.static_objects;

import com.badlogic.gdx.utils.Array;

public class CrimeLocation {

    private String id;
    private String type;
    private String name;
    private String description;

    // Map/world coordinates
    private float x;
    private float y;

    // crimeTypes are CrimeDefinition IDs
    private Array<String> crimeTypes;

    // Required no-arg constructor for JSON libraries
    public CrimeLocation() {
    }

    public CrimeLocation(String id,
                         String type,
                         String name,
                         String description,
                         float x,
                         float y,
                         Array<String> crimeTypes) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.x = x;
        this.y = y;
        this.crimeTypes = crimeTypes;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Array<String> getCrimeTypes() {
        return crimeTypes;
    }
}
