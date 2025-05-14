# Interpreter Project
## Description
An interpreter for the language defined as follows:
```
Program:
	Assignment*

Assignment:
	Identifier = Exp;

Exp: 
	Exp + Term | Exp - Term | Term

Term:
	Term * Fact  | Fact

Fact:
	( Exp ) | - Fact | + Fact | Literal | Identifier

Identifier:
     	Letter [Letter | Digit]*

Letter:
	a|...|z|A|...|Z|_

Literal:
	0 | NonZeroDigit Digit*
		
NonZeroDigit:
	1|...|9

Digit:
	0|1|...|9
```
## Instructions
Navigate to the project directory and compile with ```javac Main.java```. Then use ```java Main <filename>``` to run the interpreter. 

> [!Note]
> I used Java 22 when writing this program. It may be compatible with previous Java versions as well, but if you are having trouble running the program please make sure to use Java 22.
### Example usage: 
```
javac Main.java
java Main test1
```
### Test files
In the same directory I have provided 3 test files. 
#### `test1`
Expected output:
```
x = 1
y = 2
x_2 = 0
z = 3
```
#### `test2`
Expected output:
```
Syntax error: ';' expected on line 1
```
#### `test3`
Expected output:
```
x = 1
y = 2
z = 9
```
