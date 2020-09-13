package ge.kerketi.wallet.controllers;

import ge.kerketi.wallet.models.Client;
import ge.kerketi.wallet.models.TransactionHistory;
import ge.kerketi.wallet.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class WalletController {
    private WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("register-client")
    public @ResponseBody
    Client registerClient(@RequestBody Client client) {
        return walletService.registerClient(client);
    }

    @PostMapping("cash-in")
    public @ResponseBody
    TransactionHistory cashIn(String pid, Long accountNumber, Double amount, String walletType) {
        return walletService.cashIn(pid, accountNumber, amount, walletType);
    }

    @PostMapping("transfer")
    public @ResponseBody
    TransactionHistory transfer(Long fromAccount, Long toAccount, Double amount, String walletType) {
        return walletService.transfer(fromAccount, toAccount, amount, walletType);
    }

    @GetMapping("transaction-history")
    public @ResponseBody
    List<TransactionHistory> transactionHistory(String fromAccount, String toAccount) {
        return walletService.transactionHistory(fromAccount, toAccount);
    }
}
