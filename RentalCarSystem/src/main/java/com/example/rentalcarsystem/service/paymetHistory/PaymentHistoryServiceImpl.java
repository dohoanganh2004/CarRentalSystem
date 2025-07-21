package com.example.rentalcarsystem.service.paymetHistory;

import com.example.rentalcarsystem.dto.paymentHistory.PaymentHistoryDTO;
import com.example.rentalcarsystem.dto.wallet.TopUpWalletDTO;
import com.example.rentalcarsystem.dto.wallet.WalletCurrentBalanceDTO;
import com.example.rentalcarsystem.dto.wallet.WithdrawBalanceDTO;
import com.example.rentalcarsystem.mapper.PaymentHistoryMapper;
import com.example.rentalcarsystem.model.PaymentHistory;
import com.example.rentalcarsystem.model.User;
import com.example.rentalcarsystem.repository.PaymentHistoryRepository;
import com.example.rentalcarsystem.repository.UserRepository;
import com.example.rentalcarsystem.sercutiry.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class PaymentHistoryServiceImpl implements PaymentHistoryService {

    private final JwtTokenProvider jwtTokenProvider;
    private final PaymentHistoryMapper paymentHistoryMapper;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final UserRepository userRepository;

    public PaymentHistoryServiceImpl(PaymentHistoryRepository paymentHistoryRepository,
                                     JwtTokenProvider jwtTokenProvider,
                                     PaymentHistoryMapper paymentHistoryMapper,
                                     UserRepository userRepository) {
        this.paymentHistoryRepository = paymentHistoryRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.paymentHistoryMapper = paymentHistoryMapper;
        this.userRepository = userRepository;
    }

    /**
     * @param request request from client
     * @param from date from
     * @param to date to
     * @param page page user want to view
     * @param size quantity of each page
     * @return
     */
    @Override
    public Page<PaymentHistoryDTO> paymentHistory(HttpServletRequest request, Instant from, Instant to, Integer page, Integer size) {


        String token = getTokenFromRequest(request);
        int userId = jwtTokenProvider.getUserIdFromToken(token);
        Pageable pageable = PageRequest.of(page, size, Sort.by("paymentDate").descending());
        Page<PaymentHistory> paymentHistoryPage = paymentHistoryRepository.findAll(pageable);
        return paymentHistoryPage.map(paymentHistoryMapper::toPaymentHistoryDTO);
    }



    /**
     * Method to top up wallet
     *
     * @param request request of client
     * @param topUpWalletDTO
     * @return
     */
    @Override
    public WalletCurrentBalanceDTO topUp(HttpServletRequest request, TopUpWalletDTO topUpWalletDTO) {
        String token = getTokenFromRequest(request);
        int userId = jwtTokenProvider.getUserIdFromToken(token);
        User currentUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found!"));
        BigDecimal currentBalance = currentUser.getWallet();
        BigDecimal topUpBalance = topUpWalletDTO.getTopUpBalance();
        if (topUpBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("TopUp Amount cannot be negative");
        }
        BigDecimal totalBalance = currentBalance.add(topUpBalance);
        currentUser.setWallet(totalBalance);
        userRepository.saveAndFlush(currentUser);
        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.setAmount(totalBalance);
        paymentHistory.setTitle("Top Up Wallet " + topUpBalance + "VND");
        paymentHistory.setPaymentDate(Instant.now());
        paymentHistory.setUser(currentUser);
        paymentHistoryRepository.save(paymentHistory);
        System.out.println(currentUser.getWallet());
        WalletCurrentBalanceDTO walletCurrentBalanceDTO = new WalletCurrentBalanceDTO(totalBalance);
        walletCurrentBalanceDTO.setBalance(currentUser.getWallet());
        return walletCurrentBalanceDTO;
    }

    /**
     * Method to withdraw money from wallet
     *
     * @param request
     * @param withdrawalDTO
     * @return
     */
    @Override
    public WalletCurrentBalanceDTO withdraw(HttpServletRequest request, WithdrawBalanceDTO withdrawalDTO) {
        String token = getTokenFromRequest(request);
        int userId = jwtTokenProvider.getUserIdFromToken(token);
        User currentUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found!"));
        BigDecimal currentBalance = currentUser.getWallet();
        BigDecimal withdrawBalance = withdrawalDTO.getWithdrawalAmount();
        if (withdrawBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Withdrawal Amount cannot be negative");
        }
        if (withdrawBalance.compareTo(currentBalance) > 0) {
            throw new IllegalArgumentException("Withdrawal amount exceeds current balance");
        }

        BigDecimal totalBalance = currentBalance.subtract(withdrawBalance);
        currentUser.setWallet(totalBalance);

        userRepository.saveAndFlush(currentUser);
        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.setAmount(withdrawBalance);
        paymentHistory.setTitle("Top Up Wallet " + withdrawBalance + " VND");
        paymentHistory.setPaymentDate(Instant.now());
        paymentHistory.setUser(currentUser);
        paymentHistoryRepository.save(paymentHistory);

        return new WalletCurrentBalanceDTO(totalBalance);
    }


    /**
     * @param request
     * @return
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

        }
        return token;
    }
}
