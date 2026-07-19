package com.jyotinath.wallet.service;

import com.jyotinath.wallet.dto.DepositeRequest;
import com.jyotinath.wallet.dto.PaginationResponse;
import com.jyotinath.wallet.dto.TransactionResponse;
import com.jyotinath.wallet.dto.TransferRequest;
import com.jyotinath.wallet.entity.Transaction;
import com.jyotinath.wallet.entity.User;
import com.jyotinath.wallet.entity.Wallet;
import com.jyotinath.wallet.event.TransactionEvent;
import com.jyotinath.wallet.kafka.TransactionProducer;
import com.jyotinath.wallet.repository.TransactionRepository;
import com.jyotinath.wallet.repository.UserRepository;
import com.jyotinath.wallet.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final TransactionProducer transactionProducer;

    public  WalletService(WalletRepository walletRepository, TransactionRepository transactionRepository, UserRepository userRepository, TransactionProducer transactionProducer){
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.transactionProducer = transactionProducer;
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
        txn.setCrDrFlg("DEBIT");
        txn.setAmount(request.getAmount());
        txn.setStatus("SUCCESS");
        txn.setTimestamp(LocalDateTime.now());
        transactionRepository.save(txn);
        TransactionEvent event = new TransactionEvent(
                senderId,
                reciever.getId(),
                request.getAmount(),
                "SUCCESS",
                LocalDateTime.now()
        );
        transactionProducer.publish(event);

        return "Transaction successful!";
    }

    public PaginationResponse<TransactionResponse> getHistory(long userId, int page, int size){
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        Pageable pageable = PageRequest.of(page,size);

        Page<Transaction> transactions = transactionRepository.findBySender(wallet, pageable);

        Page<TransactionResponse> responsePage =
                transactions.map(tx ->
                        new TransactionResponse(
                                tx.getId(),
                                tx.getAmount(),
                                tx.getCrDrFlg(),
                                tx.getSender()
                                        .getUser()
                                        .getUsername(),
                                tx.getReceiver()
                                        .getUser()
                                        .getUsername(),
                                tx.getTimestamp()
                        ));

        return new PaginationResponse<>(
                responsePage.getNumber(),
                responsePage.getSize(),
                responsePage.getTotalElements(),
                responsePage.getTotalPages(),
                responsePage.hasNext(),
                responsePage.getContent()
        );

    }

    @Transactional
    public void deposit(String username,long user_id,  DepositeRequest amount) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet owner  = walletRepository.findByUserId(user_id)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        owner.setBalance(owner.getBalance().add(amount.getAmount()));

        Transaction txn = new Transaction();
        txn.setSender(owner);
        txn.setReceiver(owner);
        txn.setCrDrFlg("CREDIT");
        txn.setAmount(amount.getAmount());
        txn.setStatus("SUCCESS");
        txn.setTimestamp(LocalDateTime.now());
        transactionRepository.save(txn);

        walletRepository.save(owner);
    }
}
