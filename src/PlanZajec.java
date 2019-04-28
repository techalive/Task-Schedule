import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * 
 * @author Adrian Czarniecki
 *
 */
public class PlanZajec {

	//klasa do przechowywania wpisu uzytkownika
	public static class Entry {
		//podane godziny i zajecia (przedmiot) sa przechowywane jako Stringi
		private String godziny;
		private String przedmiot;
		//gettery i settery do pol
		public String getGodziny() {
			return godziny;
		}
		public void setGodziny(String godziny) {
			this.godziny = godziny;
		}
		public String getPrzedmiot() {
			return przedmiot;
		}
		public void setPrzedmiot(String przedmiot) {
			this.przedmiot = przedmiot;
		}
		//konstruktor wpisu
		public Entry(String godziny, String przedmiot) {
			super();
			this.godziny = godziny;
			this.przedmiot = przedmiot;
		}
		//metoda konwertujaca wpis na Stringa
		public String toString() {
			return this.godziny + " " + this.przedmiot;
		}
	}

	//pomocnicza funkcja z krotka nazwa do wypisywania na konsole stringa ze znakiem konca linii
	public static void p(String s) {
		System.out.println(s);
	}
	
	//pomocnicza funkcja z krotka nazwa do wypisywania na konsole stringe bez znaku konca linii
	public static void pn(String s) {
		System.out.print(s);
	}
	
	//funkcja do sformatowanego wypisywania planu zajec
	public static void print(List<List<Entry>> planZajec) {
		//dlugosc pola w jednym wierszu pod dniem tygodnia
	    int FIELD_SIZE = 7;
	    //wypisania naglowka, do ladnego sformatowania uzywamy tabulacji
	    p("Pon\tWto\tSro\tCzw\tPia\tSob\tNie");
		p("-------\t-------\t-------\t-------\t-------\t-------\t-------");
		//lista kolumny bedzie zawierac Stringi do wypisania w poszczegolnych wierszach pod kazdym dniem
		//np. jesli mamy wpis Fizyka i Matematyka w poniedzialek, to zostanie on podzielony na fragmenty dlugosci FIELD_SIZE
		//i do listy o numerze 0 (poniedzialek) zostana wstawione fragmenty 'Fizyka ' 'i Matem' 'atyka'
		List<List<String>> kolumny = new LinkedList<List<String>>();
		//dodajemy 7 list po jednej dla kazdego dnia tygodnia
		for(int i = 0; i < 7; ++i) kolumny.add(new LinkedList<String>());
		//ta zmienna bedzie przechowywac maksymalna dlugosc wygenerowanych list z wierszami sposrod wszystkich list dla poszczegolnych dni tygodnia
		int maxLength = 0;
		//dla kazdego dnia tygodnia
		for(int i = 0; i < 7; ++i) {
			//musimy przejsc wszystkie wpisy w tym dniu zawarte w planZajec
			for(int j = 0; j<planZajec.get(i).size(); ++j) {
				//kazdy z tych wpisow skonwertowac na Stringa
				String doWypisania = planZajec.get(i).get(j).toString();
				//tutaj musimy dzielic napis doWypisania na fragmenty do wypisania dlugosci FIELD_SIZE
				int k = 0;
				while(true) {
					//warunek stopu petli - jesli juz przekroczylismy dlugosc napisu doWypisania i juz caly napis zostal podzielny na fragmenty
					if(k*FIELD_SIZE >= doWypisania.length()) break;
					//dla kolejnych k pobieramy fragment napisu reprezentujacego wpis w planie zajec
					//np. dla k = 2 fragment dlugosci 7 spomiedzy indeksow 2*7 i 3*7, trzeba uwazan zeby nie przekroczyc dlugosci tego napisu, stad funkcja Math.min
					String linia = doWypisania.substring(k*FIELD_SIZE, Math.min((k+1)*FIELD_SIZE, doWypisania.length()));
					//uzupelniamy nowa linie tabulacjami
					linia += "\t";
					//i dodajemy ja na liscie wierszy odpowiadajacej danemu dniowi
					kolumny.get(i).add(linia);
					//przejscie do nastepnego fragmentu dlugosci FIELD_SIZE
					++k;
				}
			}
			//aktualizujemy zmienna maxLength jesli utworzona lista dla tego dnia tygodnia jest najdluzsza z do tej pory stworzonych
			if(planZajec.get(i).size() > maxLength) {
				maxLength = kolumny.get(i).size();
			}
		}
		//wypisujemy przygotowane linie
		//dla kazdego wiersze - musimy zrobic petle po wierszach, ktorych jest maksymalnie maxLength
		for(int i = 0; i < maxLength; ++i) {
			//dla kazdego dnia
			for(int dzien = 0; dzien < 7; ++dzien) {
				//jesli lista wiersz dla tego dnia jest dluzsza od i tzn. ze jest cos do wypisania pod tym dniem
				if(kolumny.get(dzien).size() > i) {
					pn(kolumny.get(dzien).get(i));
				} else {
					//w przeciwnym wypadku wypisujemy pole zlozone z kropek
					pn(".......\t");
				}
			}
			p("");
		}
	}

	//funkcja do ladowania do planu zajec przykladowych danych
	public static void getSampleData(List<List<Entry>> planZajec) {
		//wstawiamy dwa wpisy do poniedzialku
		planZajec.get(0).add(new Entry("10-12", "zakupy w supermarkecie"));
		planZajec.get(0).add(new Entry("12-14", "Jêzyk polski i matematyka"));
		//jeden wpis do wtorku
		planZajec.get(1).add(new Entry("po poludniu", "Siatkowka"));
		//i trzy wpisy na czwartek
		planZajec.get(3).add(new Entry("8-10", "Jêzyk angielski"));
		planZajec.get(3).add(new Entry("10-12", "WF i Tenis ziemny"));
		planZajec.get(3).add(new Entry("12-14", "Fizyka i astronomia"));
	}
	
