package sg.edu.nus.iss.server.models;

public class Article {
    
    private Integer p_id;
    private String title;
    private String content;
    private byte[] picture;

    public Integer getP_id() {
        return p_id;
    }
    public void setP_id(Integer p_id) {
        this.p_id = p_id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public byte[] getPicture() {
        return picture;
    }
    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
    

}
