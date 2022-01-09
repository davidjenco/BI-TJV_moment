# TJV - moment

Semestral project for BI-TJV class

<h3>1. kontrolní bod:</h2>
<p>- Business vrstva není implementována</p>
<p>- Logika IDs se bude ještě měnit, až se nasadí databáze</p>
<p>- REST API jsem testoval v Postman, zde přikládám link na mou Collection:</p>
<p>https://www.getpostman.com/collections/d083d8d09fd5b5c49c8d</p>
<br></br>

<h3>2. kontrolní bod:</h2>
<p>- Business vrstva je implementována</p>
<p>- Složitější dotaz v OrderJpaRepository</p>
<p>- Je potřeba spustit postgres databázi na portu 5432, heslo: "password" (usename: "postgres")</p>
<p>  nebo pomocí docker-compose up</p></p>
<p>https://www.getpostman.com/collections/d083d8d09fd5b5c49c8d</p>
<br></br>

<h3>3. kontrolní bod:</h2>
<p>- Napsané testy</p>
<p>- Funkční CI a docker compose</p>
<p>- Změna v entitách (m:n vazba není dekomponovaná manuálně)</p>
<p>- Přikládám i krátký insert script pro databázi, který jsem často používal, abych nemusel dokola vytvářet pár počátečních entit</p>
<p>- (Kolekce v Postman se tedy také měnila, kdyby byla náhodou potřeba)</p>
<p>https://www.getpostman.com/collections/51950f63c5e2529a5cfd</p>
<p>- Klient mé semestrální práci je v repozitáři "TJV - moment_client", do kterého jsem Vás taky přidal s rolí Reporter</p>
<br></br>

<h3>Příběh k semestrálce:</h2>
<p>Jedná se o backend k vytváření objednávek v nějaké nejmenované kavárně. Můžeme mít více poboček, každá pobočka pod sebou eviduje své objednávky, kde každá objednávka obsahuje jednu či více položek z nabídky. Každá pobočka má své "šťastné číslo", pokud je při uzavření objednávky celková cena objednávky rovna tomuto číslu, zákazník má objednávku zdarma a útrata jde na účet podniku. Pokud se toto stane, pobočce je vygenerované nové "šťastné číslo".</p>


![alt text](img/diagram.png "Model diagram")