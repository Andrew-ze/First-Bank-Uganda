# First Bank Uganda — Account Opening App

A JavaFX desktop application for opening bank accounts at First Bank Uganda. Built as a project demonstrating Object-Oriented Programming principles including inheritance, polymorphism, and abstraction.

---

## Features

- **5 Account Types** — Savings, Current, Fixed Deposit, Student, and Joint accounts, each with their own minimum deposit and age eligibility rules
- **Full form validation** — NIN format (CM/CF + 9 digits + 3 letters), Ugandan phone numbers, email confirmation, PIN strength, date of birth with leap-year logic
- **Dynamic UI** — Date of birth combo boxes update days automatically based on month/year selection; Joint account second-holder NIN field appears only when needed
- **MS Access database** — Account records saved to `firstbank.accdb` via UCanAccess JDBC driver; table created automatically on first run
- **Account number generation** — Format: `BRANCH-YEAR-XXXXXX` (e.g. `KLA-2026-000001`), sequential per branch per year
- **Success/error dialogs** — Popup confirmation on success; inline + popup error listing on validation failure

---

## Project Structure

'''
FirstBankUG/
├── src/
│   ├── Main.java                        ← Application entry point
│   ├── model/
│   │   ├── Account.java                 ← Abstract base class
│   │   ├── SavingsAccount.java
│   │   ├── CurrentAccount.java
│   │   ├── FixedDepositAccount.java
│   │   ├── StudentAccount.java
│   │   └── JointAccount.java
│   ├── validation/
│   │   ├── NameValidator.java
│   │   ├── NINValidator.java
│   │   ├── EmailValidator.java
│   │   ├── PhoneValidator.java
│   │   ├── PINValidator.java
│   │   └── DepositValidator.java
│   ├── service/
│   │   ├── FormValidator.java
│   │   ├── AccountNumberGenerator.java
│   │   └── DOBHelper.java
│   ├── ui/
│   │   ├── MainFormWindow.java
│   │   ├── PersonalInfoPanel.java
│   │   ├── AccountPanel.java
│   │   ├── SummaryPanel.java
│   │   ├── ValidationErrorPanel.java
│   │   └── AccountListWindow.java
│   └── db/
│       ├── DatabaseManager.java
│       └── AccountRepository.java
├── lib/                                 ← UCanAccess JARs (see setup)
├── resources/
│   └── firstbank.accdb                  ← MS Access database (auto-created)
└── .vscode/
    ├── settings.json
    └── launch.json
'''

---

## OOP Design

The application is built around an abstract `Account` class with 5 concrete subclasses:

| Class | Min Deposit | Age Range | Extra Rule |
|---|---|---|---|
| `SavingsAccount` | UGX 50,000 | 18–75 | — |
| `CurrentAccount` | UGX 200,000 | 18–75 | — |
| `FixedDepositAccount` | UGX 1,000,000 | 18–75 | — |
| `StudentAccount` | UGX 10,000 | 18–25 | Age capped at 25 |
| `JointAccount` | UGX 100,000 | 18–75 | Requires 2nd holder NIN |

Each subclass overrides `getMinimumDeposit()`, `getMinimumAge()`, `getMaximumAge()`, and `getExtraValidationError()` — enabling the form validator to call these methods polymorphically without knowing which account type is selected.

---

## Setup & Installation

### Prerequisites

- **JDK 17 or higher** — [Download here](https://adoptium.net/)
- **JavaFX SDK 26** — [Download here](https://gluonhq.com/products/javafx/)
- **UCanAccess JARs** — [Download here](https://sourceforge.net/projects/ucanaccess/files/)
- **VS Code** with [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)

### Step 1 — Clone the repository

```bash
git clone https://github.com/Andrew-ze/First-Bank-Uganda.git
cd First-Bank-Uganda
```

### Step 2 — Add UCanAccess JARs to `lib/`

Download and place these 5 JARs into the `lib/` folder:

'''
lib/
├── ucanaccess-5.0.1.jar
├── hsqldb-2.5.0.jar
├── jackcess-3.0.1.jar
├── commons-lang3-3.8.1.jar
└── commons-logging-1.2.jar
'''

### Step 3 — Configure JavaFX path

Open `.vscode/settings.json` and `.vscode/launch.json`. Update the JavaFX SDK path to match where you extracted it on your machine:

```json
"C:/path/to/your/javafx-sdk-26/lib"
'''

### Step 4 — Run the app

Open the project in VS Code, then run the `Main` class.


The `resources/firstbank.accdb` database file will be created automatically on first run.

---

## Technologies Used

- **Java 17+**
- **JavaFX 26** — UI framework
- **MS Access** — Database (`.accdb`)
- **UCanAccess 5.0.1** — JDBC driver for MS Access
- **VS Code** — IDE

---
'''
Author:
ATEGEKA ANDREW
