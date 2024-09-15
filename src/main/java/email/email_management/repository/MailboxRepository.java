package email.email_management.repository;

import email.email_management.models.Mailbox;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MailboxRepository extends JpaRepository<Mailbox, Integer> {

    Boolean existsByName(String name);

    Optional<Mailbox> findByName(String name);


}
