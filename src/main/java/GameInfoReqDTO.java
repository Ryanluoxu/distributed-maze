public class GameInfoReqDTO {
    private String ip;
    private Integer port;
    private GameRemote gameRemoteObj;
    private String playerId;

    public GameInfoReqDTO(String ip, Integer port, GameRemote gameRemoteObj, String playerId) {
        this.ip = ip;
        this.port = port;
        this.gameRemoteObj = gameRemoteObj;
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }

    public GameRemote getGameRemoteObj() {
        return gameRemoteObj;
    }

    @Override
    public String toString() {
        return "GameInfoReqDTO{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", gameRemoteObj=" + gameRemoteObj +
                ", playerId='" + playerId + '\'' +
                '}';
    }
}
