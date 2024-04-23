//---------------------------------------------------------------------
// Name : Orifkhon Kilichev
// Email: omk5087@psu.edu
// Class: CMPSC 470-001, Spring 2024
//---------------------------------------------------------------------

import java.util.*;
import java.util.HashMap;

@SuppressWarnings("unchecked")
public class ParserImpl
{
    public static Boolean _debug = true;
    void Debug(String message)
    {
        if(_debug)
            System.out.println(message);
    }

    // This is for chained symbol table.
    // This includes the global scope only at this moment.
    Env env = new Env(null);
    // this stores the root of parse tree, which will be used to print parse tree and run the parse tree
    ParseTree.Program parsetree_program = null;

    private void nextFreeAddress() {
        Env temp = new Env(this.env);
        this.env = temp;
    }

    Object program____decllist(Object s1) throws Exception
    {
        // 1. check if decllist has main function having no parameters and returns int type
        // 2. assign the root, whose type is ParseTree.Program, to parsetree_program
        ArrayList<ParseTree.FuncDecl> decllist = (ArrayList<ParseTree.FuncDecl>)s1;
        parsetree_program = new ParseTree.Program(decllist);

        // check if the program has one main function that returns num value and has no parameters
        boolean mainFound = false;
        for (ParseTree.FuncDecl decl : decllist) {
            if (decl.ident.equals("main")) {
                if (decl.params.size() == 0 && decl.rettype.typename.equals("num")) {
                    mainFound = true;
                    break;
                }
            }
        }
        if (!mainFound) {
            throw new Exception("The program must have one main function that returns num value and has no parameters");
        }
        return parsetree_program;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Object decllist____decllist_decl(Object s1, Object s2) throws Exception
    {
        ArrayList<ParseTree.FuncDecl> decllist = (ArrayList<ParseTree.FuncDecl>)s1;
        ParseTree.FuncDecl                decl = (ParseTree.FuncDecl           )s2;
        decllist.add(decl);
        return decllist;
    }
    Object decllist____eps() throws Exception
    {
        return new ArrayList<ParseTree.FuncDecl>();
    }
    Object decl____funcdecl(Object s1) throws Exception
    {
        return s1;
    }
    Object primtype____NUM(Object s1) throws Exception
    {
        Token num = (Token)s1;
        ParseTree.TypeSpec typespec = new ParseTree.TypeSpec(num.lexeme);
//        ParseTree.TypeSpec typespec = new ParseTree.TypeSpec("num");
        return typespec;
    }
    Object primtype____BOOL(Object s1) throws Exception
    {
        Token bool = (Token)s1;
        ParseTree.TypeSpec typespec = new ParseTree.TypeSpec(bool.lexeme);
//        ParseTree.TypeSpec typespec = new ParseTree.TypeSpec("bool");
        return typespec;
    }
    Object typespec____primtype(Object s1)
    {
        ParseTree.TypeSpec primtype = (ParseTree.TypeSpec)s1;
        return primtype;
    }
    Object typespec____primtype_LBRACKET_RBRACKET(Object s1, Object s2, Object s3)
    {
        ParseTree.TypeSpec primtype = (ParseTree.TypeSpec)s1;
        return new ParseTree.TypeSpec(primtype.typename + "[]");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Object fundecl____FUNC_IDENT_TYPEOF_typespec_LPAREN_params_RPAREN_BEGIN_localdecls_10X_stmtlist_END(Object s1, Object s2, Object s3, Object s4, Object s5, Object s6, Object s7, Object s8, Object s9) throws Exception
    {
        // 1. add function_type_info object (name, return type, params) into the global scope of env
        // 2. create a new symbol table on top of env
        // 3. add parameters into top-local scope of env
        // 4. etc.

        Token                            id         = (Token                           )s2;
        ParseTree.TypeSpec               rettype    = (ParseTree.TypeSpec              )s4;
        ArrayList<ParseTree.Param>       params     = (ArrayList<ParseTree.Param>      )s6;

        ParseTree.FuncDecl funcdecl = new ParseTree.FuncDecl(id.lexeme, rettype, params, null, null);
        funcdecl.info.functionName = id;
        funcdecl.info.returnType = rettype;
        funcdecl.info.params = params;

        env.Put(id.lexeme, funcdecl.info);
        nextFreeAddress();
        for (ParseTree.Param param : params) {
            env.Put(param.ident, param.typespec.typename);
        }

        return null;
    }
    Object fundecl____FUNC_IDENT_TYPEOF_typespec_LPAREN_params_RPAREN_BEGIN_localdecls_X10_stmtlist_END(Object s1, Object s2, Object s3, Object s4, Object s5, Object s6, Object s7, Object s8, Object s9, Object s10, Object s11, Object s12) throws Exception
    {
        // 1. check if this function has at least one return type
        // 2. etc.
        // 3. create and return funcdecl node
        Token                            id         = (Token                           )s2;
        ParseTree.TypeSpec               rettype    = (ParseTree.TypeSpec              )s4;
        ArrayList<ParseTree.Param>       params     = (ArrayList<ParseTree.Param>      )s6;
        ArrayList<ParseTree.LocalDecl>   localdecls = (ArrayList<ParseTree.LocalDecl>  )s9;
        ArrayList<ParseTree.Stmt>        stmtlist   = (ArrayList<ParseTree.Stmt>       )s11;
        Token                            end        = (Token                           )s12;

//        // Check if the function has at least one return statement
//        boolean hasReturn = false;
//        for (ParseTree.Stmt stmt : stmtlist) {
//            if (stmt instanceof ParseTree.ReturnStmt) {
//                hasReturn = true;
//                break;
//            }
//        }
//        if (!hasReturn) {
//            throw new Exception("Function '" + id.lexeme + "' must have at least one return statement");
//        }
        ParseTree.FuncDecl funcdecl = new ParseTree.FuncDecl(id.lexeme, rettype, params, localdecls, stmtlist);
        return funcdecl;
    }

    Object params____paramlist(Object s1) throws Exception
    {
//        return s1;
        return (List<ParseTree.Param>) s1;
    }

    Object params____eps() throws Exception
    {
        return new ArrayList<>();
    }

    Object paramlist____paramlist_COMMA_param(Object s1, Object s2, Object s3) throws Exception
    {
        ArrayList<ParseTree.Param> param_list = (ArrayList<ParseTree.Param>)s1;
        ParseTree.Param            param      = (ParseTree.Param           )s3;
        param_list.add(param);
        return param_list;
    }

    Object paramlist____param(Object s1) throws Exception
    {
        ArrayList<ParseTree.Param> paramlist = new ArrayList<>();
        ParseTree.Param param = (ParseTree.Param) s1;
        paramlist.add(param);
        return paramlist;
    }

    Object param____IDENT_TYPEOF_typespec(Object s1, Object s2, Object s3) throws Exception
    {
        Token              id       = (Token             )s1;
        ParseTree.TypeSpec typespec = (ParseTree.TypeSpec)s3;
        return new ParseTree.Param(id.lexeme, typespec);
    }

    Object stmtlist____stmtlist_stmt(Object s1, Object s2) throws Exception
    {
        ArrayList<ParseTree.Stmt> stmtlist = (ArrayList<ParseTree.Stmt>)s1;
        ParseTree.Stmt            stmt     = (ParseTree.Stmt           )s2;
        stmtlist.add(stmt);
        return stmtlist;
    }
    Object stmtlist____eps() throws Exception
    {
        return new ArrayList<>();
    }

    Object stmt____assignstmt  (Object s1) throws Exception
    {
//        assert(s1 instanceof ParseTree.AssignStmt);
//        return s1;
        try {
            ParseTree.AssignStmt stmt = (ParseTree.AssignStmt) s1;
            return stmt;
        } catch(ClassCastException e) {
            ParseTree.AssignStmtForArray stmt = (ParseTree.AssignStmtForArray) s1;
            return stmt;
        }
    }
    Object stmt____returnstmt  (Object s1) throws Exception
    {
//        assert(s1 instanceof ParseTree.ReturnStmt);
//        return s1;
        ParseTree.ReturnStmt stmt = (ParseTree.ReturnStmt) s1;
        return stmt;
    }
    Object stmt____ifstmt      (Object s1) throws Exception
    {
//        assert(s1 instanceof ParseTree.IfStmt);
//        return s1;
        ParseTree.IfStmt stmt = (ParseTree.IfStmt) s1;
        return stmt;
    }
    Object stmt____whilestmt   (Object s1) throws Exception
    {
//        assert(s1 instanceof ParseTree.WhileStmt);
//        return s1;
        ParseTree.WhileStmt stmt = (ParseTree.WhileStmt) s1;
        return stmt;
    }
    Object stmt____compoundstmt   (Object s1) throws Exception
    {
//        assert(s1 instanceof ParseTree.CompoundStmt);
//        return s1;
        ParseTree.CompoundStmt stmt = (ParseTree.CompoundStmt) s1;
        return stmt;
    }
    Object stmt____printstmt    (Object s1) throws Exception
    {
//        assert(s1 instanceof ParseTree.PrintStmt);
//        return s1;
        ParseTree.PrintStmt stmt = (ParseTree.PrintStmt) s1;
        return stmt;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Object assignstmt____IDENT_ASSIGN_expr_SEMI(Object s1, Object s2, Object s3, Object s4) throws Exception
    {
        // 1. check if ident.value_type matches with expr.value_type
        // 2. etc.
        // e. create and return node
        Token          id     = (Token         )s1;
        Token          assign = (Token         )s2;
        ParseTree.Expr expr   = (ParseTree.Expr)s3;

//        {
//            // check if expr.type matches with id_type
//            if(id_type.equals("num")
//                    && (expr instanceof ParseTree.ExprNumLit)
//            )
//            {} // ok
//            else if(id_type.equals("num")
//                    && (expr instanceof ParseTree.ExprFuncCall)
//                    && (env.Get(((ParseTree.ExprFuncCall)expr).ident).equals("num()"))
//            ) {} // ok
//            else if (id_type == null)
//            {
//                throw new Exception("semantic error");
//            }
//        }

        ParseTree.AssignStmt stmt = new ParseTree.AssignStmt(id.lexeme, expr);
        stmt.ident_reladdr = env.address;

        // check if expr is an identifier and if it defined
        if(expr instanceof ParseTree.ExprIdent && env.Get(((ParseTree.ExprIdent) expr).ident) == null)
            throw new Exception("Variable " + ((ParseTree.ExprIdent) expr).ident + " is not defined.");
        Object id_type = env.Get(id.lexeme);
        Object expr_type = determineType(expr);

        if(!id_type.equals(expr_type))
            throw new Exception("Variable " + id.lexeme + " should have " + id_type + " value, instead of " + expr_type + " value.");
        return stmt;
    }

    Object assignstmt____IDENT_LBRACKET_expr_RBRACKET_ASSIGN_expr_SEMI(Object s1, Object s2, Object s3, Object s4, Object s5, Object s6, Object s7) throws Exception
    {
        // 1. check if ident.value_type matches with expr1.value_type
        // 2. check if expr2.value_type is int
        // 3. etc.
        // 4. create and return node
        Token          id     = (Token         )s1;
        Token          lbrack = (Token         )s2;
        ParseTree.Expr expr1  = (ParseTree.Expr)s3;
        Token          rbrack = (Token         )s4;
        Token          assign = (Token         )s5;
        ParseTree.Expr expr2  = (ParseTree.Expr)s6;
//        Object id_type = env.Get(id.lexeme);
//        {
//            // check if expr1.type matches with id_type
//            if(id_type.equals("num[]")
//                    && (expr1 instanceof ParseTree.ExprFuncCall)
//                    && (env.Get(((ParseTree.ExprFuncCall)expr1).ident).equals("num()"))
//            )
//            {} // ok
//            else if (id_type == null)
//            {
//                throw new Exception("semantic error");
//            }
//            // check if expr2.type matches with num
//            if(id_type.equals("num[]")
//                    && (expr2 instanceof ParseTree.ExprNumLit)
//            )
//            {} // ok
//            else if (id_type == null)
//            {
//                throw new Exception("semantic error");
//            }
//        }
        ParseTree.AssignStmtForArray stmt = new ParseTree.AssignStmtForArray(id.lexeme,expr1, expr2);

        // check if index value is a num or an ident
        if(expr1 instanceof ParseTree.ExprIdent && !env.Get(((ParseTree.ExprIdent)expr1).ident).equals("num"))
            throw new Exception("Array index must be a num value.");
        else if(!(expr1 instanceof ParseTree.ExprIdent) && !determineType(expr1).equals("num"))
            throw new Exception("Array index must be a num value.");

        // check if element value is the same type as the array
        String id_type = (String) env.Get(id.lexeme);
        String expr_type = determineType(expr2);
        if(!id_type.contains(expr_type))
            throw new Exception("Element of array " + id.lexeme + " should have "
                    + id_type.substring(0, id_type.length() - 2) + " value, instead of "
                    + expr_type + " value.");
        return stmt;
    }
    Object returnstmt____RETURN_expr_SEMI(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr.value_type matches with the current function return type
        // 2. etc.
        // 3. create and return node
        ParseTree.Expr expr = (ParseTree.Expr)s2;
        return new ParseTree.ReturnStmt(expr);
    }
    Object ifstmt____IF_expr_THEN_stmtlist_ELSE_stmtlist_END(Object s1, Object s2, Object s3, Object s4, Object s5, Object s6, Object s7) throws Exception
    {
        // 1. check if expr.value_type is bool
        // 2. etc.
        // 3. create and return node
        ParseTree.Expr expr = (ParseTree.Expr)s2;
        ArrayList<ParseTree.Stmt> stmtlist1 = (ArrayList<ParseTree.Stmt>)s4;
        ArrayList<ParseTree.Stmt> stmtlist2 = (ArrayList<ParseTree.Stmt>)s6;
        return new ParseTree.IfStmt(expr, stmtlist1, stmtlist2);
    }
    Object whilestmt____WHILE_expr_BEGIN_stmtlist_END(Object s1, Object s2, Object s3, Object s4, Object s5) throws Exception
    {
        // 1. check if expr.value_type is bool
        // 2. etc.
        // 3. create and return node
        ParseTree.Expr expr = (ParseTree.Expr)s2;
        ArrayList<ParseTree.Stmt> stmtlist = (ArrayList<ParseTree.Stmt>)s4;
        return new ParseTree.WhileStmt(expr, stmtlist);
    }
    Object compoundstmt____BEGIN_localdecls_stmtlist_END(Object s1, Object s2, Object s3, Object s4) throws Exception
    {
        // 1. create and return node
        ArrayList<ParseTree.LocalDecl> localdecls = (ArrayList<ParseTree.LocalDecl>)s2;
        ArrayList<ParseTree.Stmt> stmtlist = (ArrayList<ParseTree.Stmt>)s3;
        return new ParseTree.CompoundStmt(localdecls, stmtlist);
    }
    Object printstmt____PRINT_expr_SEMI(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr.value_type is int
        // 2. etc.
        // 3. create and return node
        Token          print = (Token         )s1;
        ParseTree.Expr expr = (ParseTree.Expr)s2;
        return new ParseTree.PrintStmt(expr);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Object localdecls____localdecls_localdecl(Object s1, Object s2) throws Exception
    {
        ArrayList<ParseTree.LocalDecl> localdecls = (ArrayList<ParseTree.LocalDecl>)s1;
        ParseTree.LocalDecl            localdecl  = (ParseTree.LocalDecl           )s2;
        localdecls.add(localdecl);

        // put the variable into the environment
        if (env.Get(localdecl.ident) != null) {
            throw new Exception("Identifier " + localdecl.ident + " is already defined.");
        }
        env.Put(localdecl.ident, localdecl.typespec.typename);
        return localdecls;
    }
    Object localdecls____eps() throws Exception
    {
        return new ArrayList<>();
    }
    Object localdecl____VAR_IDENT_TYPEOF_typespec_SEMI(Object s1, Object s2, Object s3, Object s4, Object s5) throws Exception
    {
        Token id = (Token)s2;  // Identifier token
        ParseTree.TypeSpec typespec = (ParseTree.TypeSpec)s4;  // Type specification
        ParseTree.LocalDecl localdecl = new ParseTree.LocalDecl(id.lexeme, typespec);

        // Adding variable to environment with its type
        if (env.Get(id.lexeme) != null) {
            throw new Exception("Identifier " + id.lexeme + " is already defined.");
        }

        localdecl.reladdr = env.address;
//        env.Put(id.lexeme, typespec.typename);

        return localdecl;
    }
    Object args____arglist(Object s1) throws Exception
    {
//        return s1;
        List<ParseTree.Arg> arglist = (List<ParseTree.Arg>) s1;
        return arglist;
    }
    Object args____eps() throws Exception
    {
        return new ArrayList<>();
    }
    Object arglist____arglist_COMMA_expr(Object s1, Object s2, Object s3) throws Exception
    {
        ArrayList<ParseTree.Arg> arg_list = (ArrayList<ParseTree.Arg>)s1;
        ParseTree.Expr            expr     = (ParseTree.Expr           )s3;
        arg_list.add(new ParseTree.Arg(expr));
        return arg_list;
    }
    Object arglist____expr(Object s1) throws Exception
    {
        ParseTree.Expr expr = (ParseTree.Expr)s1;
        ArrayList<ParseTree.Arg> arg_list = new ArrayList<>();
        arg_list.add(new ParseTree.Arg(expr));
        return arg_list;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private String getType(ParseTree.Expr expr) {
        if (expr instanceof ParseTree.ExprNumLit) {
            return "num";
        } else if (expr instanceof ParseTree.ExprBoolLit) {
            return "bool";
        } else if (expr instanceof ParseTree.ExprIdent) {
            Object type = env.Get(((ParseTree.ExprIdent)expr).ident);
            if (type != null) {
                return type.toString();
            }
            throw new RuntimeException("Identifier not defined: " + ((ParseTree.ExprIdent)expr).ident);
        }
        throw new RuntimeException("Identifier " + " should be array variable.");

//        [Error at 19:25] Identifier x should be array variable.
    }

//    Object expr____expr_ADD_expr(Object s1, Object s2, Object s3) throws Exception
//    {
//        // 1. check if expr1.value_type matches with the expr2.value_type
//        // 2. etc.
//        // 3. create and return node that has value_type
//        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
//        Token          oper  = (Token         )s2;
//        ParseTree.Expr expr2 = (ParseTree.Expr)s3;
//
//        if(!areBothNum(expr1, expr2))
//            throw new Exception("Binary operation + cannot be used with num and bool values.");
//
//        // check if expr1.type matches with expr2.type
//        return new ParseTree.ExprAdd(expr1,expr2);
//    }

    Object expr____expr_ADD_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token oper = (Token)s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;

        String type1 = getType(expr1);
        String type2 = getType(expr2);

        if (!"num".equals(type1) || !"num".equals(type2)) {
            throw new Exception("[Error at " + oper.line + ":" + oper.column + "] Binary operation + cannot be used with " + type1 + " and " + type2 + " values.");
        }

        return new ParseTree.ExprAdd(expr1, expr2);
    }

    Object expr____expr_SUB_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;

        String type1 = getType(expr1);
        String type2 = getType(expr2);

        if (!"num".equals(type1) || !"num".equals(type2)) {
            throw new Exception("[Error at " + oper.line + ":" + oper.column + "] Binary operation - cannot be used with " + type1 + " and " + type2 + " values.");
        }

        // check if expr1.type matches with expr2.type
        return new ParseTree.ExprSub(expr1,expr2);
    }
    Object expr____expr_MUL_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;

        String type1 = getType(expr1);
        String type2 = getType(expr2);

        if (!"num".equals(type1) || !"num".equals(type2)) {
            throw new Exception("[Error at " + oper.line + ":" + oper.column + "] Binary operation * cannot be used with " + type1 + " and " + type2 + " values.");
        }


        // check if expr1.type matches with expr2.type
        return new ParseTree.ExprMul(expr1,expr2);
    }
    Object expr____expr_DIV_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;

        String type1 = getType(expr1);
        String type2 = getType(expr2);

        if (!"num".equals(type1) || !"num".equals(type2)) {
            throw new Exception("[Error at " + oper.line + ":" + oper.column + "] Binary operation / cannot be used with " + type1 + " and " + type2 + " values.");
        }


        // check if expr1.type matches with expr2.type
        return new ParseTree.ExprDiv(expr1,expr2);
    }
    Object expr____expr_MOD_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;

        String type1 = getType(expr1);
        String type2 = getType(expr2);

        if (!"num".equals(type1) || !"num".equals(type2)) {
            throw new Exception("[Error at " + oper.line + ":" + oper.column + "] Binary operation % cannot be used with " + type1 + " and " + type2 + " values.");
        }


        // check if expr1.type matches with expr2.type
        return new ParseTree.ExprMod(expr1,expr2);
    }
    Object expr____expr_EQ_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;

