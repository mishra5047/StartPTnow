package models;

/**
 * Created on 08-10-2019.
 */
public class BulletTextModel {

    private String text;
    private String link;

    public BulletTextModel(String text, String link) {
        this.text = text;
        this.link = link;
    }

    public String getText() {
        return text;
    }

    public String getLink() {
        return link;
    }

}
