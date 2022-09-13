package utils;

import exceptions.*;
import incapsulatedCommands.*;

import java.util.ArrayList;

public class Switch {
    private final Collectable collector;
    private final CollectionManager collectionManager;
    private ArrayList<Command> commands;

    public Switch(Collectable collector, CollectionManager collectionManager) {
        this.collector = collector;
        this.collectionManager = collectionManager;
    }
    public Switch(Collectable collector, CollectionManager collectionManager, ArrayList<Command> commands) {
        this.collector = collector;
        this.collectionManager = collectionManager;
        this.commands = commands;
        commands.clear();
    }


    public Command interpret(String s) throws Exception {
        Command cd = null;
        String param = null;
        if (s.contains(" ")) {
            param = s.split(" ", 2)[1];
        }
        String command = s.split(" ", 2)[0];

        switch (command) {
            case "exit":
                cd = new Exit(collectionManager);
                break;
            case "info":
                cd = new Info(collectionManager);
                break;
            case "show":
                cd = new Show(collectionManager);
                break;
            //без sout
            case "clear":
                cd = new Clear(collectionManager);
                break;
            case "reorder":
                cd = new Reorder(collectionManager);
                break;
            case "remove_last":
                cd = new RemoveLast(collectionManager);
                break;
            case "remove_by_id":
                if (Integer.parseInt(param) <= 0) throw new IncorrectValueException("Entered value must above zero");
                cd = new RemoveByID(collectionManager, Integer.parseInt(param));
                break;
            case "filter_contains_name":
                if ("".equals(param) || param == null) throw new NullArgumentException();
                cd = new FilterContainsName(collectionManager, param);
                break;
            case "remove_any_by_group_admin":
                cd = new RemoveAnyByGroupAdmin(collectionManager, collector.collectPerson(false));
                break;
            case "count_less_than_group_admin":
                cd = new CountLessThanGroupAdmin(collectionManager, collector.collectPerson(false));
                break;

            case "add":
                cd = new Add(collectionManager, collector.collectStudyGroup());
                break;
            case "update":
                if (Integer.parseInt(param) <= 0) throw new UnderZeroValueException();
                cd = new UpdateID(collectionManager, Integer.parseInt(param), collector.collectStudyGroup());
                break;
            case "remove_greater":
                cd = new RemoveGreater(collectionManager, collector.collectStudyGroup());
                break;
            case "execute_script":
                if (!collector.identify()) throw new AttemptOfRecursionException();
                cd = new ExecuteScript(collectionManager, param,commands);
                return cd;
            case "help":
                cd = new Help(collectionManager);
                break;
            default:
                throw new UnknownCommandException();
        }
        if (collector.identify()) System.out.print("\n");
        return cd;
    }
}