	//GLOWNA METODA
	public static void main(String[] args) throws FileNotFoundException {

		//stworznie listy list wpisow, to ma byc lista list, bo dla kazdego dnia mamy liste wpisow
		List<List<Entry>> planZajec = new LinkedList<List<Entry>>();
		//tworzymy liste dla kazdego dnia - jest ich w sumie 7 i wstawiamy ja do listy list
		for(int i = 0; i < 7; ++i) {
			planZajec.add(new LinkedList<Entry>());
		}
		
		//ladujemy przykladowe dane
		PlanZajec.getSampleData(planZajec);
		
		//tworzymy obiekt Scanner sluzacy do wczytywania z konsoli
		Scanner scr = new Scanner(System.in);
		
		//GLOWNA PETLA MENU
		while(true) {
			//wypisz menu
			p("0 - nadpisz dzien");
			p("1 - edytuj dzien");
			p("2 - wypisz");
			p("3 - zapisz do pliku");
			p("4 - wyszukaj przedmiot");
			p("5 - exit");
			pn("Opcja: ");
			//wczytaj opcje
			int opcja = scr.nextInt();
			scr.nextLine(); //wczytanie znaku nowej linii z bufora, bo nextInt zostawia enter w buforze
			//w zaleznosci od opcji wykonaj odpowiednia procedure obslugi opcji
			switch(opcja) {
			case 0 : PlanZajec.edytujDzien(planZajec, scr, true);
			break;
			case 1 : PlanZajec.edytujDzien(planZajec, scr, false);
			break;
			case 2 : PlanZajec.print(planZajec);
			break;
			case 3 : PlanZajec.zapiszDoPliku(planZajec);
			break;
			case 4 : PlanZajec.wyszukajPrzedmiot(planZajec, scr);
			break;
			default:
				//wyjscie z programu
				System.exit(0);
			}
		}
	}

	//procedura wyszukujaca wpisy zawierajace konkretny fragment podany przez uzytkownika
	private static void wyszukajPrzedmiot(List<List<Entry>> planZajec, Scanner scr) {
		pn("Podaj przedmiot: ");
		//wczytanie fragmentu do wyszukania
		String fragment = scr.nextLine();
		//przejscie w petli wszystkich dni
		for(int dzien = 0; dzien < 7; ++dzien) {
			p("Dzien " + (dzien+1));
			//dla kazdego dnia przechodzimy wszystkie wpisy
			for(int i = 0; i < planZajec.get(dzien).size(); ++i) {
				//kazdy konwertujemy na Stringa i sprawdzamy czy zawiera szukany fragment
				if(planZajec.get(dzien).get(i).toString().contains(fragment)) {
					//jesli tak, to go wypisujemy
					p(planZajec.get(dzien).get(i).toString());
				}
			}
		}
	}

	//metoda zapisujaca dane do pliku
	//rzuca wyjatek FileNotFoundException w przypadku bledow pracy z plikiem
	private static void zapiszDoPliku(List<List<Entry>> planZajec) throws FileNotFoundException {
		//do zapisywania do pliku sluzy obiekt klasy PrintWriter, ktoremu w konstruktorze przekazujemy plik
		PrintWriter pw = new PrintWriter(new File("out.txt"));
		//petla po wszysktich dniach
		for(int dzien = 0; dzien < 7; ++dzien) {
			//z PrintWritera korzystamy podobnie jak z System.out
			pw.println("Dzien " + (dzien+1));
			//petla po wszystkich wpisach w danym dniu
			for(int i = 0; i < planZajec.get(dzien).size(); ++i) {
				//zapisanie do pliku Stringa reprezentujacego wpis
				pw.println(planZajec.get(dzien).get(i).toString());
			}
		}
		//flush upewnia sie ze wszystko co zostalo wpisane jest wyslane do pliku 
		pw.flush();
		//zamkniecie PrintWritera
		pw.close();
		
	}

	//metoda do edycji planu zajec, parametr reset ustawiony na true oznacza wprowadzenie calego dnia od nowa
	private static void edytujDzien(List<List<Entry>> planZajec, Scanner scr, boolean reset) {
		//wypisanie menu
		p("1 Pon");
		p("2 Wto");
		p("3 Sro");
		p("4 Czw");
		p("5 Pia");
		p("6 Sob");
		p("7 Nie");
		pn("Dzien: ");
		//wczytanie numeru dnia
		int dzien = scr.nextInt();
		scr.nextLine();//wczytanie entera ktory zostaje w buforze wejscia konsoli
		if(dzien < 1 || dzien > 7) return; //jesli podany jest bledny numer dnia to konczymy funkcje
		//w zaleznosci od trybu resetujemy liste wpisow na dany dzien
		if(reset) planZajec.get(dzien-1).clear();
		//petla do podawania nowych wpisow
		do {
			pn("Godziny: ");
			//wczytanie godzin
			String godziny = scr.nextLine();
			pn("Zajecia: ");
			//wczytanie zajecia
			String zajecia = scr.nextLine();
			//dni sa numerowane od 0 wiec od wczytanego numeru dnia musimy odjac 1
			//dodanie do listy wpisow na dany dzien nowego wpisu z nowymi danymi
			planZajec.get(dzien-1).add(new Entry(godziny, zajecia));
			pn("Nastepny: ");
			//wczytanie odpowiedzi na pytanie czy kontynuowac
			String next = scr.nextLine();
			//kontynuujemy tylko jesli pierwsza litera to n
			//w przeciwnym wypadku przerywamy petle
			if(next.charAt(0) != 'n') break;
		} while(true);
	}

}
