//---------------------------------------------------------------------
// Name : Orifkhon Kilichev
// Email: omk5087@psu.edu
// Class: CMPSC 470-001, Spring 2024
//---------------------------------------------------------------------

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2000 Gerwin Klein <lsf@jflex.de>                          *
 * All rights reserved.                                                    *
 *                                                                         *
 * Thanks to Larry Bell and Bob Jamison for suggestions and comments.      *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

%%

%class Lexer
%byaccj
%line
%column

%{

  public Parser   parser;

  public Lexer(java.io.Reader r, Parser parser) {
    this(r);
    this.parser = parser;
  }
%}

num          = [0-9]+("."[0-9]+)?
identifier   = [a-zA-Z][a-zA-Z0-9_]*
newline      = \n
whitespace   = [ \t\r]+
linecomment  = "//".*
blockcomment = "{"[^]*"}"
boolean = "true" | "false"

%{
  public int getYyline() {
    return yyline + 1;
  }

  public int getYycolumn() {
    return yycolumn + 1;
  }
%}

%%

"func"                              { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.FUNC   ; }
"var"                               { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.VAR    ; }
"return"                            { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.RETURN ; }
"if"                                { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.IF     ; }
"else"                              { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.ELSE   ; }
"while"                             { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.WHILE  ; }
"then"                              { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.THEN   ; }
"bool"                              { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.BOOL   ; }
"new"                               { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.NEW    ; }
"size"                              { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.SIZE   ; }
"num"                               { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.NUM    ; }
"begin"                             { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.BEGIN  ; }
"end"                               { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.END    ; }
"print"                             { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.PRINT  ; }
"not"                               { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.NOT  ; }
"or"                                { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.OR ; }
"and"                               { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.AND ; }

"("                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.LPAREN ; }
")"                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.RPAREN ; }
"["                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.LBRACKET ; }
"]"                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.RBRACKET ; }
":="                                { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.ASSIGN ; }
","                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.COMMA  ; }
"."                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.DOT    ; }
"::"                                { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.TYPEOF ; }
";"                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.SEMI   ; }

"+"                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.ADD ; }
"-"                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.SUB ; }
"*"                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.MUL ; }
"/"                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.DIV ; }
"%"                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.MOD ; }

"<"                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.LT ; }
"<="                                { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.LE ; }
">"                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.GT ; }
">="                                { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.GE ; }
"<>"                                { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.NE ; }
"="                                 { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.EQ ; }

{boolean}                           { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.BOOL_LIT; }
{identifier}                        { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.IDENT  ;  }
{num}                               { parser.yylval = new ParserVal(new Token(yytext(), yyline + 1, yycolumn + 1)); return Parser.NUM_LIT;  }


{linecomment}                       { System.out.print(""); }
{newline}                           { System.out.print(""); }
{whitespace}                        { System.out.print(""); }
{blockcomment}                      { System.out.print(""); }


\b     { System.err.println("Sorry, backspace doesn't work"); }

/* error fallback */
[^]    { System.err.println("Error: unexpected character '" + yytext() + "' at " + (yyline+1) + ":" + (yycolumn+1) + ".\n"); return -1; }
