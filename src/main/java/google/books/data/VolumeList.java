package google.books.data;

import lombok.Data;

import java.util.List;

@Data
public class VolumeList {
    private String kind;
    private Integer totalItems;
    private List<Volume> items;
}
