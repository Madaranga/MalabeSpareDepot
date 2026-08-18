# Malabe Spare Depot

A JavaFX desktop inventory and point-of-sale management system for a three-wheeler (auto-rickshaw) spare-parts retail business. It ingests legacy, inconsistently formatted stock and dealer data, and provides a multi-screen desktop UI for browsing, searching, selling, and auditing that inventory.

## Features

- **Dashboard** — live search and category filtering over the parts catalogue, running totals, one-click "Add to Cart".
- **Inventory Management** — full CRUD on parts, including product images, duplicate part-code validation, and numeric field validation.
- **Sales / Checkout** — shopping cart with live stock checks, dealer selection, and automatic discount calculation:
  - 5% bulk discount on any cart line with 3+ units of the same part.
  - Further 10% discount when the cart contains both an Engine-category and an Electrical-category item.
- **Dealer Management** — searchable, sortable table of all dealers.
- **Low Stock Alerts** — flags parts at or below a 5-unit threshold as `LOW STOCK` or `OUT OF STOCK`.
- **Audit Log** — every completed sale is appended to a plain-text audit log and viewable from within the app.
- **Resilient legacy-data parsing** — tolerates mixed delimiters (`,` `;` `|`), inconsistent whitespace, currency-prefixed prices, and missing optional fields; malformed rows are skipped individually rather than aborting the whole file load.

## Tech Stack

| Component | Technology |
|---|---|
| Language | Java 21 |
| UI | JavaFX 21.0.6 (FXML + CSS) |
| Build | Apache Maven (Maven Wrapper included) |
| Testing | JUnit 5 (Jupiter) + Maven Surefire |

## Project Structure

```
src/main/java/com/example/malabesparedepot/
├── App.java                 # JavaFX application entry point
├── model/                   # Part, Dealer, CartItem
├── data/                    # ApplicationData (singleton shared state)
├── util/                    # DataParser, CustomSorter, SearchEngine,
│                             # DealerSelector, PriceCalculator, ImageUtil, LoggerUtil
├── service/                 # CartService, InventoryService, PartValidator
└── controller/               # NavigationController + one controller per screen

src/main/resources/
├── Fxml/                    # dashboard, inventory, sales, dealers, low_stock, audit_log
├── Styles/style.css
└── Images/

src/test/java/...            # JUnit 5 test suite (mirrors main package structure)
```

## Getting Started

### Prerequisites

- JDK 21+
- Maven 3.8+ (or use the bundled wrapper — no local Maven install required)

### Run the application

```bash
./mvnw javafx:run
```

_(Windows: `mvnw.cmd javafx:run`)_

### Run the tests

```bash
./mvnw test
```

### Build a package

```bash
./mvnw package
```

## Data Files

On startup, the app loads inventory and dealer data from plain-text files in the project root:

- `inventory_legacy.txt` — part code, name, brand, price, quantity, category, date added, image path
- `dealers_legacy.txt` — dealer ID, name, phone number, location

Sales actions are appended to `audit_log.txt`.

> **Note:** Inventory and dealer edits made while the app is running are held in memory for that session only — they are not currently written back to the source `.txt` files. See [Future Improvements](#future-improvements) below.

## Future Improvements

- Persist inventory/dealer edits back to disk (or migrate to a lightweight embedded database).
- Wire the existing `SearchEngine` and `service` package (`CartService`, `InventoryService`, `PartValidator`) into the controllers, which currently use separate inline logic for the same purpose.
- Add authentication / user roles.
- Add automated UI testing (e.g. TestFX).
- Export receipts/invoices per transaction.

## License

_Add your license here (e.g. MIT)._
