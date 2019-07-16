import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;


public class NormlizeWord {
	// the files containing prefixes, suffixes and so on
    private Vector staticFiles;
    NormlizeWord()
    {
    	readInStaticFiles ();
    }
    public String normlizeWord(String word)
    {
    	return formatWord(word);
    }
    // format the word by removing any punctuation, diacritics and non-letter charracters
    private String formatWord ( String currentWord)
    {
        StringBuffer modifiedWord = new StringBuffer ( );

        // remove any diacritics (short vowels)
        if ( removeDiacritics( currentWord, modifiedWord ) )
        {
            currentWord = modifiedWord.toString ( );
        }

        // remove any punctuation from the word
        if ( removePunctuation( currentWord, modifiedWord ) )
        {
           currentWord = modifiedWord.toString ( );
        }

        // there could also be characters that aren't letters which should be removed
        if ( removeNonLetter ( currentWord, modifiedWord ) )
        {
           currentWord = modifiedWord.toString ( );
        }

                   // check for stopwords
            if( !checkStopwords ( currentWord ) )
                currentWord = stemWord ( currentWord );
            //else
            //	currentWord="stopword";
           
        return currentWord;
    }

    //--------------------------------------------------------------------------

    // stem the word
    private String stemWord ( String word )
    {
   
        
            // check for a definite article, and remove it
            word = checkDefiniteArticle ( word );
      
        // if the root STILL hasnt' been found
       
            // check for suffixes
            word = checkForSuffixes ( word );
        
            // check for prefixes
            word = checkForPrefixes ( word );
       
        
        return word;
    }


    
	 private void readInStaticFiles ( )
	    {
	        // create a string buffer containing the path to the static files
	        String pathToStemmerFiles = new StringBuffer ( System.getProperty ( "user.dir" ) + System.getProperty ( "file.separator" ) + "StemmerFiles" + System.getProperty ( "file.separator" ) ).toString ( );

	        // create the vector composed of vectors containing the static files
	        staticFiles = new Vector ( );
	        if ( addVectorFromFile ( new StringBuffer ( pathToStemmerFiles + "definite_article.txt" ).toString ( ) ) )
	        if ( addVectorFromFile ( new StringBuffer ( pathToStemmerFiles + "prefixes.txt" ).toString ( ) ) )
	        if ( addVectorFromFile ( new StringBuffer ( pathToStemmerFiles + "punctuation.txt" ).toString ( ) ) )
	        if ( addVectorFromFile ( new StringBuffer ( pathToStemmerFiles + "stopwords.txt" ).toString ( ) ) )
	        if ( addVectorFromFile ( new StringBuffer ( pathToStemmerFiles + "suffixes.txt" ).toString ( ) ) )
	        if ( addVectorFromFile ( new StringBuffer ( pathToStemmerFiles + "diacritics.txt" ).toString ( ) ) )
	        {
	            // the vector was successfully created
	            //System.out.println( "read in files successfully" );
	        }
	    }
	 private boolean addVectorFromFile ( String fileName )
	    {
	        boolean returnValue;
	        try
	        {
	            // the vector we are going to fill
	            Vector vectorFromFile = new Vector ( );

	            // create a buffered reader
	            File file = new File ( fileName );
	            FileInputStream fileInputStream = new FileInputStream ( file );
	            InputStreamReader inputStreamReader = new InputStreamReader ( fileInputStream, "UTF-16" );

	            //If the bufferedReader is not big enough for a file, I should change the size of it here
	            BufferedReader bufferedReader = new BufferedReader ( inputStreamReader, 20000 );

	            // read in the text a line at a time
	            String part;
	            StringBuffer word = new StringBuffer ( );
	            while ( ( part = bufferedReader.readLine ( ) ) != null )
	            {
	                // add spaces at the end of the line
	                part = part + "  ";

	                // for each line
	                for ( int index = 0; index < part.length ( ) - 1; index ++ )
	                {
	                    // if the character is not a space, append it to a word
	                    if ( ! ( Character.isWhitespace ( part.charAt ( index ) ) ) )
	                    {
	                        word.append ( part.charAt ( index ) );
	                    }
	                    // otherwise, if the word contains some characters, add it
	                    // to the vector
	                    else
	                    {
	                        if ( word.length ( ) != 0 )
	                        {
	                            vectorFromFile.addElement ( word.toString ( ) );
	                            word.setLength ( 0 );
	                        }
	                    }
	                }
	            }

	            // trim the vector
	            vectorFromFile.trimToSize ( );

	            // destroy the buffered reader
	            bufferedReader.close ( );
	   	        fileInputStream.close ( );

	            // add the vector to the vector composed of vectors containing the
	            // static files
	            staticFiles.addElement ( vectorFromFile );
	            returnValue = true;
	        }
	        catch ( Exception exception )
	        {
	           // JOptionPane.showMessageDialog ( arabicStemmerGUI, "Could not open '" + fileName + "'.", " Error ", JOptionPane.ERROR_MESSAGE );
	            returnValue = false;
	        }
	        return returnValue;
	    }
	
