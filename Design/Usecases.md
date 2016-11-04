##Use Cases

#Game Player (AI)
- Can place tiles where there are possible connections
- Can rotate tiles if there are multiple possible connections
- Can place follower on tile just placed
- Can place follower only on territory not already taken by another player
- Can only place one follower on a turn
- Can see possible placements on game board
- Can view remaining tiles (count and type)
- Can place knight/thief/farmer/monk in respective city segment/road segment/field segment/monastary
- Can collect score for valid finished segments
- Can view current user and opponent score
- Can recollect follower on completed segment
- Can view number of followers left
- Can cancel tile placement and follower placement
- Can specify location of follower on tile
- Can only move on turn

#Game Manager
- Draw tile with no legal placement is discarded 
- Randomizes tile distribution at beginning
- Give player points for completed segment
- At end of game, give player points for uncompleted segments and farmers
- Calculate points before distribution
- Keep track of segments/areas
- Prevent users from putting follower on unavailable area on tile
- Recognize when a segment is completed
- Be able to distribute points between multiple players based on count of followers in shared territory
- Control game life cycle
- Differentiate scoring systems for tiles
- Control turn cycle