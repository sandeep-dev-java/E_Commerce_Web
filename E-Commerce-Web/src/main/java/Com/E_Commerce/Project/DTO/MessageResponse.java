package Com.E_Commerce.Project.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class MessageResponse {
    private  String message;

    public MessageResponse(String message){
        this.message= message;
    }
}
