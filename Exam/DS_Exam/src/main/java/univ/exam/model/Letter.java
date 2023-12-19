package univ.exam.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Letter implements Entity {
    private Long id;
    private String title;
    private User from;
    private User to;
    private LocalDateTime sentDate;
    private String category;
    private List<String> tags;
}
