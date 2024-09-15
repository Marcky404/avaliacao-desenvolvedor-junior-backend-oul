package email.email_management.repository;

import email.email_management.models.Folder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FolderRepository extends JpaRepository<Folder,Integer> {

    @Query("SELECT f FROM Folder f WHERE f.mailbox.name = :mailbox")
    Page<Folder> findByMailboxName(@Param("mailbox") String mailbox, PageRequest pageRequest);

}
