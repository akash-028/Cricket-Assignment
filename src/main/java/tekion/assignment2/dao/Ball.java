package tekion.assignment2.dao;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class Ball
{
    private int ballNumber ;
    private int bowlerId ;
    private int onStrikerId ;
    private int offStrikerId ;
    private int ballResult ;

}
