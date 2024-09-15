package email.email_management.models.response;

import email.email_management.models.Folder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderResponse {

    private Integer idt;
    private String name;

    public static FolderResponse toResponse(Folder folder) {
        FolderResponse response = new FolderResponse();

        response.setIdt(folder.getIdt());
        response.setName(folder.getName());

        return response;
    }
}
