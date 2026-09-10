package com.enterprise.customer.service;

import com.enterprise.customer.dto.CustomerRequest;
import com.enterprise.customer.dto.CustomerResponse;
import com.enterprise.customer.entity.Customer;
import com.enterprise.customer.exception.CustomerNotFoundException;
import com.enterprise.customer.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private static final Logger log =
            LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponse createCustomer(CustomerRequest request) {

        log.info("Creating customer with email: {}", request.getEmail());

        Customer customer = new Customer();

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        Customer savedCustomer = customerRepository.save(customer);

        log.info("Customer created successfully with id: {}",
                savedCustomer.getId());

        return new CustomerResponse(
                savedCustomer.getId(),
                savedCustomer.getFirstName(),
                savedCustomer.getLastName(),
                savedCustomer.getEmail(),
                savedCustomer.getPhone()
        );
    }

    public List<CustomerResponse> getAllCustomers() {

        log.info("Fetching all customers");

        return customerRepository.findAll()
                .stream()
                .map(customer -> new CustomerResponse(
                        customer.getId(),
                        customer.getFirstName(),
                        customer.getLastName(),
                        customer.getEmail(),
                        customer.getPhone()
                ))
                .toList();
    }

    public CustomerResponse getCustomerById(Long id) {

        log.info("Fetching customer with id: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with id: " + id));

        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone()
        );
    }

    public CustomerResponse updateCustomer(
            Long id, CustomerRequest request) {

        log.info("Updating customer with id: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with id: " + id));

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        Customer updatedCustomer = customerRepository.save(customer);

        log.info("Customer updated successfully with id: {}",
                updatedCustomer.getId());

        return new CustomerResponse(
                updatedCustomer.getId(),
                updatedCustomer.getFirstName(),
                updatedCustomer.getLastName(),
                updatedCustomer.getEmail(),
                updatedCustomer.getPhone()
        );
    }

    public void deleteCustomer(Long id) {

        log.info("Deleting customer with id: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with id: " + id));

        customerRepository.delete(customer);

        log.info("Customer deleted successfully with id: {}", id);
    }
}