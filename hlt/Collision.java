/*Team PriorityQueens
Badeanu Madalina
Dinca Oana Maria
Papacica Elena-Daniela*/

package hlt;

import java.util.*;

public class Collision {
    /**
     * Test whether a given line segment intersects a circular area.
     *
     * @param start  The start of the segment.
     * @param end    The end of the segment.
     * @param circle The circle to test against.
     * @param fudge  An additional safety zone to leave when looking for collisions. Probably set it to ship radius.
     * @return true if the segment intersects, false otherwise
     */
    public static boolean segmentCircleIntersect(final Position start, final Position end, final Entity circle, final double fudge) {
        // Parameterize the segment as start + t * (end - start),
        // and substitute into the equation of a circle
        // Solve for t
        final double circleRadius = circle.getRadius();
        final double startX = start.getXPos();
        final double startY = start.getYPos();
        final double endX = end.getXPos();
        final double endY = end.getYPos();
        final double centerX = circle.getXPos();
        final double centerY = circle.getYPos();
        final double dx = endX - startX;
        final double dy = endY - startY;

        final double a = square(dx) + square(dy);

        final double b = -2 * (square(startX) - (startX * endX)
                            - (startX * centerX) + (endX * centerX)
                            + square(startY) - (startY * endY)
                            - (startY * centerY) + (endY * centerY));

        if (a == 0.0) {
            // Start and end are the same point
            return start.getDistanceTo(circle) <= circleRadius + fudge;
        }

        // Time along segment when closest to the circle (vertex of the quadratic)
        final double t = Math.min(-b / (2 * a), 1.0);
        if (t < 0) {
            return false;
        }

        final double closestX = startX + dx * t;
        final double closestY = startY + dy * t;
        final double closestDistance = new Position(closestX, closestY).getDistanceTo(circle);

        return closestDistance <= circleRadius + fudge;
    }

    public static double square(final double num) {
        return num * num;
    }

    public static boolean isCollision (Move move1, Move move2){

            //verificam daca doua nave nu sunt prea apropiate 
            // pentru a incerca sa evitam o coliziune

            double x1 = move1.getShip().getXPos();
            double y1 = move1.getShip().getYPos();

            double x2 = move2.getShip().getXPos();
            double y2 = move2.getShip().getYPos();

            final double dx1 = x2 - x1;
            final double dy1 = y2 - y1;
           
            final double dist1 = Math.sqrt(Math.pow(dx1, 2) + Math.pow(dy1, 2));

            /* daca este cazul de coliziune,
            modificam traiectoria uneia cu 90 de grade (in navigateShipTowordsTarget)*/
            if(dist1 <= Constants.MAX_SPEED) {
                return true;
            }

        return false;

    }

    public static boolean isAllCollision (ArrayList<Move> moves, Move m){

        /* verificam daca miscarea m poate produce coliziune
        cu oricare din celelalte miscari*/
     
        for(Move move : moves){
                if(isCollision(move, m)){
                    return true;
                }
            }

        return false;
        
    }

}