        String type1 = getType(expr1);
        String type2 = getType(expr2);

        if (!"num".equals(type1) || !"num".equals(type2)) {
            throw new Exception("[Error at " + oper.line + ":" + oper.column + "] Binary operation = cannot be used with " + type1 + " and " + type2 + " values.");
        }


        // check if expr1.type matches with expr2.type
        return new ParseTree.ExprEq(expr1,expr2);
    }
    Object expr____expr_NE_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;

        String type1 = getType(expr1);
        String type2 = getType(expr2);

        if (!"num".equals(type1) || !"num".equals(type2)) {
            throw new Exception("[Error at " + oper.line + ":" + oper.column + "] Binary operation <> cannot be used with " + type1 + " and " + type2 + " values.");
        }


        // check if expr1.type matches with expr2.type
        return new ParseTree.ExprNe(expr1,expr2);
    }
    Object expr____expr_LE_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;

        String type1 = getType(expr1);
        String type2 = getType(expr2);

        if (!"num".equals(type1) || !"num".equals(type2)) {
            throw new Exception("[Error at " + oper.line + ":" + oper.column + "] Binary operation <= cannot be used with " + type1 + " and " + type2 + " values.");
        }


        // check if expr1.type matches with expr2.type
        return new ParseTree.ExprLe(expr1,expr2);
    }
    Object expr____expr_LT_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;

        String type1 = getType(expr1);
        String type2 = getType(expr2);

        if (!"num".equals(type1) || !"num".equals(type2)) {
            throw new Exception("[Error at " + oper.line + ":" + oper.column + "] Binary operation < cannot be used with " + type1 + " and " + type2 + " values.");
        }


        // check if expr1.type matches with expr2.type
        return new ParseTree.ExprLt(expr1,expr2);
    }
    Object expr____expr_GE_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;

        String type1 = getType(expr1);
        String type2 = getType(expr2);

        if (!"num".equals(type1) || !"num".equals(type2)) {
            throw new Exception("[Error at " + oper.line + ":" + oper.column + "] Binary operation >= cannot be used with " + type1 + " and " + type2 + " values.");
        }


        // check if expr1.type matches with expr2.type
        return new ParseTree.ExprGe(expr1,expr2);
    }
    Object expr____expr_GT_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;

        String type1 = getType(expr1);
        String type2 = getType(expr2);

        if (!"num".equals(type1) || !"num".equals(type2)) {
            throw new Exception("[Error at " + oper.line + ":" + oper.column + "] Binary operation > cannot be used with " + type1 + " and " + type2 + " values.");
        }

        // check if expr1.type matches with expr2.type
        return new ParseTree.ExprGt(expr1,expr2);
    }
    Object expr____expr_AND_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type is bool and expr2.value_type is bool
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;

        String type1 = getType(expr1);
        String type2 = getType(expr2);

        if (!"bool".equals(type1) || !"bool".equals(type2)) {
            throw new Exception("[Error at " + oper.line + ":" + oper.column + "] Binary operation and cannot be used with " + type1 + " and " + type2 + " values.");
        }