	 // check and remove any prefixes
	    private String checkForPrefixes ( String word )
	    {
	        String prefix = "";
	        String modifiedWord = word;
	        Vector prefixes = ( Vector ) staticFiles.elementAt ( 1 );

	        // for every prefix in the list
	        for ( int i = 0; i < prefixes.size ( ); i++ )
	        {
	            prefix = ( String ) prefixes.elementAt ( i );
	            // if the prefix was found
	            if ( prefix.regionMatches ( 0, modifiedWord, 0, prefix.length ( ) ) )
	            {
	                modifiedWord = modifiedWord.substring ( prefix.length ( ) );
	               
	
	            }
	        }
	        if ( modifiedWord.length ( ) > 4 )
	            return modifiedWord;
	        return word;
	    }

	    //--------------------------------------------------------------------------

	    // METHOD CHECKFORSUFFIXES
	    private String checkForSuffixes ( String word )
	    {
	        String suffix = "";
	        String modifiedWord = word;
	        Vector suffixes = ( Vector ) staticFiles.elementAt ( 4 );
	       
	       
	        // for every suffix in the list
	        for ( int i = 0; i < suffixes.size ( ); i++ )
	        {
	            suffix = ( String ) suffixes.elementAt ( i );

	            // if the suffix was found
	            if( suffix.regionMatches ( 0, modifiedWord, modifiedWord.length ( ) - suffix.length ( ), suffix.length ( ) ) )
	            {
	            	

	                modifiedWord = modifiedWord.substring ( 0, modifiedWord.length ( ) - suffix.length ( ) );
	
	                
	            }
	        }
	        if ( modifiedWord.length ( ) >= 3 )//=3
	            return modifiedWord;
	        return word;
	    }
	   
	    //--------------------------------------------------------------------------
	    // check and remove the definite article
	    private String checkDefiniteArticle ( String word )
	    {
	        // looking through the vector of definite articles
	        // search through each definite article, and try and
	        // find a match
	        String definiteArticle = "";
	        String modifiedWord = "";
	        Vector definiteArticles = ( Vector ) staticFiles.elementAt ( 0 );

	        // for every definite article in the list
	        for ( int i = 0; i < definiteArticles.size ( ); i++ )
	        {
	            definiteArticle = ( String ) definiteArticles.elementAt ( i );
	            // if the definite article was found
	            if ( definiteArticle.regionMatches ( 0, word, 0, definiteArticle.length ( ) ) )
	            {
	                // remove the definite article
	                modifiedWord = word.substring ( definiteArticle.length ( ), word.length ( ) );
	
	            }
	        }
	        if ( modifiedWord.length ( ) >= 3 )
	            return modifiedWord;
	        return word;
	    }
	    // remove non-letters from the word
	    private boolean removeNonLetter ( String currentWord, StringBuffer modifiedWord )
	    {
	        boolean nonLetterFound = false;
	        modifiedWord.setLength ( 0 );

	        // if any of the word is not a letter then remove it
	        for( int i = 0; i < currentWord.length ( ); i++ )
	        {
	            if ( Character.isLetter ( currentWord.charAt ( i ) ) )
	            {
	                modifiedWord.append ( currentWord.charAt ( i ) );
	               
	            }
	            else
	            {
	                nonLetterFound = true;
	               
	            }
	        }
	        return nonLetterFound;
	    }

	    // remove punctuation from the word
	    private boolean removePunctuation ( String currentWord, StringBuffer modifiedWord )
	    {
	        boolean punctuationFound = false;
	        modifiedWord.setLength ( 0 );
	        Vector punctuations = ( Vector ) staticFiles.elementAt ( 2 );

	        // for every character in the current word, if it is a punctuation then do nothing
	        // otherwise, copy this character to the modified word
	        for ( int i = 0; i < currentWord.length ( ); i++ )
	        {
	            if ( ! ( punctuations.contains ( currentWord.substring ( i, i+1 ) ) ) )
	            {
	                modifiedWord.append ( currentWord.charAt ( i ) );
	               
	            }
	            else
	            {
	                punctuationFound = true;
	                
	            }
	        }

	        return punctuationFound;
	    }
	    // remove diacritics from the word
	    private boolean removeDiacritics ( String currentWord, StringBuffer modifiedWord )
	    {
	        boolean diacriticFound = false;
	        modifiedWord.setLength ( 0 );
	        Vector diacritics = ( Vector ) staticFiles.elementAt ( 5 );

	        for ( int i = 0; i < currentWord.length ( ); i++ )
	            // if the character is not a diacritic, append it to modified word
	            if ( ! ( diacritics.contains ( currentWord.substring ( i, i+1 ) ) ) )
	                modifiedWord.append ( currentWord.substring ( i, i+1 ) );
	            else
	            {
	                diacriticFound = true;
	            }
	        return diacriticFound;
	    }

	    //--------------------------------------------------------------------------

	    // check that the word is a stopword
	    private boolean checkStopwords ( String currentWord )
	    {
	    	boolean stopwordFound= false;
	        Vector v = ( Vector ) staticFiles.elementAt ( 3 );

	        if ( stopwordFound = v.contains ( currentWord ) )
	        {
	        	stopwordFound= true;
	         }
	        return stopwordFound;
	    }
}
