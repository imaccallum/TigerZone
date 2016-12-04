package game.messaging.response;

import game.LocationAndOrientation;

import java.awt.*;
import java.util.List;

public class ValidMovesResponse {
    public final List<LocationAndOrientation> locationsAndOrientations;
    public final List<Point> tigersPlacedLocations;
    public final boolean tileIsUnplaceable;

    public ValidMovesResponse(List<LocationAndOrientation> locationAndOrientations,
                              List<Point> tigersPlacedLocations, boolean tileIsUnplaceable) {
        this.locationsAndOrientations = locationAndOrientations;
        this.tigersPlacedLocations = tigersPlacedLocations;
        this.tileIsUnplaceable = tileIsUnplaceable;
    }
}
