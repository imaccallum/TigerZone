#What we know from Dave so far

Monday 10/31
- There will be a limited total amount of time to play the entire game. Out of time => forfeit
- The competition will be run on a serverExample [networking via TCP sockets]
- The AI will be playing two games at once: one where it goes first, the other where it goes second

Wednesday 11/2
- System should test that every move it makes is valid
- Definitely would want to know # of each tile left
- Tip: if finishing an unclaimed road/city, put a meeple on it when placing the tile (even if it's only 2 points, meeple comes back anyways)

Tuesday 11/8
- Start tile will be placed, then remaining tiles shuffled
- AI will know the entire order of the shuffled pile. It will know exactly which tiles are coming up.

Wednesday 11/9
What we'll know from the serverExample
- Your turn to place tile and what the tile is
- Your move must be received in <1 second
- Move will include where the tile goes, orientation, and, if meeple placed, where
- Move will be confirmed by the serverExample (message of move sent to both players)
	- valid
	- invalid: illegal tile placement => forfeit
	- invalid: illegal meeple placement => forfeit
	- invalid: timeout => forfeit
- If move caused a scoring event:
	- Score each player earned (sent to both players)
	- Number of meeples each player gets back (sent to both players)
- If end of game:
	- for each unscored feature, report score
	- report winner and final score
- jungles == farms
- lakes == cities
- tigers == meeples
- den == monastery
- 77 square tiles (includes starting tile) (38 tiles per player)