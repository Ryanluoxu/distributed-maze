public class GameInfoResDTO {
    private Integer N;  // grid size
    private Integer K;  // number of treasures
    private GameRemote serverRemoteObj;

    public GameInfoResDTO(Integer n, Integer k, GameRemote serverRemoteObj) {
        N = n;
        K = k;
        this.serverRemoteObj = serverRemoteObj;
    }

    public Integer getN() {
        return N;
    }

    public void setN(Integer n) {
        N = n;
    }

    public Integer getK() {
        return K;
    }

    public void setK(Integer k) {
        K = k;
    }

    public GameRemote getServerRemoteObj() {
        return serverRemoteObj;
    }

    public void setServerRemoteObj(GameRemote serverRemoteObj) {
        this.serverRemoteObj = serverRemoteObj;
    }

    @Override
    public String toString() {
        return "GameInfoResDTO{" +
                "N=" + N +
                ", K=" + K +
                ", serverRemoteObj=" + serverRemoteObj +
                '}';
    }
}
