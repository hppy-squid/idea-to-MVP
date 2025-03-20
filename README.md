# idea-to-MVP

# Kaffe E-handel Backend

## Beskrivning
Detta är backend-delen av ett e-handelsprojekt för kaffe, byggt med Spring Boot. Systemet hanterar användare, produkter, kundvagnar och betalningar via Stripe. Frontend projektet hittar ni här https://github.com/hppy-squid/frontend-MVP

## Techstack
- Java 21
- Spring Boot 3.4.3
- Spring Security
- MySQL
- Stripe API
- JPA/Hibernate
- Lombok

## Säkerhet
- BCrypt lösenordskryptering
- Spring Security för autentisering
- CORS-konfiguration
- Stripe säker betalningshantering

## Databasmodell
Systemet använder följande huvudentiteter:
- User
- Product
- Cart
- CartItem
- Order
- OrderItem

Nedan finns en bild på databasrelationerna:
![image](https://github.com/user-attachments/assets/d5cc32bf-ea45-40f2-87db-2415439874cb)


## Funktioner 
- Användarhantering med autentisering
- Produkthantering med Stripe-integration
- Kundvagnshantering
- Betalningsprocessering via Stripe
- CORS-konfiguration för frontend-integration

# Installation

## Förutsättningar
- Java 21 eller senare
- MySQL
- Maven
- Stripe konto med API nycklar

## Steg för installation
1. Klona repot
```
git clone <https://github.com/hppy-squid/idea-to-MVP.git>
```
2. Konfigurera application.properties
```
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
stripe.secret.key=your_stripe_secret_key
```
3. Bygg projektet
```
mvn clean install
```
4. Kör applikationen
```
mvn spring-boot:run
```
