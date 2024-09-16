package email.email_management.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idt;
    private String sender;
    private String recipient;
    private String subject;
    private String body;
    private Boolean read;
    @Column(name = "SEND_AT", columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime send_at;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "folder_idt", nullable = false)
    private Folder folder;



}
