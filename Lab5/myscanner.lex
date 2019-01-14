/*
bison -d verif.y
lex myscanner.lex
gcc lex.yy.c verif.tab.c -o a
./a <program1.txt
*/


%{
    #include <stdlib.h>
    #include <string.h>
    #include "verif.tab.h"

    int crtpif = 0;
    int crtsym = 0;
    int idx=100;



    struct pifElem{
        int code1;
        int code2;
    };

    struct symElem{
        int wordidx;
        char word[256];
    };

    struct pifElem pif[500];
    struct symElem sym[500];

    int findAddressOf(char *token){
        for (int i=0; i<crtsym; i++)
            if (!strcmp(token, sym[i].word))
                return sym[i].wordidx;
        return -1;
    }


    void createPIF(char* word, int code){
        if(code != 300 && code!= 500){
            pif[crtpif].code1 = code;
            pif[crtpif].code2 = 0;
            crtpif++;
            //printf("%d | -\n", code);
        }
        else{
            if(code == 300 || code == 500){

                int address = findAddressOf(word);
                //printf("address: %d\n",address);
                if (address != -1){
                    pif[crtpif].code1 = code;
                    pif[crtpif].code2 = address;
                    crtpif++;
                }
                else {
                    sym[crtsym].wordidx = idx;
                    strcpy(sym[crtsym].word,word);
                    idx++;
                    pif[crtpif].code1 = code;
                    pif[crtpif].code2 = sym[crtsym].wordidx;
                    crtsym++;
                    crtpif++;
                    //PIFAddress[crtToken++] = crtAddress-1;
                }
                //printf("%d | %d\n", code,idx);
                //idx++;
            }
        }
    }

    void printPIF(){
        printf("-------PIF-------\n");
        for(int i=0;i<crtpif;i++)
        {
            if(pif[i].code2 == 0)
                printf("%d | - \n", pif[i].code1);
            else
                printf("%d | %d \n", pif[i].code1, pif[i].code2);
        }
        printf("\n-------SymTable-------\n");
        for(int i=0;i<crtsym;i++)
        {
            printf("%s | %d \n", sym[i].word, sym[i].wordidx);
        }
    }

%}

%option noyywrap
%option yylineno
digit         [0-9]
letter        [a-zA-Z]

%%
"-"			        			{   createPIF("-",2); 			return '-';		}
"!"			        			{   createPIF("!",3); 			return '!';		}
"&"			        			{   createPIF("&",4);  			return '&';		}
"("			        			{   createPIF("(",5); 			return '(';		}
")"			        			{   createPIF(")",6); 			return ')';		}
"*"			        			{   createPIF("*",7); 			return '*';		}
","			        			{   createPIF(",",8); 			return ',';		}
"/"			        			{   createPIF("/",9); 			return '/';		}
";"			        			{   createPIF(";",10); 			return ';';		}
"{"			        			{   createPIF("{",11); 			return '{';		}
"|"				        		{   createPIF("|",12); 			return '|';		}
"}"			        			{   createPIF("}",13); 			return '}';		}
"\""		        			{   createPIF("\"",14); 		return '"';		}
"+"			        			{   createPIF("+",15); 			return '+';		}
"<"			        			{   createPIF("<",16); 			return '<';		}
"="			        			{   createPIF("=",17); 			return '=';		}
">"			        			{   createPIF(">",18); 			return '>';		}
"BOOLEAN"	        			{   createPIF("BOOLEAN",19);	return BOOLEAN;}
"CHARACTER"	        			{   createPIF("CHARACTER",20);	return CHARACTER;}
"ELSE"	        				{   createPIF("ELSE",21);		return ELSE;	}
"FOR"	        				{   createPIF("FOR",22);		return FOR;		}
"IF"	        				{   createPIF("IF",23);			return IF;		}
"INTEGER"	        			{   createPIF("INTEGER",24);	return INTEGER;	}
"PRINT"	        				{   createPIF("PRINT",25);		return PRINT;	}
"READ"	        				{   createPIF("READ",26);		return READ;	}
"START"	        				{   createPIF("START",27);		return START;	}
"STRING"	        			{   createPIF("STRING",28);		return STRING;	}
"VAR"	        				{   createPIF("VAR",29);		return VAR;		}
"WHILE"	        				{   createPIF("WHILE",30);		return WHILE;	}
{letter}({letter}|{digit})*     {   createPIF(yytext,500);		return IDENTIFIER;}
{digit}+                        {   createPIF(yytext,300);		return CONSTANT;  }
[ \t\n\r]
.                               { printf("Unknown character [%c]\n",yytext[0]); return -1;  }
%%