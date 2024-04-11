package ru.egorov.app;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class AccountRepository {
    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    public void save(Account account) {
        accounts.put(account.getId(), account);
    }

    public Account getById(String id) {
        return accounts.get(id);
    }

    public List<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

}
