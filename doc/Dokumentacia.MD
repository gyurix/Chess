# Zadanie

Vo vami zvolenom prostredí vytvorte databázovú aplikáciu, ktorá komplexne rieši päť
vami zadefinovaných scenárov (prípadov použitia) vo vami zvolenej doméne tak,
aby ste demonštrovali využitie relačnej databázy podľa pokynov uvedených nižšie.

Presný rozsah a konkretizáciu scenárov si dohodnete s Vašim cvičiacim na cvičení.
Projekt sa rieši vo dvojiciach, pričom sa očakáva, že na synchronizáciu práce so
spolužiakom / spolužiačkou použijete git. 

# Scenáre

## 1. Registračný a prihlasovací systém

V prvom scenáre sme implementovali systém na registrácie používateľov, ako aj na
prihlásovanie.

Pri spúsťanie programu používateľ potrebuje prihlásiť do systému s použitým nasledujúceho
formulára:

![Prihlasovací formulár](https://github.com/fiit-dbs-2019/dbs2019-project-assignment-barath-nestorenko/raw/master/doc/img/Login.png)

Keď ešte nemá účet, môže registrovať použitým nasledujúceho formulára:

![Registračný formulár](https://github.com/fiit-dbs-2019/dbs2019-project-assignment-barath-nestorenko/raw/master/doc/img/Register.png)

Pri prihlásení program kontroluje SHA-256 hash zadaného hesla. Keď to zhoduje s heslom v databáze,
tak sa ukáže dashboard, čo bude implementovaný v nasledujúcich scenároch.

Pri registrácii program uverí unikátnosť prihlasovacej mene, ako aj emailovej adrese.
Keď nenájde žiadne rovnaké údaje v databáze, len vtedy povolí registráciu.

## 2. Výber používateľa a nastavenie jeho role

## 3. Prehľadávateľny zoznam hier, kde používateľ participoval

## 4. Štatistiky hráča a administrácia líg

## 5. Priateľský systém


# Dátový model

## Logický

![Logický dátový model](https://github.com/fiit-dbs-2019/dbs2019-project-assignment-barath-nestorenko/raw/master/doc/img/LogModel.png)

## Fyzický

![Fyzický dátový model](https://github.com/fiit-dbs-2019/dbs2019-project-assignment-barath-nestorenko/raw/master/doc/img/FyzModel.png)

## Opis entitou

- **UserProfile:** Profil jedného používateľa systému
- **Rank:** Štatult používateľa systému, pri štatultoch sú priradené viacere práva
- **Permission:** Právo na vykonanie jednej funkcionalite systému
- **Friendship:** Prijateľské pripojenie medzi dvoch používateľou systému
- **Duration:** Čas začiatku a koncu nejakej akcie
- **LeaguePlayer:** Profil hráča v jednej lige
- **Game:** Informácie o jednu hru
- **League:** Liga v jednom majstrovstve
- **Championship:** Údaje o jednej majstrovstve

## Rozdiel medzi fyzickým a logickým dátovým modelom

Na umožnenie pripájanie user profilou k viacero líg ako aj jeden líg k viacermi user profilmi,
pridali sme entitu **LeaguePlayer**. Pridali sme aj unikátne kľúče ku každej entitám, ako aj
potrebné cudzie kľúče k entitám na zabezpečenie funkčného pripojenia medzi nimi.     
 
# Programovateľské prostredie
Program je implementovaný v programovacom jazyku **Java 8** a spracuje s
**Posgresql** databázou.

Na komunikáciu s databázou je použitá knižnica `postgresql 42.2.5`.

Ako externé pomocné knižnice používame `lombok 1.18.6` a `apache common-langs 3.8.1`

Na spracovávanie knižníc používame **Maven**.

# Hlavné časti programu

Každá hlavná časť programu sa nachádza v odlišnom balíku.
Každý balík sa začína s názvom `barnes.chess.` a potom máme názov funkcionalite

## db
Obsahuje triedy na prácu s databázou.
Máme tam nasledovné dôležité triedy:
- Trieda `DatabaseConfig`, ktorá obsahuje nastavenia databázového pripojenia;
- Trieda `DB`, ktorý funguje ako wrapper posgresql connectoru a slúži na
zabezpečenie pohodlnej asynchrónnej práci s databázou.

Odstatné triedy tam sa nachádzajú v podbalíku `entity` a slúžia
na implementáciu funkcionality, ktoré robíme s tabuľkami v databáze.
- Abstraktná trieda `AbstractEntity` poskytuje
pomocné funkcie na vygenerovanie príkazou na načítanie, modifikácie
a uloženie atribúty entitou do databáze.

## gui

Obsahuje triedy slúžiace na riešie funkcionality používateľského rozhrania.

Trieda `ResourceManager` slúži na spracovávanie externými zdrojmi, ktoré
sú použité na používateľskom rozhraní. Tieto zdroje sú napríklad ikony
jednotlivých možností aplikácie, ako aj hlavná ikona aplikácie.

Ostatné triedy sa nachádzajú v podbalíke `screens` a slúžia na
spracovávanie funkcionality jednej obrazovke, ako napr. registrácia,
prihlásenie, dashboard atď.

Abstraktná trieda `AbstractScreen` slúži na poskytnutie pomocných
funkcií na vytváranie **JavaFX** komponentov, na štruktúralizáciu ich spracovávanie
ako aj možnosť na vytváranie chybových okien.

## utils

Poskytuje pomocné triedy na riešenie univerzálnych problémov v softvéru.

Obsahujú napríklad hashovacie funkcie a funkcie na prepínanie medzi UI a asynchrónne vlákna.

Tieto triedy sú použité aj v `db` aj v `gui` časti programu.





  