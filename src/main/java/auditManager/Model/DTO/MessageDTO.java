package auditManager.Model.DTO;

/**
 * Created by Nettle on 2017/2/6.
 */
public class MessageDTO {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageDTO(String msg) {
        this.message = msg;
    }
}
