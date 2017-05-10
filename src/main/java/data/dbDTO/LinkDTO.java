package data.dbDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/** Basic LinkDTO
 * Created by Christian on 04-05-2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkDTO {
    private String text;
    private String href;
    private List<String> addData;

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
