package ge.kerketi.wallet.services;

import ge.kerketi.wallet.dao.ClientRepository;
import ge.kerketi.wallet.dao.TransactionHistoryRepository;
import ge.kerketi.wallet.dao.WalletRepository;
import ge.kerketi.wallet.enums.WalletType;
import ge.kerketi.wallet.models.Client;
import ge.kerketi.wallet.models.TransactionHistory;
import ge.kerketi.wallet.models.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WalletService {
    private ClientRepository clientRepository;
    private WalletRepository walletRepository;
    private TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    public WalletService(ClientRepository clientRepository,
                         WalletRepository walletRepository,
                         TransactionHistoryRepository transactionHistoryRepository) {
        this.clientRepository = clientRepository;
        this.walletRepository = walletRepository;
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    public Client registerClient(Client client) {
        Client created = null;

        try {
            created = clientRepository.save(client);

            if (created != null) {
                for (WalletType walletType : WalletType.values()) {
                    walletRepository.save(new Wallet(walletType.toString(),
                            0.,
                            created));
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return created;
    }

    public TransactionHistory cashIn(String pid, Long accountNumber, Double amount, String walletType) {
        TransactionHistory transactionHistory = null;

        try {
            transactionHistory = walletRepository.updateBalanceAvailable(amount,
                    pid,
                    accountNumber,
                    walletType) == 1 ?
                    transactionHistoryRepository.save(
                            new TransactionHistory(amount,
                                    "kerketi",
                                    "kerketi",
                                    accountNumber)
                    ) : null;

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return transactionHistory;
    }

    public TransactionHistory transfer(Long fromAccount, Long toAccount, Double amount, String walletType) {
        TransactionHistory transactionHistory = null;

        try {
            Optional<Client> from = clientRepository.findByAccountNumber(fromAccount);
            Optional<Client> to = clientRepository.findByAccountNumber(toAccount);

            if (from.isPresent() && to.isPresent()) {
                Optional<Wallet> optionalWallet = walletRepository.checkAvailableWallet(from.get().getId(),
                        walletType,
                        amount);

                if (optionalWallet.isPresent()) {
                    int res = walletRepository.updateBalanceAvailable(-1 * amount,
                            from.get().getPersonalNumber(),
                            fromAccount,
                            walletType);

                    transactionHistory = res == 1 ? walletRepository.updateBalanceAvailable(amount,
                            to.get().getPersonalNumber(),
                            toAccount,
                            walletType) == 1 ?
                            transactionHistoryRepository.save(
                                    new TransactionHistory(amount,
                                            fromAccount.toString(),
                                            fromAccount.toString(),
                                            toAccount)
                            ) : null : null;
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return transactionHistory;
    }

    public List<TransactionHistory> transactionHistory(String fromAccount, String toAccount) {
        return transactionHistoryRepository.allOrFilterTransactionHistory(fromAccount, toAccount).orElse(null);
    }
}
