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
// blockcomment = "/*"[^]*"*/"
blockcomment = "{"[^]*"}"
boolean = "true" | "false"

%line
%column

%{
  public int getYyline() {
    return yyline + 1;
  }

  public int getYycolumn() {
    return yycolumn + 1;
  }
%}

%%

"func"                              { parser.yylval = new ParserVal(new Token(yytext())); return Parser.FUNC   ; }
"var"                               { parser.yylval = new ParserVal(new Token(yytext())); return Parser.VAR    ; }
"return"                            { parser.yylval = new ParserVal(new Token(yytext())); return Parser.RETURN ; }
"if"                                { parser.yylval = new ParserVal(new Token(yytext())); return Parser.IF     ; }
"else"                              { parser.yylval = new ParserVal(new Token(yytext())); return Parser.ELSE   ; }
"while"                             { parser.yylval = new ParserVal(new Token(yytext())); return Parser.WHILE  ; }
"then"                              { parser.yylval = new ParserVal(new Token(yytext())); return Parser.THEN   ; }
// "void"                              { parser.yylval = new ParserVal((Object)yytext()); return Parser.VOID   ; }
"bool"                              { parser.yylval = new ParserVal(new Token(yytext())); return Parser.BOOL   ; }
"new"                               { parser.yylval = new ParserVal(new Token(yytext())); return Parser.NEW    ; }
"size"                              { parser.yylval = new ParserVal(new Token(yytext())); return Parser.SIZE   ; }
"num"                               { parser.yylval = new ParserVal(new Token(yytext())); return Parser.NUM    ; }
"begin"                             { parser.yylval = new ParserVal(new Token(yytext())); return Parser.BEGIN  ; }
"end"                               { parser.yylval = new ParserVal(new Token(yytext())); return Parser.END    ; }
"print"                             { parser.yylval = new ParserVal(new Token(yytext())); return Parser.PRINT  ; }
"not"                               { parser.yylval = new ParserVal(new Token(yytext())); return Parser.NOT  ; }

"("                                 { parser.yylval = new ParserVal(new Token(yytext())); return Parser.LPAREN ; }
")"                                 { parser.yylval = new ParserVal(new Token(yytext())); return Parser.RPAREN ; }
"["                                 { parser.yylval = new ParserVal(new Token(yytext())); return Parser.LBRACKET ; }
"]"                                 { parser.yylval = new ParserVal(new Token(yytext())); return Parser.RBRACKET ; }
":="                                { parser.yylval = new ParserVal(new Token(yytext())); return Parser.ASSIGN ; }
","                                 { parser.yylval = new ParserVal(new Token(yytext())); return Parser.COMMA  ; }
"."                                 { parser.yylval = new ParserVal(new Token(yytext())); return Parser.DOT    ; }
"::"                                { parser.yylval = new ParserVal(new Token(yytext())); return Parser.TYPEOF ; }
";"                                 { parser.yylval = new ParserVal(new Token(yytext())); return Parser.SEMI   ; }

"+"                                 { parser.yylval = new ParserVal(new Token(yytext())); return Parser.ADD ; }
"-"                                 { parser.yylval = new ParserVal(new Token(yytext())); return Parser.SUB ; }
"or"                                { parser.yylval = new ParserVal(new Token(yytext())); return Parser.OR ; }

"<"                                 { parser.yylval = new ParserVal(new Token(yytext())); return Parser.LT ; }
"<="                                { parser.yylval = new ParserVal(new Token(yytext())); return Parser.LE ; }
">"                                 { parser.yylval = new ParserVal(new Token(yytext())); return Parser.GT ; }
">="                                { parser.yylval = new ParserVal(new Token(yytext())); return Parser.GE ; }
"<>"                                { parser.yylval = new ParserVal(new Token(yytext())); return Parser.NE ; }
"="                                 { parser.yylval = new ParserVal(new Token(yytext())); return Parser.EQ ; }

"*"                                 { parser.yylval = new ParserVal(new Token(yytext())); return Parser.MUL ; }
"/"                                 { parser.yylval = new ParserVal(new Token(yytext())); return Parser.DIV ; }
"and"                               { parser.yylval = new ParserVal(new Token(yytext())); return Parser.AND ; }

{num}                               { parser.yylval = new ParserVal(new Token(yytext())); return Parser.NUM_LIT; }
{identifier}                        { parser.yylval = new ParserVal(new Token(yytext())); return Parser.IDENT  ; }
{boolean}                           { parser.yylval = new ParserVal(new Token(yytext())); return Parser.BOOL_LIT; }

{linecomment}                       { System.out.print(""); }
{newline}                           { System.out.print(""); }
{whitespace}                        { System.out.print(""); }
{blockcomment}                      { System.out.print(""); }


\b     { System.err.println("Sorry, backspace doesn't work"); }

/* error fallback */
[^]    { System.err.println("Error: unexpected character '" + yytext() + "' at " + (yyline+1) + ":" + (yycolumn+1) + ".\n"); return -1; }
