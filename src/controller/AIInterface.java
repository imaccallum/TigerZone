package controller;

import wrappers.BeginTurnWrapper;

public interface AIInterface {
    Move decideMove(BeginTurnWrapper beginMove);
    String getPlayerName();
}
