package com.bertvanbrakel.lang.interpolator;

import static com.bertvanbrakel.lang.Check.checkNotNull;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


/**
 * I interpolate strings of the form
 *
 * <pre>the ${animal} jumped over the ${what}</pre>
 *
 *
 *
 * @author Bert van Brakel
 *
 */
public class Interpolator {

    public static CharSequence interpolate(final CharSequence input, final Map<String, Object> vars) {
        if( input == null){
            return null;
        } else if( vars == null || vars.size() == 0 ){
            return input;
        } else {
            //TODO:we may want a faster, lower resource parser for strings
            final StringReader sr = new StringReader(input.toString());
            final StringWriter sw = new StringWriter();
            try {
                interpolate(sr,sw,vars);
            } catch (final IOException e) {
                //should never be thrown if we're only reading a string
                throw new RuntimeException("Unexpected IO error whilst reading string",e);
            }
            return sw.toString();
        }
    }

    public static void interpolate(final Reader in, final Writer out, final Map<String, Object> vars) throws IOException {
        checkNotNull("inputReader", in);
        checkNotNull("outputWriter", out);
        new ReaderParser(in,out,vars).interpolate();
    }

    private static class ReaderParser {
        final Reader in;
        final Writer out;
        final Map<String, Object> vars;

        public ReaderParser(final Reader in, final Writer out, final Map<String, Object> vars) {
            super();
            this.in = in;
            this.out = out;
            this.vars = vars==null?new HashMap<String, Object>():vars;
        }

        public void interpolate() throws IOException {
            //if no variables to interpolate, simply copy the input to the output
            if( vars == null || vars.size() == 0){
                final char[] tmpBuf = new char[1024*8];
                int numRead = in.read(tmpBuf, 0, tmpBuf.length);
                while( numRead != -1){
                    out.write(tmpBuf, 0, numRead);
                    numRead = in.read(tmpBuf, 0, tmpBuf.length);
                }
            } else {
                final StringBuilder varNameBuf = new StringBuilder();
                char c = readNext();
                mainLoop:while(!isEnd(c)){
                    if( c == '$'){
                        //lets see if the next bunch of chars match the terminal
                        final char nextChar = readNext();
                        if( !isEnd(nextChar) ){
                            if( nextChar == '{'){
                                //start of varName, lets read until the terminal
                                varNameBuf.setLength(0);
                                while( true ){
                                    final char endChar = readNext();
                                    if (isEnd(endChar)){
                                        out.write(c);
                                        out.write(nextChar);
                                        out.write(varNameBuf.toString());
                                        break mainLoop;
                                    } else if( endChar == '$'){
                                        //not the token expression, lets print stuff up
                                        //to this point out, and carry on processing
                                        out.write(c);
                                        out.write(nextChar);
                                        out.write(varNameBuf.toString());
                                        c = endChar;
                                        continue mainLoop;
                                    } else if( endChar == '}'){
                                        //we now have the whole tokenName
                                        final String varName = varNameBuf.toString().trim();
                                        if( vars.containsKey(varName)){
                                            final Object varVal = vars.get(varName);
                                            //only print out if there is content, else just leav the output empty
                                            if( varVal != null ){
                                                out.write(varVal.toString());
                                            }
                                        } else {
                                            //print the declaration as is
                                            out.write('$');
                                            out.write('{');
                                            out.write(varNameBuf.toString());
                                            out.write('}');
                                        }
                                        //carry on with the rest of the text
                                        c = readNext();
                                        continue mainLoop;
                                    } else {
                                        //lets keep collecting the tokens names
                                        varNameBuf.append(endChar);
                                    }
                                }
                            } else if( nextChar == '$'){
                                //maybe this is the start of the token?
                                //write out the content before this token, and carry on
                                out.write(c);
                                c = nextChar;//want the 'c=$' test to be run above
                                continue mainLoop;
                            } else {
                                //normal content, not start of token. Print out as is
                                out.write(c);
                                out.write(nextChar);
                                //and go on to next char
                                c = readNext();
                                continue mainLoop;
                            }
                        } else {
                            //end of content, just output what we have
                            out.write(c);
                            break mainLoop;
                        }
                    } else {
                        //normal content, just print as is
                        out.write(c);
                        //and go to next char
                        c = readNext();
                        continue mainLoop;
                    }
                }
            }
            out.flush();
        }

        private char readNext() throws IOException{
            return (char)in.read();
        }

        private boolean isEnd(final char c) throws IOException{
            return c < 0 || c >= 65535;
        }
    }
}

