package data.dbDTO;

import lombok.*;
import org.mongodb.morphia.annotations.Entity;

import java.util.List;

/** Basic LinkDTO
 * Created by Christian on 04-05-2017.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class Link extends BaseDTO{

    private String text;
    private String href;

    public Link(String text, String href) {
        this.text = text;
        this.href = href;
    }

    private List<String> addData;

}
