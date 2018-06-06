/*Team PriorityQueens
Badeanu Madalina
Dinca Oana Maria
Papacica Elena-Daniela*/

package hlt;

import java.util.*;

public class Navigation {

    public static ThrustMove navigateShipToDock(
            final GameMap gameMap,
            final Ship ship,
            final Entity dockTarget,
            final int maxThrust,
            final ArrayList<Move> moveList)//am adugat aici
    {
        final int maxCorrections = Constants.MAX_NAVIGATION_CORRECTIONS;
        final boolean avoidObstacles = true;
        final double angularStepRad = Math.PI/180.0;
        final Position targetPos = ship.getClosestPoint(dockTarget);

        return navigateShipTowardsTarget(gameMap, ship, targetPos, maxThrust, avoidObstacles, maxCorrections, angularStepRad, moveList);
    }

    public static ThrustMove navigateShipTowardsTarget(
            final GameMap gameMap,
            final Ship ship,
            final Position targetPos,
            final int maxThrust,
            final boolean avoidObstacles,
            final int maxCorrections,
            final double angularStepRad,
            final ArrayList<Move> moveList)
    {
        if (maxCorrections <= 0) {
            return null;
        }

         Position newTarget = null;

        final double distance = ship.getDistanceTo(targetPos);
        final double angleRad = ship.orientTowardsInRad(targetPos);

        if (avoidObstacles && !gameMap.objectsBetween(ship, targetPos).isEmpty()) {
            final double newTargetDx = Math.cos(angleRad + angularStepRad) * distance;
            final double newTargetDy = Math.sin(angleRad + angularStepRad) * distance;
            newTarget = new Position(ship.getXPos() + newTargetDx, ship.getYPos() + newTargetDy);



            return navigateShipTowardsTarget(gameMap, ship, newTarget, maxThrust, true, (maxCorrections-1), angularStepRad, moveList);
        }



        final int thrust;
        if (distance < maxThrust) {
            // Do not round up, since overshooting might cause collision.
            thrust = (int) distance;
        }
        else {
            thrust = maxThrust;
        }

        final int angleDeg = Util.angleRadToDegClipped(angleRad);

        ThrustMove newThrustMove = new ThrustMove(ship, angleDeg, thrust);

         if (Collision.isAllCollision(moveList, newThrustMove ) == true) {

            /* in caz de o posibila coliziune,
            modificam traiectoria uneia dintre navele in cauza*/
            
            newThrustMove = new ThrustMove(ship, angleDeg + 90, thrust);

         }


        return newThrustMove;
    }
}
