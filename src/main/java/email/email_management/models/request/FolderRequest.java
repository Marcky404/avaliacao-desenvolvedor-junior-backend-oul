package email.email_management.models.request;

import email.email_management.models.Folder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderRequest {

    @NotNull(message = "O nome não pode ser nulo.")
    @Pattern(regexp = "^[a-zA-Z0-9-_]{1,100}$", message = "Pasta inválida")
    private String name;


    public static Folder toEntity(FolderRequest folderRequest){
        Folder folder = new Folder();

        folder.setName(folderRequest.getName());

        return folder;
    }

}
