package com.jyotinath.wallet.service;

import com.jyotinath.wallet.dto.DepositeRequest;
import com.jyotinath.wallet.dto.TransferRequest;
import com.jyotinath.wallet.entity.Transaction;
import com.jyotinath.wallet.entity.User;
import com.jyotinath.wallet.entity.Wallet;
import com.jyotinath.wallet.repository.TransactionRepository;
import com.jyotinath.wallet.repository.UserRepository;
import com.jyotinath.wallet.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;

    public  WalletService(WalletRepository walletRepository, TransactionRepository transactionRepository, UserRepository userRepository){
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public BigDecimal getBalance(Long userId){
        Wallet wallet = walletRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Wallet not found!"));

        return  wallet.getBalance();
    }

    @Transactional
    public String transfer(Long senderId, TransferRequest request){
        Wallet sender = walletRepository.findByUserId(senderId).orElseThrow(()-> new RuntimeException("Sender not found!"));

        Wallet reciever = walletRepository.findByUserId(request.getReceiverId()).orElseThrow(()-> new RuntimeException("Reciver not found!"));


        if(sender.getBalance().compareTo(request.getAmount()) < 0)
                throw  new RuntimeException("Insuffiecient Balance!");

        sender.setBalance(sender.getBalance().subtract(request.getAmount()));
        walletRepository.save(sender);

        reciever.setBalance(reciever.getBalance().add(request.getAmount()));
        walletRepository.save(reciever);

        Transaction txn = new Transaction();
        txn.setSender(sender);
        txn.setReceiver(reciever);
        txn.setAmount(request.getAmount());
        txn.setStatus("SUCCESS");
        txn.setTimestamp(LocalDateTime.now());
        transactionRepository.save(txn);

        return "Transaction successful!";
    }

    public List<Transaction> getHistory(Long userId){
        Wallet wallet = walletRepository.findByUserId(userId).orElseThrow(()-> new RuntimeException("Wallet not found!"));
        return transactionRepository.findBySenderId(wallet.getId());
    }

    @Transactional
    public void deposit(String username,long user_id,  DepositeRequest amount) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = walletRepository.findByUserId(user_id)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        wallet.setBalance(wallet.getBalance().add(amount.getAmount()));

        walletRepository.save(wallet);
    }
}
