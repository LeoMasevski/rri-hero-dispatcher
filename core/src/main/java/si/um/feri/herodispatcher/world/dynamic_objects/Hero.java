package si.um.feri.herodispatcher.world.dynamic_objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import si.um.feri.herodispatcher.world.static_objects.PathNode;
import si.um.feri.herodispatcher.world.dynamic_objects.Crime;

public class Hero {
    private Crime assignedCrime;
    private boolean arrivedAtCrime = false;

    private final String name;
    private final String type;
    private final int age;
    private final String weapon;
    private final String infoFacts;
    private final String heroId;

    private final int strength;
    private final int intelligence;
    private final int agility;

    private final Vector2 position;
    private final float speed = 200f;

    private Array<PathNode> currentPath = new Array<>();
    private int currentPathIndex = 0;

    private Vector2 finalTarget = null;

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

    public void update(float delta) {

        // 1) Follow graph nodes
        if (hasPath()) {
            PathNode targetNode = currentPath.get(currentPathIndex);
            moveTowards(targetNode.getPosition(), delta);

            if (position.epsilonEquals(targetNode.getPosition(), 1.0f)) {
                position.set(targetNode.getPosition());
                currentPathIndex++;
            }
            return;
        }

        if (assignedCrime != null && assignedCrime.isActive()) {
            arrivedAtCrime = true;
            return;
        }

        if (finalTarget != null) {
            moveTowards(finalTarget, delta);

            if (position.epsilonEquals(finalTarget, 1.0f)) {
                position.set(finalTarget);
                finalTarget = null;
            }
        }
    }

    public void setPath(Array<PathNode> path) {
        setPath(path, null);
    }

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

    private void moveTowards(Vector2 target, float delta) {
        if (target == null) return;

        float dist = position.dst(target);
        if (dist < 0.0001f) return;

        float maxMove = speed * delta;

        if (maxMove >= dist) {
            position.set(target);
            return;
        }

        float dx = target.x - position.x;
        float dy = target.y - position.y;
        float invLen = 1f / (float) Math.sqrt(dx * dx + dy * dy);

        position.x += dx * invLen * maxMove;
        position.y += dy * invLen * maxMove;
    }

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

    public boolean hasArrivedAtCrime() {
        return arrivedAtCrime;
    }

    public void resetArrivedAtCrime() {
        arrivedAtCrime = false;
    }

    public Crime getAssignedCrime() {
        return assignedCrime;
    }

    public void assignCrime(Crime crime) {
        this.assignedCrime = crime;
    }
}
