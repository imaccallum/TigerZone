package game.messaging;

import game.messaging.info.PlayerInfo;
import game.messaging.info.RegionInfo;

import java.util.List;

public class GameStatusMessage {
    /**
     * The list containing info about all of the open regions
     */
    public final List<RegionInfo> openRegionsInfo;

    /**
     * The list containing fino about all of the players
     */
    public final List<PlayerInfo> playersInfo;

    /**
     * Construct a game status message that is sent to all players at the end of every turn, all properties are final
     * to protect game data
     *
     * @param openRegionsInfo,
     * The information list about the open regions in the game
     *
     * @param playersInfo,
     * The information list about the players in the game
     */
    public GameStatusMessage(List<RegionInfo> openRegionsInfo, List<PlayerInfo> playersInfo) {
        this.openRegionsInfo = openRegionsInfo;
        this.playersInfo = playersInfo;
    }

}
