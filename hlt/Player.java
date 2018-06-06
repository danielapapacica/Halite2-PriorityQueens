package hlt;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.*;

public class Player {

    private final Map<Integer, Ship> ships;
    private final int id;

    public Player(final int id, Map<Integer, Ship> ships) {
        this.id = id;
        this.ships = Collections.unmodifiableMap(ships);
    }

    public Map<Integer, Ship> getShips() {
        return ships;
    }

    public List<Ship> getListShips(){
    	return new ArrayList<Ship>(getShips().values());
    }

    public Ship getShip(final int entityId) {
        return ships.get(entityId);
    }

    public int getId() {
        return id;
    }
}
