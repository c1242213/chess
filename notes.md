# My notes
### substring: returns a string that is a substring of this string in the package java.lang
#### examples: "unhappy".substring(2)

## primative data types:
int anInt = 5;

byte aByte = -128;

long long1 = 10;

long long2 = 10L;

short aShort = 5;
## string addition 
System.out.println(anInt + ", " + aByte + ", " + long1 + ", " + long2);
System.out.printf("%d, %d, %d, %d\n, anInt, aByte, long1, long2);

## Char
char char1 = 'a';

char char2 = '\u03A3'; ### special char;

## converting a string to an int
int Integer.parseInt(string value);

## strings
string s = "hello";

string s3 = s1 + " " + s2;

# 1/19
## packages 
#### fully qualified package student class part of package
package edu.byu.cs;

public class Student{ }

##### packages separated by dots . are subpackages
##### the package name becomes part of the class name. ex java has two dates java.util.Date java.sql.Date

## imports: 
#### provides a shorthand notation to qualify as the package name
#### do not bring in the code
#### the wildcard: imports all classes in the package but not subpackages
import java.util.* 
### exceptions: java.lang package, do not need to be imported
# ClassPath: 
#### an environment variable that contains a list of directories that contain .class files

# Class:
package SimpleClassExample;
import java.util.Date;
public class Person{
    private String firstName;
    private String lastName;
    private Date birthDate;
}

# References:
date dt;
#### reference
dt = new Date();
#### reference that points to an object

# equals method
@Override
public boolean equals(Object o) {
if (this == o) return true;
if (!(o instanceof Person)) return false;
Person person = (Person) o
return Objects.equals(firstName, person.firstName) && Objects.equals(lastname)
}
@Override
public int hashCode() {
return Objects.hash(firstName, lastName, birthDate);
}
@Override
public String toString() {
}
arrays.deep.equals

# Instance variable:
#### at the top, gets its own copy of all the instance variables, its inside the object
#### access by calling the variable

# static variables:
#### variables associated with the class not with the instance, ex, pi (never changes), i (you don't want it to be reset to 0)
#### to call variable reference the class 
