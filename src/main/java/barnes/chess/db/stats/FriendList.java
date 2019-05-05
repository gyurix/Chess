package barnes.chess.db.stats;

import java.sql.ResultSet;
import java.sql.SQLException;
import barnes.chess.db.entity.Friendship;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class FriendList {

    private final Integer userId;
    private final Integer friendId;
    private final String friendName;

    public FriendList(){
        this(0, 0, null);
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
