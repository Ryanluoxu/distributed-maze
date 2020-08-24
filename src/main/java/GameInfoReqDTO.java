public class GameInfoReqDTO {
    private String playerId;
    private GameRemote gameRemoteObj;

    public String getPlayerId() {
        return playerId;
    }
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
    public GameRemote getGameRemoteObj() {
        return gameRemoteObj;
    }
    public void setGameRemoteObj(GameRemote gameRemoteObj) {
        this.gameRemoteObj = gameRemoteObj;
    }

    @Override
    public String toString() {
        return "GameInfoReqDTO{" +
                "playerId='" + playerId + '\'' +
                ", gameRemoteObj=" + gameRemoteObj +
                '}';
    }
}
