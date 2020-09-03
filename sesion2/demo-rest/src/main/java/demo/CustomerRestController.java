package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
//0
@RestController
//1
@RequestMapping("/v1/customers")
public class CustomerRestController {

 @Autowired
 private CustomerRepository customerRepository;

 // 3.
 @RequestMapping(method = RequestMethod.OPTIONS)
 ResponseEntity<?> options() {

  //3.1
  return ResponseEntity
   .ok()
   .allow(HttpMethod.GET, HttpMethod.POST,
          HttpMethod.HEAD, HttpMethod.OPTIONS,
          HttpMethod.PUT, HttpMethod.DELETE)
          .build();
   //@formatter:on
 }

 @GetMapping
  Collection<Customer> getCollection() {
  return this.customerRepository.findAll();
 }

 // <2>
 @GetMapping(value = "/{id}")
 //2.1
 Customer get(@PathVariable Long id) {
  return this.customerRepository.findById(id)
   .orElseThrow(() -> new CustomerNotFoundException(id));
 }

//4
 @PostMapping
 ResponseEntity<Customer> post(@RequestBody Customer c) { 
    //4.1
  Customer customer = this.customerRepository.save(new Customer(c
   .getFirstName(), c.getLastName()));
//4.2
  URI uri = MvcUriComponentsBuilder.fromController(getClass()).path("/{id}")
   .buildAndExpand(customer.getId()).toUri();
   System.out.println("uri: "+uri);
   //4.3
  return ResponseEntity.created(uri).body(customer);
 }

 // <5>
 @DeleteMapping(value = "/{id}")
 ResponseEntity<?> delete(@PathVariable Long id) {
//5.1
  return this.customerRepository.findById(id).map(c -> {
   customerRepository.delete(c);
   return ResponseEntity.noContent().build();
  }).orElseThrow(() -> new CustomerNotFoundException(id));
 }

 // <6>
 @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
 ResponseEntity<?> head(@PathVariable Long id) {
  return this.customerRepository.findById(id)
   .map(exists -> ResponseEntity.noContent().build())
   .orElseThrow(() -> new CustomerNotFoundException(id));
 }

 // <6>
 @PutMapping(value = "/{id}")
 ResponseEntity<Customer> put(@PathVariable Long id, @RequestBody Customer c) {
  return this.customerRepository
   .findById(id)
   .map(
    existing -> {
     Customer customer = this.customerRepository.save(new Customer(existing
      .getId(), c.getFirstName(), c.getLastName()));
     URI selfLink = URI.create(ServletUriComponentsBuilder.fromCurrentRequest()
      .toUriString());
     return ResponseEntity.created(selfLink).body(customer);
    }).orElseThrow(() -> new CustomerNotFoundException(id));

 }
}
