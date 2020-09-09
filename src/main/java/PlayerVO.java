public class PlayerVO {
    private String ip;
    private Integer port;
    private GameRemote gameRemoteObj;
    private String playerId;
    private int score;

    public PlayerVO(GameInfoReqDTO request) {
        this.ip = request.getIp();
        this.port = request.getPort();
        this.gameRemoteObj = request.getGameRemoteObj();
        this.playerId = request.getPlayerId();
    }

    public PlayerVO(String playerId, Integer score){
        this.playerId=playerId;
        this.score=score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public GameRemote getGameRemoteObj() {
        return gameRemoteObj;
    }

    public void setGameRemoteObj(GameRemote gameRemoteObj) {
        this.gameRemoteObj = gameRemoteObj;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return "PlayerVO{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", gameRemoteObj=" + gameRemoteObj +
                ", playerId='" + playerId + '\'' +
                ", score=" + score +
                '}';
    }
}
