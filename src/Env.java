import java.util.HashMap;

public class Env {
    private HashMap<String, Object> table; // Table to store variables and their values
    public Env prev; // Reference to the previous scope

    // Constructor for creating a new scope, linked to a previous scope
    public Env(Env p) {
        this.prev = p;
        this.table = new HashMap<>();
    }

    // Method to add a new variable (or update an existing one) in the current scope
    public void Put(String name, Object value) {
        table.put(name, value);
    }

    // Method to find a variable's value, starting in the current scope and moving to outer scopes
    public Object Get(String name) {
        for (Env e = this; e != null; e = e.prev) {
            if (e.table.containsKey(name)) {
                return e.table.get(name);
            }
        }
        return null; // Variable not found in any scope
    }
//    public Object Get(String name) {
//        for (Env e = this; e != null; e = e.prev) {
//            if (e.table.containsKey(name)) {
//                System.out.println("Found " + name + " in scope with details: " + e.table.get(name));
//                return e.table.get(name);
//            }
//        }
//        System.out.println("Identifier not found in any scope: " + name);
//        return null; // Variable not found in any scope
//    }


}
