package data;

import java.util.List;

/**
 * Created by Christian on 04-05-2017.
 */
public class LinkDTO extends BaseDTO{
    private String text;
    private String href;
    private List<String> addData;

    @Override
    public String toString() {
        return "LinkDTO{" +
                "text='" + text + '\'' +
                ", href='" + href + '\'' +
                ", addData=" + addData +
                '}' + super.toString();
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getAddData() {
        return addData;
    }

    public void setAddData(List<String> addData) {
        this.addData = addData;
    }
}
