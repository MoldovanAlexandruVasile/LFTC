%{
    #include <string.h>
    #include <stdio.h>
    #include <stdlib.h>

    extern int yylex();
    extern int yyparse();
    extern FILE *yyin;
    extern int yylineno;  // defined and maintained in flex.flex
    extern char *yytext;  // defined and maintained in flex.flex
    extern void printPIF();
    void yyerror(const char *s);
%}

%token BOOLEAN
%token CHARACTER
%token ELSE
%token FOR
%token IF
%token INTEGER
%token PRINT
%token READ
%token START
%token IDENTIFIER
%token CONSTANT
%token STRING
%token VAR
%token WHILE
%%



program :VAR '{' decl '}' START '{' stmt_list '}'
decl : type IDENTIFIER ';' | type IDENTIFIER ';' decl;
type : INTEGER | BOOLEAN | CHARACTER | STRING;
stmt_list: stmt  | stmt stmt_list
stmt : assignstmt ';' | inputstmt ';' | outputstmt ';' | condstmt | loopstmt
assignstmt : IDENTIFIER  '=' expr | IDENTIFIER '=' expr op expr
expr : IDENTIFIER | CONSTANT;
op : '+' | '-' | '*' | '/' | '>' | '<' | '=' | '&' | '|' ;
inputstmt : READ IDENTIFIER
outputstmt : PRINT IDENTIFIER
condstmt : IF '('expr op expr')' '{' stmt_list '}' | IF '('expr op expr')' '{' stmt_list '}' ELSE '{' stmt_list '}'
loopstmt : WHILE '('expr op expr')' '{' stmt_list '}' | FOR '('IDENTIFIER '=' CONSTANT';' IDENTIFIER op expr';' assignstmt')' '{' stmt_list '}'


//
%%

int main(int argc, char *argv[]) {
    //printf("dsadsadsada\n");
    ++argv, --argc; /* skip over program name */
    if ( argc > 0 ){
        //printf("--------%s",argv[0]);
        yyin = fopen( argv[0], "r" );
    }
    else
        yyin = stdin;
    while (!feof(yyin)) {
        //printf("%s\n", "########");
        yyparse();
    }
    //printPIF();
    printf("%s\n","The program is sintactically correct!!!");
    return 0;
}

void yyerror(const char *s) {
    fprintf(stderr, "%d: error: '%s' at '%s', yylval=%u\n", yylineno, s, yytext, yylval);
    //printPIF();
    exit(1);
}