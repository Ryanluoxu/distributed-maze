import java.util.HashMap;
public class MoveReqDTO {
    private String playerId;
    private Integer keyboardInput;

    public String getPlayerId() {
        return playerId;
    }

    public Integer getKeyboardInput() {
        return keyboardInput;
    }

    public String toString() {
        HashMap<Integer, String> moveDir = new HashMap<Integer, String>();
        moveDir.put(0,"Unmoved");
        moveDir.put(1,"West");
        moveDir.put(2,"South");
        moveDir.put(3,"East");
        moveDir.put(4,"North");
        moveDir.put(9,"Quit");

        return "MoveReqDTO{" +
                "playerId='" + playerId +
                ", move=" + moveDir.get(keyboardInput) +
                '}';
    }
}
