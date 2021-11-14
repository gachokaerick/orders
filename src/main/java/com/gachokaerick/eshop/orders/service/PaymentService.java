package com.gachokaerick.eshop.orders.service;

import com.gachokaerick.eshop.orders.domain.Payment;
import com.gachokaerick.eshop.orders.repository.PaymentRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Payment}.
 */
@Service
@Transactional
public class PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Save a payment.
     *
     * @param payment the entity to save.
     * @return the persisted entity.
     */
    public Payment save(Payment payment) {
        log.debug("Request to save Payment : {}", payment);
        return paymentRepository.save(payment);
    }

    /**
     * Partially update a payment.
     *
     * @param payment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Payment> partialUpdate(Payment payment) {
        log.debug("Request to partially update Payment : {}", payment);

        return paymentRepository
            .findById(payment.getId())
            .map(existingPayment -> {
                if (payment.getCreateTime() != null) {
                    existingPayment.setCreateTime(payment.getCreateTime());
                }
                if (payment.getUpdateTime() != null) {
                    existingPayment.setUpdateTime(payment.getUpdateTime());
                }
                if (payment.getPaymentStatus() != null) {
                    existingPayment.setPaymentStatus(payment.getPaymentStatus());
                }
                if (payment.getPayerCountryCode() != null) {
                    existingPayment.setPayerCountryCode(payment.getPayerCountryCode());
                }
                if (payment.getPayerEmail() != null) {
                    existingPayment.setPayerEmail(payment.getPayerEmail());
                }
                if (payment.getPayerName() != null) {
                    existingPayment.setPayerName(payment.getPayerName());
                }
                if (payment.getPayerSurname() != null) {
                    existingPayment.setPayerSurname(payment.getPayerSurname());
                }
                if (payment.getPayerId() != null) {
                    existingPayment.setPayerId(payment.getPayerId());
                }
                if (payment.getCurrency() != null) {
                    existingPayment.setCurrency(payment.getCurrency());
                }
                if (payment.getAmount() != null) {
                    existingPayment.setAmount(payment.getAmount());
                }
                if (payment.getPaymentId() != null) {
                    existingPayment.setPaymentId(payment.getPaymentId());
                }

                return existingPayment;
            })
            .map(paymentRepository::save);
    }

    /**
     * Get all the payments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Payment> findAll(Pageable pageable) {
        log.debug("Request to get all Payments");
        return paymentRepository.findAll(pageable);
    }

    /**
     * Get one payment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Payment> findOne(Long id) {
        log.debug("Request to get Payment : {}", id);
        return paymentRepository.findById(id);
    }

    /**
     * Delete the payment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Payment : {}", id);
        paymentRepository.deleteById(id);
    }
}
