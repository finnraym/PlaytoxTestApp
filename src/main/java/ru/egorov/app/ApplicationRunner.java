package ru.egorov.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ApplicationRunner {
    private final static Integer DEFAULT_COUNT_THREADS = 2;
    private final static Integer DEFAULT_COUNT_ACCOUNTS = 4;
    private final static Logger log = LogManager.getLogger(ApplicationRunner.class);

    public static void run(String[] args) {
        Map<String, Integer> commands = CommandLineReader.parseCommandLine(args);
        Integer nThreads = commands.get(CommandLineReader.COUNT_THREADS_KEY_NAME);
        ExecutorService executorService =
                Executors.newFixedThreadPool(Objects.requireNonNullElse(nThreads, DEFAULT_COUNT_THREADS));
        Integer nAccounts = commands.get(CommandLineReader.COUNT_ACCOUNT_KEY_NAME);
        AccountRepository accountRepository =
                new AccountRepository();
        List<String> idsAccount = new ArrayList<>();

        for (int i = 0; i < Objects.requireNonNullElse(nAccounts, DEFAULT_COUNT_ACCOUNTS); i++) {
            Account account = new Account(
                    UUID.randomUUID().toString(),
                    10_000L
            );
            log.info("Create account with id " + account.getId());
            idsAccount.add(account.getId());
            accountRepository.save(account);
        }

        AccountService accountService = new AccountService(accountRepository);
        Random random = new Random();

        for (int i = 0; i < 30; i++) {
            Account from = accountRepository.getById(idsAccount.get(random.nextInt(0, idsAccount.size())));
            Account to = accountRepository.getById(idsAccount.get(random.nextInt(0, idsAccount.size())));
            Long amount = random.nextLong(0L, 10_000L);
            final int transactionNumber = i + 1;
            executorService.submit(() -> {
                try {
                    Thread.sleep(random.nextInt(1000, 2000));
                    log.info("Transaction #{}: Money transfer from {} to {} with amount {}",
                            transactionNumber, from.getId(), to.getId(), amount);
                    accountService.transfer(from, to, amount);
                } catch (InterruptedException e) {
                    log.error("Something went wrong! More details:\n" + e.getMessage());
                }
            });
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("Something went wrong! More details:\n" + e.getMessage());
        }
    }
}
