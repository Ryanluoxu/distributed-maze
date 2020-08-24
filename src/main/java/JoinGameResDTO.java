import java.util.List;

public class JoinGameResDTO {
    private List<PlayerVO> existingPlayers;
    private Integer N;  // grid size
    private Integer K;  // number of treasures
    private boolean isSuccess;  // if join game succeeds
    private String failReason;  // reason that fail to join

    public JoinGameResDTO(List<PlayerVO> existingPlayers, Integer n, Integer k, boolean isSuccess, String failReason) {
        this.existingPlayers = existingPlayers;
        N = n;
        K = k;
        this.isSuccess = isSuccess;
        this.failReason = failReason;
    }

    public List<PlayerVO> getExistingPlayers() {
        return existingPlayers;
    }

    public void setExistingPlayers(List<PlayerVO> existingPlayers) {
        this.existingPlayers = existingPlayers;
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

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    @Override
    public String toString() {
        return "JoinGameResDTO{" +
                "existingPlayers=" + existingPlayers +
                ", N=" + N +
                ", K=" + K +
                ", isSuccess=" + isSuccess +
                ", failReason='" + failReason + '\'' +
                '}';
    }
}
