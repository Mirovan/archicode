package ru.bigint.model;

import org.antlr.v4.runtime.misc.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class DiagObject {
    private String name;
    private String type;
    private Coord coord;
    private Map<String, Object> properties = new HashMap<>();

    public Coord getCoord() {
        return coord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public DiagObject(String name) {
        this.name = name;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public void addProperties(Queue<Pair<Object, Object>> pairs) {
        Map<String, Object> map = new HashMap<>();
        for (var item: pairs) {

            map.put(item.a.toString(), item.b);

        }
        this.setProperties(map);
        pairs.clear();
    }
}
