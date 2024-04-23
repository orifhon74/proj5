//---------------------------------------------------------------------
// Name : Orifkhon Kilichev
// Email: omk5087@psu.edu
// Class: CMPSC 470-001, Spring 2024
//---------------------------------------------------------------------

import java.util.HashMap;

public class Env {
    HashMap<String, Object> table; // Table to store variables and their types and other attributes
    public Env prev; // Reference to the previous scope
    public int address = 1; // Address counter for variables

    // Constructor for creating a new scope, linked to a previous scope
    public Env(Env p) {
        this.prev = p;
        this.table = new HashMap<>();
    }

    // Method to add a new variable (or update an existing one) in the current scope
    public void Put(String name, Object value) {
        this.table.put(name, value);
        this.address++;
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
}

