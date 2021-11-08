package cz.cvut.fit.tjv.moment.business;

public class ElementAlreadyExistsException extends Exception{
    public ElementAlreadyExistsException(){
        super("Such element already exists.");
    }
}
