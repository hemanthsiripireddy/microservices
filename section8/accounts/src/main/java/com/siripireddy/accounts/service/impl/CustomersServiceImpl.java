package com.siripireddy.accounts.service.impl;

import com.siripireddy.accounts.dto.AccountsDto;
import com.siripireddy.accounts.dto.CardsDto;
import com.siripireddy.accounts.dto.CustomerDetailsDto;
import com.siripireddy.accounts.dto.LoansDto;
import com.siripireddy.accounts.entity.Accounts;
import com.siripireddy.accounts.entity.Customer;
import com.siripireddy.accounts.exception.ResourceNotFoundException;
import com.siripireddy.accounts.mapper.AccountsMapper;
import com.siripireddy.accounts.repository.AccountsRepository;
import com.siripireddy.accounts.repository.CustomerRepository;
import com.siripireddy.accounts.service.ICustomersService;
import com.siripireddy.accounts.service.client.CardsFeignClient;
import com.siripireddy.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.siripireddy.accounts.mapper.CustomerMapper;

@Service
@AllArgsConstructor
public class CustomersServiceImpl implements ICustomersService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(mobileNumber);
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber);
        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;

    }
}
