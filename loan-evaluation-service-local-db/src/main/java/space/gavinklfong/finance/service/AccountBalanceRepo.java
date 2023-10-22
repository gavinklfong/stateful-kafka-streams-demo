package space.gavinklfong.finance.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import space.gavinklfong.finance.dto.AccountBalance;

@Repository
public interface AccountBalanceRepo extends CrudRepository<AccountBalance, String> {
}
