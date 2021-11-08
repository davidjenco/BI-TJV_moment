package cz.cvut.fit.tjv.moment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication //je to Configuration + něco navíc
//@ComponentScan("jméno balíčku") --> vyhledává rekurzivně v tom balíčku, který zadám ty componenty (kdybych balíček nezadal, tak je to od toho aktuálního)
//@EnableAutoConfiguration --> protože máme v závislostech TomCat (ten tam máme díky starter-web - je tam obsažen), tak na TomCat nasadí naše controllery
//taky umí rovnout pustit a propojit databázi...
//@SpringBootConfiguration --> hodí se na anotace, které přibudou s testama
public class RestMain {
    public static void main(String[] args) {
        SpringApplication.run(RestMain.class, args); //run způsobí, že se spustí webový server, který tam uvnitř je

    }
}
