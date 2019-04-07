package barnes.chess.db.stats;


import barnes.chess.db.entity.UserProfile;

import java.util.List;

import static barnes.chess.utils.FormattingUtils.toCamelCase;

public enum UserType {
    NAME{
        @Override
        public String nick(int userId, List<UserProfile> users){
            for(UserProfile u : users){
                if(userId == u.getId()){
                    return u.getNick();
                }
            }
            return null;
        }
    };

    public abstract String nick(int userId, List<UserProfile> users);

    @Override
    public String toString() {
        return toCamelCase(name());
    }
}
