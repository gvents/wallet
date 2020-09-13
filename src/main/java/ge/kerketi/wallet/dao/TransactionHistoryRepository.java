package ge.kerketi.wallet.dao;

import ge.kerketi.wallet.models.TransactionHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransactionHistoryRepository extends CrudRepository<TransactionHistory, Long> {
    @Query(value = "SELECT * FROM testdb.tbl_transaction_history th \n" +
            "WHERE th._from = IFNULL(:from_account, th._from) AND th._to = IFNULL(:to_account, th._to)",
            nativeQuery = true)
    Optional<List<TransactionHistory>> allOrFilterTransactionHistory(@Param("from_account") String fromAccount,
                                                                     @Param("to_account") String toAccount);
}
