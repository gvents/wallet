package ge.kerketi.wallet.dao;

import ge.kerketi.wallet.models.Wallet;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface WalletRepository extends CrudRepository<Wallet, Long> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE testdb.tbl_wallet \n" +
            "SET balance_available = (\n" +
            "SELECT t1.amount\n" +
            "FROM\n" +
            "(SELECT (w.balance_available + :amount) AS amount\n" +
            "FROM testdb.tbl_wallet w JOIN testdb.tbl_client c ON w.client_id = c.id\n" +
            "WHERE c.pid = :pid AND c.account_number = :account_number AND w.wallet_type = :wallet_type) t1)\n" +
            "WHERE id = (\n" +
            "SELECT t2.id\n" +
            "FROM\n" +
            "(SELECT w.id\n" +
            "FROM testdb.tbl_wallet w JOIN testdb.tbl_client c ON w.client_id = c.id\n" +
            "WHERE c.pid = :pid AND c.account_number = :account_number AND w.wallet_type = :wallet_type) t2)",
            nativeQuery = true)
    int updateBalanceAvailable(@Param("amount") Double amount,
                               @Param("pid") String personalNumber,
                               @Param("account_number") Long accountNumber,
                               @Param("wallet_type") String walletType);

    @Query(value = "SELECT * FROM testdb.tbl_wallet " +
            "WHERE client_id = :client_id AND wallet_type = :wallet_type AND  balance_available >= :amount",
            nativeQuery = true)
    Optional<Wallet> checkAvailableWallet(@Param("client_id") Long clientId,
                                          @Param("wallet_type") String walletType,
                                          @Param("amount") Double amount);

}
