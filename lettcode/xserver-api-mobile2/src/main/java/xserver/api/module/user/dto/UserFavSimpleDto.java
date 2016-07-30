package xserver.api.module.user.dto;

public class UserFavSimpleDto {
    public final static String FAVORITE_SUCCESS = "1";
    public final static String FAVORITE_FAILURE = "0";

    private String favorite;
    private String message;

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
