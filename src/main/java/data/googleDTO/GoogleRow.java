package data.googleDTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 25-05-2017.
 */
@Data
public class GoogleRow {
    private List<GoogleCell> cells = new ArrayList<>();

    public void addCell(GoogleCell cell){
        cells.add(cell);
    }
}
