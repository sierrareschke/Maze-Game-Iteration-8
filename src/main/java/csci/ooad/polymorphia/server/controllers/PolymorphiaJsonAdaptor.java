package csci.ooad.polymorphia.server.controllers;

import csci.ooad.polymorphia.Polymorphia;

public class PolymorphiaJsonAdaptor {
    public final String name;
    public final int turn;
    // TODO: more to do here...

    public PolymorphiaJsonAdaptor(String gameName, Polymorphia polymorphia) {
        name = gameName;
        turn = polymorphia.getTurnNumber();

    }
}
