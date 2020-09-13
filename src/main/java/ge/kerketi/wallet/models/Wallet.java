package ge.kerketi.wallet.models;

import ge.kerketi.wallet.enums.WalletType;

import javax.persistence.*;

@Entity
@Table(name = "tbl_wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "wallet_type", nullable = false)
    private String walletType;

    @Column(name = "balance_available", nullable = false)
    private Double balanceAvailable = 0.0;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public Wallet() {}

    public Wallet(String walletType, Double balanceAvailable, Client client) {
        this.walletType = walletType;
        this.balanceAvailable = balanceAvailable;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWalletType() {
        return walletType;
    }

    public void setWalletType(String walletType) {
        this.walletType = walletType;
    }

    public Double getBalanceAvailable() {
        return balanceAvailable;
    }

    public void setBalanceAvailable(Double balanceAvailable) {
        this.balanceAvailable = balanceAvailable;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
