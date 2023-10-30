package com.example.banking_money.service.customer;

import com.example.banking_money.domain.Customer;
import com.example.banking_money.repository.CustomerRepository;
import com.example.banking_money.service.customer.request.CustomerSaveRequest;
import com.example.banking_money.util.AppUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id).get();
    }
    public void create(CustomerSaveRequest request) {
        Customer newCustomer = AppUtil.mapper.map(request, Customer.class);

        customerRepository.save(newCustomer);
    }

    public Customer update(CustomerSaveRequest request, Long id) {
        Customer newCustomer = AppUtil.mapper.map(request, Customer.class);

        Customer oldCustomer = findById(id);
        newCustomer.setId(id);
        newCustomer.setBalance(oldCustomer.getBalance());

        customerRepository.save(newCustomer);
        return newCustomer;
    }

    public void delete(Long id) {
        customerRepository.deleteById(id);
    }
}
