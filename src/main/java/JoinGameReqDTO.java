public class JoinGameReqDTO {
    private String playerId;

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return "JoinGameReqDTO{" +
                "playerId='" + playerId + '\'' +
                '}';
    }
}
