package com.micropay.customers.service;

import com.micropay.customers.datastore.ContactRepository;
import com.micropay.customers.datastore.UserRepository;
import com.micropay.customers.domains.Contact;
import com.micropay.customers.domains.Customer;
import com.micropay.customers.exceptions.UserExists;
import com.micropay.customers.exceptions.UserNotFound;
import com.micropay.customers.utils.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Service
@Slf4j
public class CustomerService {
    private UserRepository userRepository;
    private ContactRepository contactRepository;
    @Autowired
    public CustomerService(UserRepository userRepository, ContactRepository contactRepository) {
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
    }

    public Customer getCustomerById(Long id) {
        log.trace("looking for the user with id {} in DB", id);
        return userRepository.findById(id).orElseThrow(() -> new UserNotFound("Customer not found."));
    }

    public Customer createCustomer(Customer newCustomer) {
        if(userRepository.findByNameIgnoreCaseContainingAndDateOfBirth(newCustomer.getName(), newCustomer.getDateOfBirth()).isPresent()){
            throw new UserExists("Customer already exist.");
        }
        log.trace("saving user to DB");
        return userRepository.save(newCustomer);
    }

    public Customer updateCustomer(UpdateUserCommand updateUser) {
        Customer foundCustomer = userRepository.findById(updateUser.getId()).orElseThrow(() -> new UserNotFound("Customer not found."));
        Contact contact = foundCustomer.getContact();
        if (!StringUtils.isEmpty(updateUser.getAddress()) && !updateUser.getAddress().equalsIgnoreCase(contact.getAddress())) {
            contact.setAddress(updateUser.getAddress());
        }
        if (!StringUtils.isEmpty(updateUser.getEmail()) && !updateUser.getEmail().equalsIgnoreCase(contact.getEmail())) {
            contact.setEmail(updateUser.getEmail());
        }
        if (null != updateUser.getPhone() && !updateUser.getPhone().equals(contact.getPhone())) {
            contact.setPhone(updateUser.getPhone());
        }
        contactRepository.saveAndFlush(contact);
        if (!StringUtils.isEmpty(updateUser.getName()) && !updateUser.getName().equalsIgnoreCase(foundCustomer.getName())) {
            foundCustomer.setName(updateUser.getName());
        }
        if (!StringUtils.isEmpty(updateUser.getDateOfBirth()) && !updateUser.getDateOfBirth().equals(foundCustomer.getDateOfBirth())) {
            foundCustomer.setDateOfBirth(updateUser.getDateOfBirth());
        }
        return userRepository.save(foundCustomer);
    }

    @Getter
    public static class UpdateUserCommand extends SelfValidating<UpdateUserCommand> {
        @Min(1)
        private Long id;
        private String name;
        @Past(message = "Date of birth cannot be present date")
        private LocalDate dateOfBirth;
        @Email(message = "Invalid email")
        private String email;
        @Length(min = 10, max = 100, message = "Address length should be with in 10 to 100")
        private String address;
        @Pattern(regexp = "(^$|[0-9]{10})", message = "Phone number should be of 10 digits")
        private String phone;

        public UpdateUserCommand(Long id, String name, String dateOfBirth, String email, String address, String phone) {
            this.id = id;
            this.name = name;
            if(!StringUtils.isEmpty(dateOfBirth)){
                this.dateOfBirth = LocalDate.parse(dateOfBirth);
            }
            this.email = email;
            this.address = address;
            this.phone = phone;
            this.validateSelf();
        }
    }
}
