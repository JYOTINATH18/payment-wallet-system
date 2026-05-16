# Payment Wallet System

A production-style RESTful Payment Wallet API
built with Java Spring Boot.

## Tech Stack
- Java 18
- Spring Boot
- MySQL
- Apache Kafka
- JWT Authentication
- JUnit & Mockito
- Maven

## Features
- User registration & JWT login
- Wallet creation & balance management
- Fund transfer between wallets
- Transaction history
- Kafka event streaming
- 70%+ unit test coverage

## Setup
1. Clone the repository
2. Configure MySQL in application.properties
3. Run WalletApplication.java

## API Endpoints
- POST /api/auth/register
- POST /api/auth/login
- GET  /api/wallet/balance
- POST /api/wallet/transfer
- GET  /api/transactions