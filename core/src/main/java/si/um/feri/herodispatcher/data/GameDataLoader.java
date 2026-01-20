package si.um.feri.herodispatcher.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;

import si.um.feri.herodispatcher.data.dto.CrimeDefinitionsData;
import si.um.feri.herodispatcher.data.dto.CrimeLocationsData;
import si.um.feri.herodispatcher.data.dto.PathNodeData;
import si.um.feri.herodispatcher.world.static_objects.CrimeDefinition;
import si.um.feri.herodispatcher.world.static_objects.CrimeLocation;
import si.um.feri.herodispatcher.data.dto.PathGraphData;
import si.um.feri.herodispatcher.world.static_objects.PathNode;
import si.um.feri.herodispatcher.world.static_objects.PathNodeType;

public class GameDataLoader {

    private static final String CRIMES_FILE = "data/crime_definitions.json";
    private static final String CRIME_LOCATIONS_FILE = "data/crime_locations.json";
    private static final String PATH_GRAPH_FILE = "data/path_graph.json";

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

    public Array<PathNode> loadPathGraph() {
        FileHandle file = Gdx.files.internal(PATH_GRAPH_FILE);
        PathGraphData data = json.fromJson(PathGraphData.class, file);

        // TODO: change conversion
        float mapHeight = 4755f;

        // --- Step 1: create nodes (no neighbors yet) ---
        ObjectMap<String, PathNode> nodeMap = new ObjectMap<>();

        for (PathNodeData nodeData : data.nodes) {
            float convertedY = mapHeight - nodeData.y;

            PathNode node = new PathNode(
                nodeData.id,
                PathNodeType.valueOf(nodeData.type.toUpperCase()),
                nodeData.x,
                convertedY
            );

            nodeMap.put(node.getId(), node);
        }

        // --- Step 2: wire neighbors ---
        for (PathNodeData nodeData : data.nodes) {
            PathNode node = nodeMap.get(nodeData.id);

            for (String neighborId : nodeData.connections) {
                PathNode neighbor = nodeMap.get(neighborId);
                if (neighbor != null) {
                    node.addNeighbor(neighbor);
                    neighbor.addNeighbor(node); // <-- IMPORTANT: add reverse connection too
                }
            }
        }


        // --- Step 3: return nodes as Array ---
        return new Array<>(nodeMap.values().toArray());
    }

}
