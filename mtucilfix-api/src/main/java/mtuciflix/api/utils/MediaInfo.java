package mtuciflix.api.utils;

public class MediaInfo {

    private String title;
    private String description;
    private String director;
    private int mediaId;

    public MediaInfo(String title, String description, String director, int mediaId) {
        this.description = description;
        this.title = title;
        this.mediaId = mediaId;
        this.director = director;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getMediaId() {
        return mediaId;
    }

    public String getDirector() {
        return director;
    }
}
