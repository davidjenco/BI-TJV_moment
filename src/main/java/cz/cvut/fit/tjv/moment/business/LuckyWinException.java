package cz.cvut.fit.tjv.moment.business;

public class LuckyWinException extends Exception{
    public LuckyWinException(){
        super("Congrats, today is your lucky day!");
    }
}
