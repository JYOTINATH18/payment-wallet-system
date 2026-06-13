package com.jyotinath.wallet.repository;

import com.jyotinath.wallet.entity.Transaction;
import com.jyotinath.wallet.entity.User;
import com.jyotinath.wallet.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySenderId(Long senderId);
    List<Transaction> findByReceiverId(Long receiverId);

    Page<Transaction> findBySender(Wallet wallet, Pageable pageable);
}