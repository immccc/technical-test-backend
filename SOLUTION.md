# Solution

## Description

As requested, two endpoints were added.

## Endpoints

### GET /wallets/:user_id
Retrieves a wallet information by its id.

Returns the wallet and the balance with HTTP status `200` if found, or `404` if not found. 

According to this approach, a wallet is identified by an `user_id`, assuming only one wallet can be assigned to a single user in the system. 

`balance` is stored and returned in monetary cent units as a straightforward way to avoid floating point number precision issues. 
For simplicity, currency is assumed that it's the same throughout all wallets, so no need to specify it for now.

### POST /wallets
Given a payload like 
```json
{
  "user_id": "an user id",
  "creditCard": "2222 2222 2222 2222",
  "topUpAmount": 1000, 
}
```
And given `topUpAmount` in monetary cent units, increases the wallet balance if possible.

Returns HTTP `200` if success, `404` if wallet does not exist, or `400` if amount can't be top-up because third party service denies operation.

#### Insights
- Sending a credit card openly is unsafe, and I would recommend to either tokenize the credit card or completely delegate payment to the 3rd party provider.
- This operation requires control of race conditions and transactionality. Followed approach is the simplest one, but has two things to consider:
  - Resource locking is on the wallet service, so only one simoultaneous top-up operation can be performed at the same time. However, under heavy amount of requests like this, would be great to allow both independent credit cards and wallets to increase balances in parallel.
  - Current solution is enough when a single instance of wallet application is running, but not enough when multiple replicas under a load balancer are available on a cloud cluster. That would require other techniques that we could discuss.

## Refactor
Code has been refactored from an Anemic Driven Design format to DDD, so that packages are based per domain entities:
- `wallet` package containing wallet endpoints and all related pieces to offer operations on wallets.
- `payments` package containing payment related content, including third party payment providers.
- `health` generic endpoint to check if app is up, moved to the root package.

Reasoning:
- Better interfacing and scoping, so that internal operations and elements can be package private and not exposed out of their context.
- Feels more natural to navigate through the codebase, as the reader looks for conceptual involved domain elements rather than mixing and scattering them in technical parts.
- Makes future refactors easier, e.g. if a new microservice for processing payments is created, we could just move the new `payments` package into the new application, instead of looking for the pieces scattered in different packages.
- Tests could make use of internal, package private components without the need to make them public and expanding visibility of internal parts.

## Testing
TDD has been applied for this solution. 
Two kind of tests are provided:
- Solitary unit tests to check if pieces comply with the functionality they have to provide, in isolation.
- Sociable tests to check endpoints from user perspective, and making real usage of a testing, temporary database and 3rd party payment provider in sandbox mode.

Also, solitary test for 3rd party provider service integration has been fixed. 
