# SimpleBankingSystem

Project on hyperskill: https://hyperskill.org/projects/93?track=1

In this project, you will find out how the banking system works and learn about SQL. 
We'll also see how Luhn algorithm can help us avoid mistakes when entering the card number. 

In our banking system, the customer account number can be any, but it should be unique. And the whole card number should be 16-digit length.

The very last digit of a credit card is the check digit or checksum. It is used to validate the credit card number using the Luhn algorithm.

 - Stage 1
You should allow customers to create a new account in our banking system.

Once the program starts, you should print the menu:

1. Create an account
2. Log into account
0. Exit
If the customer chooses ‘Create an account’, you should generate a new card number which satisfies all the conditions described above. 
Then you should generate a PIN code that belongs to the generated card number. 
A PIN code is a sequence of any 4 digits. PIN should be generated in a range from 0000 to 9999.

If the customer chooses ‘Log into account’, you should ask them to enter their card information. 
After all information is entered correctly, you should allow the user to check the account balance; right after creating the account, the balance should be 0. 
It should also be possible to log out of the account and exit the program.

 - Stage 2
 In this stage, we will find out what the purpose of the checksum is and what the Luhn algorithm is used for.

The main purpose of the check digit is to verify that the card number is valid.

The Luhn algorithm is used to validate a credit card number or other identifying numbers, such as Social Security. 
Luhn algorithm, also called the Luhn formula or modulus 10, 
checks the sum of the digits in the card number and checks whether the sum matches the expected result or if there is an error in the number sequence. 
After working through the algorithm, 
if the total modulus 10 equals zero, then the number is valid according to the Luhn method.

 You need to change the credit card generation algorithm so that they pass the Luhn algorithm.
 
 Objectives
You should allow customers to create a new account in our banking system.

Once the program starts you should print the menu:

 - Objectives

1. Create an account
2. Log into the account
0. Exit

If the customer chooses ‘Create an account’, you should generate a new card number that satisfies all the conditions described above. Then you should generate a PIN code that belongs to the generated card number. PIN is a sequence of 4 digits; it should be generated in the range from 0000 to 9999.

If the customer chooses ‘Log into account’, you should ask to enter card information.

 - Stage 3
 In this stage, create a database with a table titled card. It should have the following columns:

 - id INTEGER
 - number TEXT
 - pin TEXT
 - balance INTEGER DEFAULT 0
 
Also, in this stage, you should read the database file name from the command line argument. Filename should be passed to the program using -fileName argument, for example, -fileName db.s3db.

Pay attention: your database file should be created when the program starts, if it hasn't yet been created. And all created cards should be stored in the database from now.

 - Stage 4
 Now your menu should look like this:

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
If the user asks for Balance, you should read the balance of the account from the database and output it into the console.

Add income item should allow us to deposit money to the account.

Do transfer item should allow transferring money to another account. You should handle the following errors:

 - If the user tries to transfer more money than he/she has, output: Not enough money!
 - If the user tries to transfer money to the same account, output the following message: You can't transfer money to the same account!
 - If the receiver's card number doesn’t pass the Luhn algorithm, you should output: Probably you made a mistake in the card number. Please try again!
 - If the receiver's card number doesn’t exist, you should output: Such a card does not exist.
 - If there is no error, ask the user how much money they want to transfer and make the transaction.
 - If the user chooses the Close an account item, you should delete that account from the database.
 
 Examples
The symbol > represents the user input. Notice that it's not a part of the input.

Example 1:

1. Create an account
2. Log into account
0. Exit
>1

Your card has been created
Your card number:
4000009455296122
Your card PIN:
1961

1. Create an account
2. Log into account
0. Exit
>1

Your card has been created
Your card number:
4000003305160034
Your card PIN:
5639

1. Create an account
2. Log into account
0. Exit
>2

Enter your card number:
>4000009455296122
Enter your PIN:
>1961

You have successfully logged in!

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>2

Enter income:
>10000
Income was added!

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>1

Balance: 10000

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>3

Transfer
Enter card number:
>4000003305160035
Probably you made a mistake in the card number. Please try again!

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>3

Transfer
Enter card number:
>4000003305061034
Such a card does not exist.

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>3

Transfer
Enter card number:
>4000003305160034
Enter how much money you want to transfer:
>15000
Not enough money!

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>3

Transfer
Enter card number:
>4000003305160034
Enter how much money you want to transfer:
>5000
Success!

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>1

Balance: 5000

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit

>0
Bye!
Example 2:

1. Create an account
2. Log into account
0. Exit
>1

Your card has been created
Your card number:
4000007916053702
Your card PIN:
6263

1. Create an account
2. Log into account
0. Exit
>2

Enter your card number:
>4000007916053702
Enter your PIN:
>6263

You have successfully logged in!

1. Balance
2. Add income
3. Do transfer
4. Close account
5. Log out
0. Exit
>4

The account has been closed!

1. Create an account
2. Log into account
0. Exit
>2

Enter your card number:
>4000007916053702
Enter your PIN:
>6263

Wrong card number or PIN!

1. Create an account
2. Log into account
0. Exit
>0

Bye!
