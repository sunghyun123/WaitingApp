package com.example.backend.service;


import com.example.backend.entity.Customer;
import com.example.backend.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    public Customer getCustomerByPhoneNumber(String phoneNumber){
        return customerRepository.findByPhoneNumber(phoneNumber);
    }

    public Customer saveCustomer(Customer customer){
        return customerRepository.save(customer);
    }

}
