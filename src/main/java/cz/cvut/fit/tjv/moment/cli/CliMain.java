package cz.cvut.fit.tjv.moment.cli;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CliMain {
    public static void main(String[] args) {

        //ApplicationContext ctx = new AnnotationConfigApplicationContext(BeansConfiguration.class); //mohl bych tady předat toto, v tom případě by ta definice here přebyla ty ostatní
        ApplicationContext ctx = new AnnotationConfigApplicationContext("cz.cvut.fit.tjv.moment"); //takhle mu řeknu odkud tahat tu konfiguraci
        //mohl bych tam postupně vyjmenovávat ty moje třídy, které mají @Component u sebe, ale takhle on si projde ten balíček do hloubky a najde si to sám



    }
}
