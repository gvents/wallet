package ge.kerketi.wallet.dao;

import ge.kerketi.wallet.models.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClientRepository extends CrudRepository<Client, Long> {
    @Query(value = "SELECT * FROM tbl_client WHERE account_number = :account_number",
            nativeQuery = true)
    Optional<Client> findByAccountNumber(@Param("account_number") Long accountNumber);
}
