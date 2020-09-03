package demo.account;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/v1")
public class AccountController {
//0
 private AccountService accountService;

 public AccountController(AccountService accountService) {
  this.accountService = accountService;
 }

 @RequestMapping(path = "/accounts")
 public ResponseEntity getUserAccount() throws Exception {
  return Optional.ofNullable(accountService.getUserAccounts())//1
   .map(a -> new ResponseEntity<List<Account>>(a, HttpStatus.OK))
   .orElseThrow(() -> new Exception("Accounts for user do not exist"));
 }
}
