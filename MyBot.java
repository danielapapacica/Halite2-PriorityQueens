
/*Team PriorityQueens
Badeanu Madalina
Dinca Oana Maria
Papacica Elena-Daniela*/

import hlt.*;

import java.util.*;

public class MyBot {

    public static void main(final String[] args) {
        final Networking networking = new Networking();
        final GameMap gameMap = networking.initialize("PriorityQueens");

        // We now have 1 full minute to analyse the initial map.
        final String initialMapIntelligence =
                "width: " + gameMap.getWidth() +
                "; height: " + gameMap.getHeight() +
                "; players: " + gameMap.getAllPlayers().size() +
                "; planets: " + gameMap.getAllPlanets().size();
        Log.log(initialMapIntelligence);

        final ArrayList<Move> moveList = new ArrayList<>();

        final ArrayList<Planet>  planetList = new ArrayList<>();
        for (;;) {

            moveList.clear();
            planetList.clear();
            networking.updateMap(gameMap);


            for (final Ship ship : gameMap.getMyPlayer().getShips().values()) {
                if (ship.getDockingStatus() != Ship.DockingStatus.Undocked) {
                    continue;
                }

                for (final Planet planet : gameMap.nearbyPlanetsByDistance(ship).values()) {

                    /*Nu ne indreptam catre propriile planete care sunt pline*/
                    if (planet.isFull() && planet.getOwner() == gameMap.getMyPlayerId() ) {
                        continue;
                    }

                    int taken = 0;

                    /*verificam daca spre planeta indreptam deja n nave, 
                    unde n este numarul de nave pe care il poate accepta*/

                    for(Planet p : planetList)
                        if( p == planet){
                            taken ++;
                            break;
                        }

                    
                    if(taken == planet.getDockingSpots())
                       continue;


                    Entity target = planet;
                    List<Integer> dockedShips = null;
                    if (ship.canDock(planet) ) {
                        //atac - daca planeta e ocupata de inamic
                        // mergem unde e docata nava inamica pentru a ataca

                        if(planet.isOwned() && planet.getOwner() != gameMap.getMyPlayerId()) {
                            dockedShips = planet.getDockedShips();
                            for(Integer IDTarget : dockedShips)             
                                target = gameMap.getShip(planet.getOwner(), IDTarget);      
                        } else {

                            //aparare - daca sunt iamici in apropiere,
                            //nava curenta nu va dock-a
                            boolean okDock = true;

                            Map<Double, Entity> entityByDistance = gameMap.nearbyEntitiesByDistance(ship);  
                            
                            for (double entityKey : entityByDistance.keySet()) {
                                if(entityKey > (Constants.MAX_SPEED * Constants.DOCK_TURNS) - 10) 
                                    break;  
                                
                                if(entityKey <= (Constants.MAX_SPEED * Constants.DOCK_TURNS) - 10 
                                    && entityByDistance.get(entityKey) instanceof Ship 
                                    && entityByDistance.get(entityKey).getOwner() != gameMap.getMyPlayerId() ){
                                    okDock = false;

                                    // noua tinta: o nava inamica
                                    target = entityByDistance.get(entityKey);
                                    break;
                                }
                            }

                            // daca nu sunt inamici in preajma, putem dock-a
                            if(okDock || target instanceof Planet) {
                                DockMove d = new DockMove(ship, planet);
                                moveList.add(d);
                                planetList.add(planet);
                                break;
                            }
                        }
    
                    }


                    final ThrustMove newThrustMove = Navigation.navigateShipToDock(gameMap, ship, target, Constants.MAX_SPEED, moveList);
                    if (newThrustMove != null) {


                        //daca exista coliziuni cu noua incercare
                        //de virare a navei, anulam miscare
                        if (Collision.isAllCollision(moveList, newThrustMove ) == true) {
                            moveList.add(new Move(Move.MoveType.Noop, ship));

                        } else {

                            moveList.add(newThrustMove);
                            planetList.add(planet); //adaugam planeta aleasa in lista
                        }
                    }

                    break;
                }
            }
            Networking.sendMoves(moveList);
        }
    }
}
