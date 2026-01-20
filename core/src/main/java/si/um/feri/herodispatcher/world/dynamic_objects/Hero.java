package si.um.feri.herodispatcher.world.dynamic_objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import si.um.feri.herodispatcher.world.static_objects.PathNode;

public class Hero {

    // ---------- Identity / Info ----------
    private final String name;
    private final String type;
    private final int age;
    private final String weapon;
    private final String infoFacts;
    private final String heroId; // used for asset lookup (e.g. "spiderman")

    // ---------- Stats (0-5) ----------
    private final int strength;
    private final int intelligence;
    private final int agility;

    // ---------- Movement / Pathfinding ----------
    private final Vector2 position;               // hero's current world position
    private final float speed = 200f;             // pixels per second

    private Array<PathNode> currentPath = new Array<>();
    private int currentPathIndex = 0;

    // If the target isn't exactly on a node (crime circle), we finish with a final move here
    private Vector2 finalTarget = null;

    // ---------- Constructors ----------
    public Hero(String name, String type, int age, String weapon, String infoFacts,
                int strength, int intelligence, int agility, String heroId) {
        this(name, type, age, weapon, infoFacts, strength, intelligence, agility, heroId, 0f, 0f);
    }

    public Hero(String name, String type, int age, String weapon, String infoFacts,
                int strength, int intelligence, int agility, String heroId,
                float startX, float startY) {
        this.name = name;
        this.type = type;
        this.age = age;
        this.weapon = weapon;
        this.infoFacts = infoFacts;

        this.strength = strength;
        this.intelligence = intelligence;
        this.agility = agility;

        this.heroId = heroId;
        this.position = new Vector2(startX, startY);
    }

    // ---------- Update ----------
    /** Call every frame to move the hero along the path / to the final target. */
    public void update(float delta) {
        // 1) Follow graph nodes
        if (hasPath()) {
            PathNode targetNode = currentPath.get(currentPathIndex);
            moveTowards(targetNode.getPosition(), delta);

            // Once we reach the node, advance to the next node
            if (position.epsilonEquals(targetNode.getPosition(), 0.5f)) {
                position.set(targetNode.getPosition());
                currentPathIndex++;
            }
            return;
        }

        // 2) Finish at exact target (crime circle position, etc.)
        if (finalTarget != null) {
            moveTowards(finalTarget, delta);

            if (position.epsilonEquals(finalTarget, 0.5f)) {
                position.set(finalTarget);
                finalTarget = null;
            }
        }
    }

    // ---------- Path API ----------
    public void setPath(Array<PathNode> path) {
        setPath(path, null);
    }

    /**
     * @param path       Node path to follow (can be empty).
     * @param finalTarget Optional exact destination after the last node (e.g. crime circle).
     */
    public void setPath(Array<PathNode> path, Vector2 finalTarget) {
        this.currentPath = (path == null) ? new Array<>() : path;
        this.currentPathIndex = 0;

        // Only allow finalTarget if we actually have a node-path to follow
        if (this.currentPath.size > 0 && finalTarget != null) {
            this.finalTarget = new Vector2(finalTarget);
        } else {
            this.finalTarget = null;
        }
    }

    public void clearPath() {
        this.currentPath.clear();
        this.currentPathIndex = 0;
        this.finalTarget = null;
    }

    public boolean hasPath() {
        return currentPath != null && currentPathIndex < currentPath.size;
    }

    public Array<PathNode> getCurrentPath() {
        return currentPath;
    }

    public int getCurrentPathIndex() {
        return currentPathIndex;
    }

    public Vector2 getFinalTarget() {
        return finalTarget;
    }

    // ---------- Movement helpers ----------
    private void moveTowards(Vector2 target, float delta) {
        if (target == null) return;

        float dist = position.dst(target);
        if (dist < 0.0001f) return;

        float maxMove = speed * delta;

        // Don't overshoot
        if (maxMove >= dist) {
            position.set(target);
            return;
        }

        // Move in direction of target
        float dx = target.x - position.x;
        float dy = target.y - position.y;
        float invLen = 1f / (float) Math.sqrt(dx * dx + dy * dy);

        position.x += dx * invLen * maxMove;
        position.y += dy * invLen * maxMove;
    }

    // ---------- Getters ----------
    public String getName() { return name; }
    public String getType() { return type; }
    public int getAge() { return age; }
    public String getWeapon() { return weapon; }
    public String getInfoFacts() { return infoFacts; }

    public int getStrength() { return strength; }
    public int getIntelligence() { return intelligence; }
    public int getAgility() { return agility; }

    public String getHeroId() { return heroId; }
    public Vector2 getPosition() { return position; }
    public float getSpeed() { return speed; }

    public String getImagePath() {
        return "images/heroes/" + heroId + ".jpg";
    }
}
