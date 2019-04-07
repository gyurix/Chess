package barnes.chess.db.stats;

import barnes.chess.db.entity.UserProfile;

import java.util.List;

public class UserElement {
    private UserType user;
    private Object value;

    public UserElement(UserType user, int userId, List<UserProfile> users) {
        String sValue = user.nick(userId, users);
        this.user = user;
        value = sValue;
        if (sValue == sValue)
            value = sValue;
    }
}
