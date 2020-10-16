import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountTest {

	private static final int BIG_AMOUNT = 1000001;
	private static final int BOUND = 1000000;
	private Account account;

	@BeforeEach
	void createNewAccount() {
		account = new Account();
	}

	@Test
	void onTryingToChangeMaxCreditWhenAccountNotBlocked_ReturnFalse() {
		assertEquals(false, account.setMaxCredit(100));
	}

	@Test
	void onTryingToChangeMaxCreditWhenAmountIsBiggerThanBound_ReturnFalse() {
		int prevCredit = account.getMaxCredit();

		assertAll(
			() -> assertFalse(account.setMaxCredit(BIG_AMOUNT)),
			() -> assertEquals(prevCredit, account.getMaxCredit()),
			() -> assertFalse(account.setMaxCredit(-BIG_AMOUNT)),
			() -> assertEquals(prevCredit, account.getMaxCredit()));
	}

	@Test
	void onTryingToChangeMaxCreditWhenAllIsOk_ReturnTrue() {
		assertAll(
			() -> assertTrue(account.setMaxCredit(100)),
			() -> assertEquals(100, account.getMaxCredit())
		);
	}

	@Test
	void onTryingToDepositOrWithdrawSumUpperBound_ReturnFalse() {
		int prevBalance = account.getBalance();

		assertAll(
			() -> assertFalse(account.deposit(BIG_AMOUNT)),
			() -> assertEquals(prevBalance, account.getBalance()),
			() -> assertFalse(account.withdraw(BIG_AMOUNT)),
			() -> assertEquals(prevBalance, account.getBalance())
		);
	}

	@Test
	void onTryingToDepositOrWithdrawNegativeSum_ReturnFalse() {
		int prevBalance = account.getBalance();

		assertAll(
			() -> assertFalse(account.deposit(-1)),
			() -> assertEquals(prevBalance, account.getBalance()),
			() -> assertFalse(account.withdraw(-1)),
			() -> assertEquals(prevBalance, account.getBalance())
		);
	}

	@Test
	void onBlockedAccount_ReturnFalse() {
		account.block();
		int prevBalance = account.getBalance();

		assertAll(
			() -> assertFalse(account.deposit(100)),
			() -> assertEquals(prevBalance, account.getBalance()),
			() -> assertFalse(account.withdraw(100)),
			() -> assertEquals(prevBalance, account.getBalance())
		);
	}

	@Test
	void onTryingToDepositSumWhenBalanceBecomesUpperBound_ReturnFalse() {
		account.deposit(BOUND - 1);
		int prevBalance = account.getBalance();

		assertAll(
			() -> assertFalse(account.deposit(2)),
			() -> assertEquals(prevBalance, account.getBalance())
		);
	}

	@Test
	void onTryingUnblockWhenBalanceIsBelowCreditMaximum_ReturnFalse() {
		account.withdraw(100);
		account.setMaxCredit(10);

		assertAll(
			() -> assertFalse(account.unblock()),
			() -> assertTrue(account.isBlocked())
		);
	}

	@Test
	void onTryingBlockWhenAllOk_ReturnTrue() {

		assertAll(
			() -> assertTrue(account.unblock()),
			() -> assertFalse(account.isBlocked())
		);
	}

	@Test
	void onTryingWithdrawWhenAllOk_ReturnTrue() {

		assertAll(
			() -> assertTrue(account.withdraw(100)),
			() -> assertEquals(-100, account.getBalance())
		);
	}

	@Test
	void onTryingDepositWhenAllOk_ReturnTrue() {

		assertAll(
			() -> assertTrue(account.deposit(100)),
			() -> assertEquals(100, account.getBalance())
		);
	}
}