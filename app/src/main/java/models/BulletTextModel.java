package models;

/**
 * Created on 08-10-2019.
 */
public class BulletTextModel {

    private String text;
    private String link;
    private String no;

    public BulletTextModel(String no,String text, String link) {
        this.text = text;
        this.link = link;
        this.no = no;
    }

    public String getNo() {
        return no;
    }

    public String getText() {
        return text;
    }

    public String getLink() {
        return link;
    }

}
