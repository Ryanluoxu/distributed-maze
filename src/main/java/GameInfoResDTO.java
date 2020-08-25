import java.util.List;

public class GameInfoResDTO {
    private Integer N;
    private Integer K;
    private List<PlayerVO> players;
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

    public List<PlayerVO> getPlayers() {
        return players;
    }

    public GameInfoResDTO(Integer n, Integer k, List<PlayerVO> players, boolean isValidPlayerId) {
        N = n;
        K = k;
        this.players = players;
        this.isValidPlayerId = isValidPlayerId;
    }

    @Override
    public String toString() {
        return "GameInfoResDTO{" +
                "N=" + N +
                ", K=" + K +
                ", players=" + players +
                ", isValidPlayerId=" + isValidPlayerId +
                '}';
    }
}
