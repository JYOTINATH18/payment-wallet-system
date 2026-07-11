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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class WalletApplicationTests {


}
