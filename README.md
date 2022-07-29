# NewBank

foo bar baz

## Requirements

* openjdk >=11.0.15 

### Optional

* A terminal (i.e., [PowerShell](https://docs.microsoft.com/en-us/powershell/), [zsh](https://ohmyz.sh/) or [Bash](https://www.gnu.org/software/bash/))
* [Git](https://git-scm.com/)
* An IDE (i.e., [IntelliJ](https://www.jetbrains.com/idea/) or [VSCode](https://code.visualstudio.com/))

## Getting started

### Compile
```
> javac ./newbank/client/*.java
> javac ./newbank/server/*.java
```

### Bootup
```
> java newbank.server.NewBankServer &
> java newbank.client.ExampleClient
```

### Usage

```
This document details the protocol for interacting with the NewBank server.  

A customer enters the command below and sees the messages returned 

SHOWMYACCOUNTS
Returns a list of all the customers accounts along with their current balance 
e.g. Main: 1000.0 

NEWACCOUNT <Name>
e.g. NEWACCOUNT Savings
Returns SUCCESS or FAIL

MOVE <Amount> <From> <To>
e.g. MOVE 100 Main Savings 
Returns SUCCESS or FAIL

PAY <Payee_Account_name> <Person/Company> <Receipient_Account_name> <Sotrt_code> <Ammount>
e.g. PAY Checking Bhagy Main EC12345 1500
Returns SUCCESS or FAIL

```

## Misc.

### Javadoc
```
> javadoc -d docs/newbank/client newbank.client 
> javadoc -d docs/newbank/server newbank.server 
```

## Contributing

Contributor info

## License

[MIT](https://choosealicense.com/licenses/mit/)