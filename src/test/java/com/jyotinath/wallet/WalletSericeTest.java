package com.jyotinath.wallet;

import com.jyotinath.wallet.dto.TransferRequest;
import com.jyotinath.wallet.entity.Transaction;
import com.jyotinath.wallet.entity.Wallet;
import com.jyotinath.wallet.repository.TransactionRepository;
import com.jyotinath.wallet.repository.UserRepository;
import com.jyotinath.wallet.repository.WalletRepository;
import com.jyotinath.wallet.service.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WalletSericeTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    @Test
    void transferSuccessful(){
        TransferRequest request = new TransferRequest();
        request.setReceiverId(2l);
        request.setAmount(new BigDecimal(100));

        Wallet sender = new Wallet();
        sender.setBalance(new BigDecimal(200));

        Wallet receiver = new Wallet();
        receiver.setBalance(new BigDecimal(0));

        when(walletRepository.findByUserId(1l)).thenReturn(Optional.of(sender));

        when(walletRepository.findByUserId(2l)).thenReturn(Optional.of(receiver));

        String result = walletService.transfer(1l, request);

        ArgumentCaptor<Wallet> walletArgumentCaptor = ArgumentCaptor.forClass(Wallet.class);
        ArgumentCaptor<Transaction> transactionArgumentCaptor = ArgumentCaptor.forClass(Transaction.class);

        assertEquals("Transaction successful!", result);

        verify(walletRepository, times(2)).save(walletArgumentCaptor.capture());

        verify(transactionRepository).save(transactionArgumentCaptor.capture());

        List<Wallet> walletList = walletArgumentCaptor.getAllValues();

        Wallet updateSender = walletList.get(0);
        Wallet updateReceiver = walletList.get(1);

        assertEquals(new BigDecimal(100), updateSender.getBalance());
        assertEquals(new BigDecimal(100), updateReceiver.getBalance() );

        Transaction transaction = transactionArgumentCaptor.getValue();

        assertEquals(new BigDecimal(100), transaction.getAmount());
        assertEquals("SUCCESS", transaction.getStatus());
        assertEquals(updateSender, transaction.getSender());
        assertEquals(updateReceiver, transaction.getReceiver());
        assertEquals("DEBIT",
                transaction.getCrDrFlg());


        InOrder inOrder = inOrder(walletRepository, transactionRepository);

        inOrder.verify(walletRepository, times(2)).save(any(Wallet.class));
        inOrder.verify(transactionRepository).save(transaction);

    }

    @Test
    void transferInsuffceintBalance(){
        TransferRequest request = new TransferRequest();
        request.setReceiverId(2l);
        request.setAmount(new BigDecimal("100"));

        Wallet sender = new Wallet();
        sender.setBalance(new BigDecimal("50"));

        Wallet receiver = new Wallet();
        receiver.setBalance(new BigDecimal("0"));

        when(walletRepository.findByUserId(1l)).thenReturn(Optional.of(sender));

        when(walletRepository.findByUserId(2l)).thenReturn(Optional.of(receiver));

        RuntimeException ex = assertThrows(RuntimeException.class, ()-> walletService.transfer(1l, request));

        assertEquals("Insuffiecient Balance!", ex.getMessage());

        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(walletRepository, never()).save(any(Wallet.class));
        verify(walletRepository).findByUserId(1L);
        verify(walletRepository).findByUserId(2L);
    }



    @Test
    void getBalanceSuccessful(){
        Wallet wallet = new Wallet();
        wallet.setBalance(new BigDecimal("100"));

        when(walletRepository.findByUserId(1l)).thenReturn(Optional.of(wallet));

        BigDecimal balance = walletService.getBalance(1l);

        assertEquals(new BigDecimal(100), balance);

        verify(walletRepository).findByUserId(1l);
    }

    @Test
    void getBalanceWalletNotFound(){
        when(walletRepository.findByUserId(1l)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> walletService.getBalance(1l));

        assertEquals("Wallet not found!", ex.getMessage());

        verify(walletRepository).findByUserId(1l);
    }



}

