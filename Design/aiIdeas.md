## Ideas for the AI

# First two turns of game
- Try to place a Tiger on an region that's unclaimed, since it's very likely that they will expand (worth many points in end) or close quickly if it's a lake.

# Placing Tigers
- When placing a tile, iterate over all the possibilities, if by placing the tile on some of them, 
 can complete a Region, find the largest one that meets the criteria, complete it and place a Tiger on it.
- If there is no possible move that can complete a region, then place the tile on the largest unclaimed Region.

# Crocodiles
- If the Tile drawn contains a crocodile, try to put it in the largest claimed Region or the one with the most unique prey animals.
- If at the end of the game, the player still has Crocodiles, use them on the same as above.

# Prey Animals
- If a Prey Animal Tile is drawn and there are no closing moves, try placing the prey Animal near the largest claimable or owned region
 that has the largest number of unique animals on it. 
- Try not to place a unique Prey Animal Tile near a region that already has that animal. Unless its a Trail, there could be better used to 
add unique Animals to lakes as the score can increase exponentially.

#Jungles
- When iterating over all the possibilities of tile placement, if by placing a tile, it extends a Jungle region that is adjacent to a 
closed Den and it is not claimed, place the Tile on that spot and use a Tiger.

### Notes:
- All of these possible moves can be ranked into a highest reward system when iterating to optimize the best move 
(Or the one that yields the highest score) (I really think this is the best route - Trevor)
- Counters could be kept to hold the reward system, and once we are done iterating. Retrieve the x and Y coordinates for the best move.
- Some of the possible moves can be a affected by:
    - Number of Tigers left
    - Number of Crocodiles
    - Tiles left in the game
