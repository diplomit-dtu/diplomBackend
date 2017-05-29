package data.dbDTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mongodb.morphia.annotations.Entity;

import java.util.List;

/** Basic LinkDTO
 * Created by Christian on 04-05-2017.
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class Link extends BaseDTO{

    @NonNull private String text;
    @NonNull private String href;
    private List<String> addData;

}
