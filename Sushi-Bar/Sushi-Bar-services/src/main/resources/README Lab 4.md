# 🍣 Semantic Web Query App – Sushi Bar Lab

This is the result of the **Semantic Web** section of the lab assignment. It demonstrates how to load RDF data and perform SPARQL queries on sushi bar-related information using Java and Apache Jena.

---

## 📌 Project Summary

The app loads `.ttl` RDF data files representing:
- Sushi menu items (name, price, type)
- Customers and their reservations

Then it executes several SPARQL queries and prints the results in the console.

---

## ✅ Implemented Queries

1. **List all menu items** with names and prices
2. **Filter menu items** priced under 6.00
3. **List reservations** with customer name and timestamp
4. **Get drinks or sushi rolls** by type
5. **Count number of reservations** per customer

---

## ▶️ How to Run

### Requirements
- Java 17+
- Maven

### Steps
```bash
# Navigate to the main folder
cd Sushi-Bar-services

# Build and run
mvn clean install
mvn spring-boot:run
```

> Or run directly using:
```bash
mvn exec:java -Dexec.mainClass="org.example.sushibar.SemanticWebQueryApp"
```

---

## 📂 Key File

- `SemanticWebQueryApp.java` — contains all RDF loading and SPARQL query logic.
- RDF files are stored in: `src/main/resources/sushi-bar-semantic-web/`

---

## 📷 Example Output

```text
# Query 2: Menu items under 6.00
price: 5.0 | item: Cucumber Roll
price: 4.5 | item: Matcha Latte
...

# Query 5: Count reservations per customer
Ahmed Al-Mansouri → 1 reservation
Layla El-Fahd     → 1 reservation
...
```

---

## 🙋 Author

**Mohammed Altarhouni**  
Lab Assignment – Semantic Web (June 2025)  
Wrocław University of Science and Technology

---

## 📄 Notes

- No frontend or AOP features are included in this version.
- This is a standalone semantic web app for querying sushi bar RDF data.
