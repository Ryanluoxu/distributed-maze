import java.util.List;

public class GameInfoResDTO {
    private Integer N;
    private Integer K;
    private List<PlayerVO> playerList;
    private boolean isValidPlayerId;

    public boolean isValidPlayerId() {
        return isValidPlayerId;
    }

    public Integer getN() {
        return N;
    }

    public Integer getK() {
        return K;
    }

    public List<PlayerVO> getPlayerList() {
        return playerList;
    }

    public GameInfoResDTO(Integer n, Integer k, List<PlayerVO> players, boolean isValidPlayerId) {
        N = n;
        K = k;
        this.playerList = players;
        this.isValidPlayerId = isValidPlayerId;
    }

    @Override
    public String toString() {
        return "GameInfoResDTO{" +
                "N=" + N +
                ", K=" + K +
                ", players=" + playerList +
                ", isValidPlayerId=" + isValidPlayerId +
                '}';
    }
}
