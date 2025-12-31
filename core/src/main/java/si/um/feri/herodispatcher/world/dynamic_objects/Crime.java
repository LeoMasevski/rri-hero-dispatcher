package si.um.feri.herodispatcher.world.dynamic_objects;

import si.um.feri.herodispatcher.world.static_objects.CrimeDefinition;
import si.um.feri.herodispatcher.world.static_objects.CrimeLocation;
import si.um.feri.herodispatcher.world.static_objects.CrimeState;

public class Crime {

    private final int runtimeId; // helpful for logs and debugging;

    private final CrimeDefinition crimeDefinition;
    private final CrimeLocation crimeLocation;

    private float crimeDuration;
    private CrimeState crimeState; // crimeState is not final as it is changing as time passes

    public Crime(int runtimeId, CrimeDefinition crimeDefinition, CrimeLocation crimeLocation) {
        this.runtimeId = runtimeId;
        this.crimeDefinition = crimeDefinition;
        this.crimeLocation = crimeLocation;

        this.crimeDuration = crimeDefinition.getDuration();
        this.crimeState = CrimeState.ACTIVE; // initial state after crime has spawned
    }

    // ---------- Update ----------
    public void update(float delta) {
        if (crimeState != CrimeState.ACTIVE) return;

        crimeDuration -= delta;

        if (crimeDuration <= 0f) {
            crimeDuration = 0;
            crimeState = CrimeState.FAILED;
        }
    }

    // ---------- State control ----------
    public void resolve() {
        if (crimeState == CrimeState.ACTIVE) {
            crimeState = CrimeState.RESOLVED;
        }
    }

    // -------- Getters --------

    public int getRuntimeId() {
        return runtimeId;
    }

    public CrimeDefinition getDefinition() {
        return crimeDefinition;
    }

    public CrimeLocation getLocation() {
        return crimeLocation;
    }

    public float getcrimeDuration() {
        return crimeDuration;
    }

    public CrimeState getState() {
        return crimeState;
    }

    public boolean isActive() {
        return crimeState == CrimeState.ACTIVE;
    }

    public boolean isFailed() { return crimeState == CrimeState.FAILED; }

    public boolean isResolved() { return crimeState == CrimeState.RESOLVED; }

}
