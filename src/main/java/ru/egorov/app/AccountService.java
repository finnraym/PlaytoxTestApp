package ru.egorov.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccountService {
    private final AccountRepository repository;
    private final Logger log = LogManager.getLogger(AccountService.class);

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public boolean transfer(Account from, Account to, Long amount) {
        Account first, second;
        int compare = from.getId().compareTo(to.getId());

        if (compare >= 0) {
            first = from;
            second = to;
        } else {
            first = to;
            second = from;
        }

        synchronized (first) {
            synchronized (second) {
                if (accountIsAvailableForTransfer(from, amount)) {
                    from.setMoney(from.getMoney() - amount);
                    to.setMoney(to.getMoney() + amount);
                    repository.save(from);
                    repository.save(to);
                    log.info("Money transferred successfully!");
                    return true;
                } else {
                    log.info("Money has not been transferred! Insufficient funds or amount is negative.");
                    return false;
                }
            }
        }
    }

    private boolean accountIsAvailableForTransfer(Account account, Long amount) {
        return amount >= 0 && account.getMoney() >= amount;
    }

}
