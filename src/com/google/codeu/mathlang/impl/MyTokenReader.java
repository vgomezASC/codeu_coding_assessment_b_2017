// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.codeu.mathlang.impl;

import java.io.IOException;

import com.google.codeu.mathlang.core.tokens.NameToken;
import com.google.codeu.mathlang.core.tokens.NumberToken;
import com.google.codeu.mathlang.core.tokens.StringToken;
import com.google.codeu.mathlang.core.tokens.SymbolToken;
import com.google.codeu.mathlang.core.tokens.Token;
import com.google.codeu.mathlang.parsing.TokenReader;

// MY TOKEN READER
//
// This is YOUR implementation of the token reader interface. To know how
// it should work, read src/com/google/codeu/mathlang/parsing/TokenReader.java.
// You should not need to change any other files to get your token reader to
// work with the test of the system.
public final class MyTokenReader implements TokenReader {

  private final String source;
  //private final String[] lines;
  private int currentPosition = 0;
  //private int currentLine = 0;
  private char read() throws IOException 
  {
    try
    {
      if(currentPosition < source.length())
      {
        return source.charAt(currentPosition ++);
      }
      else
      {
        return '\0';
      }
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
      throw new IOException();
    }
  }
  
  private void back()
  {
    currentPosition --;
  }

  public MyTokenReader(String source) 
  {
    // Your token reader will only be given a string for input. The string will
    // contain the whole source (0 or more lines).
    this.source = source;
  }

  private String readName(char c)
  {
    String result = "" + c;
    try
    {
      c = read();
      while(!Character.isWhitespace(c) && c != ';' && c != '\0' && Character.isAlphabetic(c))
      {
        result += c;
        c = read();
      }
      if(!Character.isAlphabetic(c))
      {
        back();
      }
    }
    catch(IOException exception)
    {

    }
    return result;
  } 

  private double readNumber(char c)
  {
    double result = c - '0';
    boolean foundDot = false;
    double division = 0.1;
    try
    {
      c = read();
      while(c != '\0' && ((c >= '0' && c <= '9') || c == '.'))
      {
        if(c == '.')
        {
          if(foundDot)
          {
            break;
          }
          foundDot = true;
        }
        else if(!foundDot)
        {
          result *= 10;
          result += c - '0';
        }
        else
        {
          result += (c - '0') * division;
          division /= 10;
        }
        c = read();
      }
    }
    catch(IOException exception)
    {

    }
    back();
    return result;
  } 
  private String readString(char c)
  {
    String result = "";
    try
    {
      c = read();
      while(c != '\0' && c != '\"')
      {
        result += c;
        c = read();
      }
    }
    catch(IOException exception)
    {

    }
    return result;
  }

  @Override
  public Token next() throws IOException 
  {
    // Most of your work will take place here. For every call to |next| you should
    // return a token until you reach the end. When there are no more tokens, you
    // should return |null| to signal the end of input.

    // If for any reason you detect an error in the input, you may throw an IOException
    // which will stop all execution.
    try
    {
      char currentChar = read();
      while(currentChar == ' ' || currentChar == '\n')
      {
        currentChar = read();
      }
      if(Character.isAlphabetic(currentChar))
      {
        return new NameToken(readName(currentChar));
      }
      else if (currentChar >= '0' && currentChar <= '9')
      {
        return new NumberToken(readNumber(currentChar));
      }
      switch(currentChar)
      {
       case ';': return new SymbolToken(';');
       case '\"': return new StringToken(readString(currentChar));
       case '=': return new SymbolToken('=');
       case '+': return new SymbolToken('+');
       case '-': return new SymbolToken('-');
       case '\0': return null;
      }
    }
    catch(IOException exception)
    {
      exception.printStackTrace();
      throw new IOException("Failed to read next token!");
    }
    return null;
  }
}