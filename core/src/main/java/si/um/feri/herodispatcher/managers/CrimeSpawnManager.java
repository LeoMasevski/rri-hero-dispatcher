package si.um.feri.herodispatcher.managers;

import static com.badlogic.gdx.math.MathUtils.random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import si.um.feri.herodispatcher.world.dynamic_objects.Crime;
import si.um.feri.herodispatcher.world.static_objects.CrimeDefinition;
import si.um.feri.herodispatcher.world.static_objects.CrimeLocation;

public class CrimeSpawnManager {
    private static final int MAX_ACTIVE_CRIMES = 4;

    private final Map<String, CrimeDefinition> crimeDefinitions;
    private final Array<CrimeLocation> crimeLocations;

    private final Array<Crime> activeCrimes = new Array<>(); // updates every frame and queried by UI later on

    private float spawnTimer = 0f;
    private float nextSpawnTime = 10f; // can be randomized

    private int nextCrimeId = 1;

    public CrimeSpawnManager(Map<String, CrimeDefinition> crimeDefinitions,
                            Array<CrimeLocation> crimeLocations) {
        this.crimeDefinitions = crimeDefinitions;
        this.crimeLocations = crimeLocations;
    }

    public void update(float delta) {
        // remove inactive crimes
        for (int i = activeCrimes.size - 1; i >= 0; i--) {
            Crime crime = activeCrimes.get(i);
            crime.update(delta);

            if (activeCrimes.get(i).isResolved() || activeCrimes.get(i).isFailed()) {
                activeCrimes.removeIndex(i);
            }
        }

        spawnTimer += delta;
        if (spawnTimer >= nextSpawnTime) {
            trySpawnCrime();
            spawnTimer = 0f;
        }
    }

    private void trySpawnCrime() {

        if (activeCrimes.size >= MAX_ACTIVE_CRIMES) {
            return;
        }

        Array<CrimeLocation> availableLocations = new Array<>();
        Set<String> occupiedLocationsIds = getOccupiedLocationIds();

        for (CrimeLocation crimeLocation : crimeLocations) {
            if (!occupiedLocationsIds.contains(crimeLocation.getId())) {
                availableLocations.add(crimeLocation);
            }
        }

        if (availableLocations.isEmpty()) {
            return;
        }

        CrimeLocation chosenLocation = availableLocations.get(random.nextInt(availableLocations.size));

        Array<String> possibleCrimeIds = chosenLocation.getCrimeTypes(); // each crime has an ID which corresponds to crimeTypes for each location
        if (possibleCrimeIds.isEmpty()) {
            return;
        }

        String crimeId = possibleCrimeIds.get(random.nextInt(possibleCrimeIds.size));

        CrimeDefinition crimeDefinition = crimeDefinitions.get(crimeId);
        if (crimeDefinition == null) {
            return;
        }

        Crime crime = new Crime(nextCrimeId++, crimeDefinition, chosenLocation);
        activeCrimes.add(crime);

        Gdx.app.log("CrimeSpawn", "Spawned crime: " + crime.getDefinition().getName() +
            " at " + crime.getLocation().getName());
    }

    private Set<String> getOccupiedLocationIds() {
        Set<String> occupiedLocationIds = new HashSet<>();
        for (Crime crime : activeCrimes) {
            occupiedLocationIds.add(crime.getLocation().getId());
        }
        return occupiedLocationIds;
    }

    public Array<Crime> getActiveCrimes() {
        return  activeCrimes;
    }
}
