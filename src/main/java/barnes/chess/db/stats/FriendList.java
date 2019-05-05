package barnes.chess.db.stats;

public class FriendList {

    private final Integer userId;
    private final Integer friendId;
    private final String friendName;

    public FriendList(){
        this(0, 0, null);
    }

    public FriendList(String friendName) {
        this.userId = 0;
        this.friendId = 0;
        this.friendName = friendName;
    }

    public FriendList(Integer userId, Integer friendId, String friendName){
        this.userId = userId;
        this.friendId = friendId;
        this.friendName = friendName;
    }

    public Integer getUserId(){
        return userId;
    }
    public Integer getFriendId(){
        return friendId;
    }
    public String getFriendName() {
        return friendName;
    }
}
