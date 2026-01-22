package si.um.feri.herodispatcher.world.static_objects;

public class CrimeDefinition {

    private String id;
    private String name;
    private String description;
    private String category;
    private float duration;
    private String requiredHeroAttribute;

    public CrimeDefinition() {
    }

    public CrimeDefinition(String id,
                           String name,
                           String description,
                           String category,
                           float duration,
                           String requiredHeroAttribute) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.duration = duration;
        this.requiredHeroAttribute = requiredHeroAttribute;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public float getDuration() {
        return duration;
    }

    public String getRequiredHeroAttribute() {
        return requiredHeroAttribute;
    }
}
