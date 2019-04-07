# Zadanie

Vo vami zvolenom prostredí vytvorte databázovú aplikáciu, ktorá komplexne rieši päť
vami zadefinovaných scenárov (prípadov použitia) vo vami zvolenej doméne tak,
aby ste demonštrovali využitie relačnej databázy podľa pokynov uvedených nižšie.

Presný rozsah a konkretizáciu scenárov si dohodnete s Vašim cvičiacim na cvičení.
Projekt sa rieši vo dvojiciach, pričom sa očakáva, že na synchronizáciu práce so
spolužiakom / spolužiačkou použijete git. 

# Scenáre

## 1. Registračný a prihlasovací systém

V prvom scenári sme implementovali systém na registráciu používateľov, ako aj na
prihlásovanie.

Pri spúsťaní programu sa používateľ potrebuje prihlásiť do systému s použitým nasledujúceho
formulára:

![Prihlasovací formulár](https://github.com/fiit-dbs-2019/dbs2019-project-assignment-barath-nestorenko/raw/master/doc/img/Login.png)

Keď ešte nemá účet, môže registrovať použitím nasledovného formulára:

![Registračný formulár](https://github.com/fiit-dbs-2019/dbs2019-project-assignment-barath-nestorenko/raw/master/doc/img/Register.png)

Pri prihlásení program kontroluje SHA-256 hash zadaného hesla. Keď sa zhoduje s heslom v databáze,
ukáže sa dashboard, ktorý bude implementovaný v nasledujúcich scenároch.

Pri registrácii program uverí jedinečnosť prihlasovacieho mena, ako aj emailovej adresy.
Keď nenájde rovnaké údaje v databáze, povolí registráciu.

## 2. Štatistiky hráča

Druhý scenár umožní použivateľom prehľad svojej štatistiky. Sú 4 možne štatistiky: denná, mesiačná,
ročná a celková. Obyčajný použivateľ môže prehľadávať len vlastnú štatistiku, keď administrátor
má možnosť prehľadavať štatistiku všetkých hračov.

![DashBoard](https://github.com/fiit-dbs-2019/dbs2019-project-assignment-barath-nestorenko/raw/master/doc/img/ChessStats.png)

Zobrazíme nasledovné štatistiky:
- celkový počet hier (celé čislo)
- výhry (celé čislo)
- prehry (celé čislo)
- remízy (celé čislo)
- koeficient výhier / prehier (necelé čislo zaokrúhlené na 2 desatinné miesta)
- koeficient výhier / celkový počet hier (necelé čislo zaokruhlené na 2 desatinné miesta)

Administrátor taktiež má možnosť pozrieť všetkých použivatelov, ich rank a id, ktoré oni majú.

## 3. Prehľadávateľný zoznam hier, na ktorých používateľ participoval

Použivateľ bude moc pozrieť zoznam svojích hier, ktoré hrával. 
Existujú 4. zoznámy:
- denné partie - zoznám partií, ktoré hral v jeden deň
- týždenné partie - zoznám partií, ktoré hral v týždeň
- mesiačné partie - zoznám partií, ktoré hral v mesiac
- celkový zoznám partií

Každý zoznám bude obsahovať:
- použivatelské mena dvoch hračov, ktoré sa zučastnili v partie
- výsledok - 1-0, 1/2 alebo 0-1
- čas, kedy bola partia výtvorená

## 4. Výber používateľa a nastavenie jeho role

## 5. Priateľský systém


# Dátový model

## Logický

![Logický dátový model](https://github.com/fiit-dbs-2019/dbs2019-project-assignment-barath-nestorenko/raw/master/doc/img/LogModel.png)

## Fyzický

![Fyzický dátový model](https://github.com/fiit-dbs-2019/dbs2019-project-assignment-barath-nestorenko/raw/master/doc/img/FyzModel.png)

## Opis entitou

- **UserProfile:** Profil jedného používateľa systému
- **Rank:** Štatút používateľa systému, pri štatútoch sú priradené viaceré práva
- **Permission:** Právo na použitie jednej časti systému
- **Friendship:** Priateľské pripojenie medzi dvoma používateľmi systému
- **Duration:** Čas začiatku a konca nejakej akcie
- **LeaguePlayer:** Profil hráča v jednej lige
- **Game:** Informácie o jednej hre
- **League:** Liga v jednom majstrovstve
- **Championship:** Údaje o jednom majstrovstve

## Rozdiel medzi fyzickým a logickým dátovým modelom

Na umožnenie pripájania user profilov k viacerým ligám ako aj jednej ligy k viacerým user profilom,
sme pridali  entitu **LeaguePlayer**. Pridali sme aj unikátne kľúče ku každej entite, ako aj
potrebné cudzie kľúče k entitám na zabezpečenie funkčného pripojenia medzi nimi.     
 
# Programovateľské prostredie
Program je implementovaný v programovacom jazyku **Java 8** a pracuje s
**Posgresql** databázou.

Na komunikáciu s databázou je použitá knižnica `postgresql 42.2.5`.

Ako externé pomocné knižnice používame `lombok 1.18.6` a `apache common-langs 3.8.1`

Na spracovávanie knižníc používame **Maven**.

# Hlavné časti programu

Každá hlavná časť programu sa nachádza v odlišnom balíku.
Každý balík sa začína s názvom `barnes.chess.` a potom máme názov funkcionality

## db
Obsahuje triedy na prácu s databázou.
Máme tam nasledovné dôležité triedy:
- Trieda `DatabaseConfig`, ktorá obsahuje nastavenia databázového pripojenia;
- Trieda `DB`, ktorá funguje ako wrapper posgresql connectoru a slúži na
zabezpečenie pohodlnej asynchrónnej práce s databázou.

Ostatné triedy sa nachádzajú v podbalíku `entity` a slúžia
na implementáciu funkcionality, ktoré robíme s tabuľkami v databáze.
- Abstraktná trieda `AbstractEntity` poskytuje
pomocné funkcie na vygenerovanie príkazov na načítanie, modifikáciu
a uloženie atribútov entity do databázy.

## gui

Obsahuje triedy slúžiace na riešenie funkcionality používateľského rozhrania.

Trieda `ResourceManager` slúži na spracovávanie externými zdrojmi, ktoré
sú použité na používateľskom rozhraní. Tieto zdroje sú napríklad ikony
jednotlivých možností aplikácie, ako aj hlavná ikona aplikácie.

Ostatné triedy sa nachádzajú v podbalíku `screens` a slúžia na
spracovávanie funkcionality jednej obrazovky, ako napr. registrácia,
prihlásenie, dashboard atď.

Abstraktná trieda `AbstractScreen` slúži na poskytnutie pomocných
funkcií na vytváranie **JavaFX** komponentov, na štruktúralizáciu ich spracovávania
ako aj možnosť na vytváranie chybových okien.

## utils

Poskytuje pomocné triedy na riešenie univerzálnych problémov v softvéri.

Obsahujú napríklad hashovacie funkcie a funkcie na prepínanie medzi UI a asynchrónne vlákna.

Tieto triedy sú použité aj v `db` aj v `gui` časti programu.





  