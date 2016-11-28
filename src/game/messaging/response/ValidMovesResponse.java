package game.messaging.response;

import entities.board.Tiger;
import game.LocationAndOrientation;

import java.util.List;
import java.util.Set;

public class ValidMovesResponse {
    public final List<LocationAndOrientation> locationsAndOrientations;
    public final Set<Tiger> tigerPlacedOnBoard;
    public final boolean tileIsUnplaceable;

    public ValidMovesResponse(List<LocationAndOrientation> locationAndOrientations,
                              Set<Tiger> tigerPlacedOnBoard, boolean tileIsUnplaceable) {
        this.locationsAndOrientations = locationAndOrientations;
        this.tigerPlacedOnBoard = tigerPlacedOnBoard;
        this.tileIsUnplaceable = tileIsUnplaceable;
    }
}
