package si.um.feri.herodispatcher.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import si.um.feri.herodispatcher.data.dto.CrimeDefinitionsData;
import si.um.feri.herodispatcher.data.dto.CrimeLocationsData;
import si.um.feri.herodispatcher.world.static_objects.CrimeDefinition;
import si.um.feri.herodispatcher.world.static_objects.CrimeLocation;

public class GameDataLoader {

    private static final String CRIMES_FILE = "data/crime_definitions.json";
    private static final String CRIME_LOCATIONS_FILE = "data/crime_locations.json";

    private final Json json;

    public GameDataLoader() {
        json = new Json();
    }

    public Array<CrimeDefinition> loadCrimeDefinitions() {
        FileHandle file = Gdx.files.internal(CRIMES_FILE);
        CrimeDefinitionsData data = json.fromJson(CrimeDefinitionsData.class, file);
        return data.crimeDefinitions;
    }

    public Array<CrimeLocation> loadCrimeLocations() {
        FileHandle file = Gdx.files.internal(CRIME_LOCATIONS_FILE);
        CrimeLocationsData data = json.fromJson(CrimeLocationsData.class, file);

        // TODO: conversion needs more work, as of now it uses hardcoded map height
        float mapHeight = 4755;
        for (int i = 0; i < data.crimeLocations.size; i++) {
            data.crimeLocations.get(i).setY(mapHeight - data.crimeLocations.get(i).getY());
        }
        return data.crimeLocations;
    }
}
