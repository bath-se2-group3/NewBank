# NewBank

NewBank is an idea for a new disrupter bank where customers can interact with their
accounts via a simple command-line interface.

## Requirements

* [openjdk >=11.0.15](https://jdk.java.net/archive/) 

### Optional

* A terminal (i.e., [PowerShell](https://docs.microsoft.com/en-us/powershell/), [zsh](https://ohmyz.sh/) or [Bash](https://www.gnu.org/software/bash/))
* [Git](https://git-scm.com/)
* An IDE (i.e., [IntelliJ](https://www.jetbrains.com/idea/) or [VSCode](https://code.visualstudio.com/))

## Getting started
The following paragraphs outlines how to compile and bootup the NewBank server as well as client.

### Compile
```
> javac ./newbank/client/*.java
> javac ./newbank/server/*.java
```

### Bootup
Keep in mind that you are not able to bootup a client without a server. Therefore, do something like:

```
> java newbank.server.NewBankServer &
> java newbank.client.ExampleClient
```

### Usage
The following table outlines the protocol that you can use with the NewBank server. Note that values in the return column are examples of successful requests.

|Command|Description|Example|Returns|
|---|---|---|---|
|`SHOWMYACCOUNTS`|Returns a list of all the customers accounts along with their current balance|`SHOWMYACCOUNTS`|`Main: 1000.0`|
|`CREATEACCOUNT <Account_Name> <Starting_Balance>`|Creates a new account|`CREATEACCOUNT Savings 100`|`Savings created with a balance of 100.0. You now have the following accounts: Main: £1000.00 Savings: £100.00`|
|`CHANGEUSERNAME <Username>`|Changes the username of a logged in user|`CHANGEUSERNAME Lola`|`CHANGEUSERNAME Lola Request from bhagy Username was updated to lola`|
|`MOVE <Payer_Account_name> <Recipient_Account_name> <Amount>`|Moves money between a user's existing accounts|`MOVE Main Savings 100`|`£100.00 has been transferred from main to savings`|
|`PAY <Payer_Account_name> <Person/Company> <Recipient_Account_name> <Sort_code> <Ammount>`|Pay another user from your account to their account|`PAY Checking Bhagy Main EC12345 1500`|`£100.00 has been transferred from john to bhagy`|
|`LOGOUT`|Log out of your account|`LOGOUT`|`Logged out successfully!`|
|`ADDMYCONTACTDETAILS <Mail_address \| Phone_number>`|Add your e-mail address or phone number|`ADDMYCONTACTDETAILS foo@bar.baz`|`Updated e-mail address to: foo@bar.baz`|


## Misc.

### Javadoc
NewBank  utilizes [JavaDoc](https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html); hence, you can always regenerate the documentation via:

```
> javadoc -d docs/newbank/client newbank.client 
> javadoc -d docs/newbank/server newbank.server 
```

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License

[MIT](https://choosealicense.com/licenses/mit/)