//        if(!areBothBool(expr1, expr2))
//            throw new Exception("Binary operation and cannot be used with num and bool values.");

        // check if expr1.type matches with expr2.type
        return new ParseTree.ExprAnd(expr1,expr2);
    }
    Object expr____expr_OR_expr(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if expr1.value_type is bool and expr2.value_type is bool
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;

        String type1 = getType(expr1);
        String type2 = getType(expr2);

        if (!"bool".equals(type1) || !"bool".equals(type2)) {
            throw new Exception("[Error at " + oper.line + ":" + oper.column + "] Binary operation or cannot be used with " + type1 + " and " + type2 + " values.");
        }

        // check if expr1.type matches with expr2.type
        return new ParseTree.ExprOr(expr1,expr2);
    }
    Object expr____NOT_expr(Object s1, Object s2) throws Exception
    {
        // 1. check if expr.value_type is bool
        // 2. etc.
        // 3. create and return node that has value_type
        Token          oper = (Token         )s1;
        ParseTree.Expr expr = (ParseTree.Expr)s2;

        if(!isBool(expr))
            throw new Exception("Unary operation not cannot be used with num value");

        return new ParseTree.ExprNot(expr);
    }
    Object expr____LPAREN_expr_RPAREN(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. create and return node whose value_type is the same to the expr.value_type
        Token          lparen = (Token         )s1;
        ParseTree.Expr expr   = (ParseTree.Expr)s2;
        Token          rparen = (Token         )s3;

        ParseTree.ExprParen exprParen = new ParseTree.ExprParen(expr);
        String type = determineType(expr);

        if (type.equals("num")) {
            exprParen.info.type = "num";
        }
        else if (type.equals("bool")) {
            exprParen.info.type = "bool";
        }
//        return new ParseTree.ExprParen(expr);
        return exprParen;
    }
    Object expr____IDENT(Object s1) throws Exception
    {
        // 1. check if id.lexeme can be found in chained symbol tables
        // 2. check if it is variable type
        // 3. etc.
        // 4. create and return node that has the value_type of the id.lexeme
//        Token id = (Token)s1;
        Token id = (Token)s1;
//        Object id_type = env.Get(id.lexeme);
//        if (id_type == null) {
//            throw new Exception("Semantic error: Identifier '" + id.lexeme + "' not found in symbol table.");
//        }
        ParseTree.ExprIdent expr = new ParseTree.ExprIdent(id.lexeme);
        expr.reladdr = env.address;
        return expr;
    }
    Object expr____NUM_LIT(Object s1) throws Exception {
        Token token = (Token) s1;
        double value = token.lexeme.contains(".") ? Double.parseDouble(token.lexeme) : Integer.parseInt(token.lexeme);
        return new ParseTree.ExprNumLit(value);
//        Token token = (Token)s1;
//        try {
//            double value = Double.parseDouble(token.lexeme);  // Handle as 'num' which can be float
//            return new ParseTree.ExprNumLit(value);
//        } catch(NumberFormatException e) {
//            throw new Exception("Semantic error: Invalid number format '" + token.lexeme + "'");
//        }
    }
    Object expr____BOOL_LIT(Object s1) throws Exception {
        Token token = (Token) s1;
        boolean value = Boolean.parseBoolean(token.lexeme); // Ensure token.lexeme is "true" or "false"
        return new ParseTree.ExprBoolLit(value); // This should directly create a boolean literal node
    }
    Object expr____IDENT_LPAREN_args_RPAREN(Object s1, Object s2, Object s3, Object s4) throws Exception
    {
        // 1. check if id.lexeme can be found in chained symbol tables
        // 2. check if it is function type
        // 3. check if the number and types of env(id.lexeme).params match with those of args
        // 4. etc.
        // 5. create and return node that has the value_type of env(id.lexeme).return_type
        Token                    id   = (Token                   )s1;
        ArrayList<ParseTree.Arg> args = (ArrayList<ParseTree.Arg>)s3;
//        Object func_attr = env.Get(id.lexeme);
//        {
//            // check if argument types match with function param types
//            if(env.Get(id.lexeme).equals("num()")
//                && (args.size() == 0)
//                )
//            {} // ok
//            else
//            {
//                throw new Exception("semantic error");
//            }
//        }
        return new ParseTree.ExprFuncCall(id.lexeme, args);
    }
    Object expr____NEW_primtype_LBRACKET_expr_RBRACKET(Object s1, Object s2, Object s3, Object s4, Object s5) throws Exception
    {
        // 1. check if expr.value_type is int
        // 2. etc.
        // 3. create and return node that has the value_type of primtype.lexeme[]
        Token              new_     = (Token             )s1;
        ParseTree.TypeSpec primtype = (ParseTree.TypeSpec)s2;
        Token              lbracket = (Token             )s3;
        ParseTree.Expr     expr     = (ParseTree.Expr     )s4;
        Token              rbracket = (Token             )s5;
        return new ParseTree.ExprNewArray(primtype, expr);
    }
    Object expr____IDENT_LBRACKET_expr_RBRACKET(Object s1, Object s2, Object s3, Object s4) throws Exception
    {
        // 1. check if id.lexeme can be found in chained symbol tables
        // 2. check if it is array type
        // 3. check if expr.value_type is int
        // 4. etc.
        // 5. create and return node that has the value_type of id.lexeme[]
        Token          id       = (Token         )s1;
        Token          lbracket = (Token         )s2;
        ParseTree.Expr expr     = (ParseTree.Expr)s3;
        Token          rbracket = (Token         )s4;
        return new ParseTree.ExprArrayElem(id.lexeme, expr);
    }
    Object expr____IDENT_DOT_SIZE(Object s1, Object s2, Object s3) throws Exception
    {
        // 1. check if id.lexeme can be found in chained symbol tables
        // 2. check if it is array type
        // 3. etc.
        // 4. create and return node that has int type
        Token id   = (Token)s1;
        Token dot  = (Token)s2;
        Token size = (Token)s3;
        return new ParseTree.ExprArraySize(id.lexeme);
    }
    private boolean isBool(ParseTree.Expr expr) throws Exception {

        // expr = bool
        if(expr instanceof ParseTree.ExprBoolLit) return true;

            // expr = ident
        else if(expr instanceof ParseTree.ExprIdent
                && env.Get(((ParseTree.ExprIdent) expr).ident).equals("bool")
        ) return true;

            // expr = paren
        else if(expr instanceof ParseTree.ExprParen && expr.info.type.equals("bool")) return true;

        else return false;
    }

    private boolean areBothNum(ParseTree.Expr expr1, ParseTree.Expr expr2) throws Exception {
        // expr1 = num, expr2 = num
        if(expr1 instanceof ParseTree.ExprNumLit && expr2 instanceof ParseTree.ExprNumLit)
            return true;

            // expr1 = num, expr2 = ident
        else if(expr1 instanceof ParseTree.ExprNumLit
                && (expr2 instanceof ParseTree.ExprIdent && env.Get(((ParseTree.ExprIdent) expr2).ident).equals("num"))
        ) return true;

            // expr1 = ident, expr2 = num
        else if(expr2 instanceof ParseTree.ExprNumLit
                && (expr1 instanceof ParseTree.ExprIdent && env.Get(((ParseTree.ExprIdent) expr1).ident).equals("num"))
        ) return true;

            // expr1 = ident, expr2 = ident
        else if(expr1 instanceof ParseTree.ExprIdent && expr2 instanceof ParseTree.ExprIdent)
            return env.Get(((ParseTree.ExprIdent)expr1).ident).equals(env.Get(((ParseTree.ExprIdent)expr2).ident));

            // if expr1 is paren, expr2 is num
        else if(expr1 instanceof ParseTree.ExprParen
                && expr1.info.type.equals("num")
                && expr2 instanceof ParseTree.ExprNumLit
        ) return true;

            // if expr1 is paren, expr2 is ident
        else if(expr1 instanceof ParseTree.ExprParen && expr1.info.type.equals("num")
                && (expr2 instanceof ParseTree.ExprIdent && env.Get(((ParseTree.ExprIdent) expr2).ident).equals("num"))
        ) return true;

            // if expr1 is num, expr2 is paren
        else if(expr1 instanceof ParseTree.ExprNumLit
                && expr2 instanceof ParseTree.ExprParen && expr2.info.type.equals("num")
        ) return true;

            // if expr1 is ident, expr2 is paren
        else if(expr1 instanceof ParseTree.ExprIdent && env.Get(((ParseTree.ExprIdent) expr1).ident).equals("num")
                && expr2 instanceof ParseTree.ExprParen && expr2.info.type.equals("num")
        ) return true;

            // if both are parens
        else if(expr1 instanceof ParseTree.ExprParen && expr1.info.type.equals("num")
                && expr2 instanceof ParseTree.ExprParen && expr2.info.type.equals("num")
        ) return true;

            // TODO: might have to check if the other one is a paren
            // if either are one of the bool classes
        else if(determineType(expr1).equals("num") || determineType(expr2).equals("num"))
            return true;

            // throw error
        else return false;
    }

    private boolean areBothBool(ParseTree.Expr expr1, ParseTree.Expr expr2) throws Exception {

        // expr1 = bool, expr2 = bool
        if(expr1 instanceof ParseTree.ExprBoolLit && expr2 instanceof ParseTree.ExprBoolLit)
            return true;

            // expr1 = bool, expr2 = ident
        else if(expr1 instanceof ParseTree.ExprBoolLit
                && (expr2 instanceof ParseTree.ExprIdent && env.Get(((ParseTree.ExprIdent) expr2).ident).equals("bool"))
        ) return true;

            // expr1 = ident, expr2 = bool
        else if(expr2 instanceof ParseTree.ExprBoolLit
                && (expr1 instanceof ParseTree.ExprIdent && env.Get(((ParseTree.ExprIdent) expr1).ident).equals("bool"))
        ) return true;

            // expr1 = ident, expr2 = ident
        else if(expr1 instanceof ParseTree.ExprIdent && expr2 instanceof ParseTree.ExprIdent)
            return env.Get(((ParseTree.ExprIdent)expr1).ident).equals(env.Get(((ParseTree.ExprIdent)expr2).ident));

            // if expr1 is paren, expr2 is bool
        else if(expr1 instanceof ParseTree.ExprParen
                && expr1.info.type.equals("bool")
                && expr2 instanceof ParseTree.ExprBoolLit
        ) return true;

            // if expr1 is paren, expr2 is ident
        else if(expr1 instanceof ParseTree.ExprParen && expr1.info.type.equals("bool")
                && (expr2 instanceof ParseTree.ExprIdent && env.Get(((ParseTree.ExprIdent) expr2).ident).equals("bool"))
        ) return true;

            // if expr1 is bool, expr2 is paren
        else if(expr1 instanceof ParseTree.ExprBoolLit
                && expr2 instanceof ParseTree.ExprParen && expr2.info.type.equals("bool")
        ) return true;

            // if expr1 is ident, expr2 is paren
        else if(expr1 instanceof ParseTree.ExprIdent && env.Get(((ParseTree.ExprIdent) expr1).ident).equals("bool")
                && expr2 instanceof ParseTree.ExprParen && expr2.info.type.equals("bool")
        ) return true;

            // if both are parens
        else if(expr1 instanceof ParseTree.ExprParen && expr1.info.type.equals("bool")
                && expr2 instanceof ParseTree.ExprParen && expr2.info.type.equals("bool")
        ) return true;

            // TODO: might have to check if the other one is a paren
            // if either are one of the bool classes
        else if(determineType(expr1).equals("bool") || determineType(expr2).equals("bool"))
            return true;

            // throw error
        else return false;
    }

    private String determineType(ParseTree.Expr expr) throws Exception {
        List<Class<?>> numClasses = Arrays.asList(
                ParseTree.ExprNumLit.class, ParseTree.ExprAdd.class, ParseTree.ExprSub.class,
                ParseTree.ExprMul.class, ParseTree.ExprDiv.class, ParseTree.ExprMod.class
        );
        boolean num = numClasses.stream().anyMatch(clazz -> clazz.isInstance(expr));
        if(num) return "num";

        List<Class<?>> boolClasses = Arrays.asList(
                ParseTree.ExprBoolLit.class, ParseTree.ExprAnd.class, ParseTree.ExprOr.class,
                ParseTree.ExprNot.class, ParseTree.ExprEq.class, ParseTree.ExprNe.class,
                ParseTree.ExprLe.class, ParseTree.ExprLt.class, ParseTree.ExprGe.class,
                ParseTree.ExprGt.class
        );
        boolean bool = boolClasses.stream().anyMatch(clazz -> clazz.isInstance(expr));
        if(bool) return "bool";

        // expr = array, type = num
        if(expr instanceof ParseTree.ExprNewArray && ((ParseTree.ExprNewArray) expr).elemtype.typename.equals("num"))
            return "num[]";

        // expr = array, type = bool
        if(expr instanceof ParseTree.ExprNewArray && ((ParseTree.ExprNewArray) expr).elemtype.typename.equals("bool"))
            return "bool[]";

        return "null";
    }

}
