package controller;

import wrappers.BeginTurnWrapper;

public interface AINotifier {
    Move decideMove(BeginTurnWrapper beginMove);
}
