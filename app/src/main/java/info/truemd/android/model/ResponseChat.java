package info.truemd.android.model;

/**
 * Created by yashvardhansrivastava on 20/03/16.
 */
public class ResponseChat {

    String response_text,color,search_text;
    boolean on_clickable, on_longpressable;

    public ResponseChat() {
    }

    public ResponseChat(String response_text, String color, String search_text, boolean on_clickable, boolean on_longpressable) {
        this.response_text = response_text;
        this.color = color;
        this.search_text = search_text;
        this.on_clickable = on_clickable;
        this.on_longpressable = on_longpressable;
    }

    public String getResponse_text() {

        return response_text;
    }

    public void setResponse_text(String response_text) {
        this.response_text = response_text;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSearch_text() {
        return search_text;
    }

    public void setSearch_text(String search_text) {
        this.search_text = search_text;
    }

    public boolean isOn_clickable() {
        return on_clickable;
    }

    public void setOn_clickable(boolean on_clickable) {
        this.on_clickable = on_clickable;
    }

    public boolean isOn_longpressable() {
        return on_longpressable;
    }

    public void setOn_longpressable(boolean on_longpressable) {
        this.on_longpressable = on_longpressable;
    }
}
