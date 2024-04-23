public class Token {
    String lexeme;
    int line, column;

    public Token(String lexeme, int line, int column) {
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
    }
}


//public class Token
//{
//    public String lexeme;
//    public Token(String lexeme)
//    {
//        this.lexeme = lexeme;
//    }
//}
