CREATE DATABASE `loan`;

CREATE TABLE `loan`.`account_balances` (
  `account` varchar(25) NOT NULL,
  `amount` decimal(14,2) NOT NULL,
  PRIMARY KEY (account)
);
