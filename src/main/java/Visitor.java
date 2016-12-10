/**
 * Created by samrudhinayak on 12/4/16.
 */
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchPattern;



public class Visitor extends ASTVisitor {
    double operators=0;
    double summer =0;
    ASTParser parser = ASTParser.newParser(AST.JLS3);
    //get the total number of calls from java.util,java.io and java.lang
    public double getoperators()
    {
        return operators;
    }
    public double getSummer() { return summer;}
    public boolean visit(MethodDeclaration node) {
        int sam= node.getNodeType();
        //gets the total number of references to the node
        SearchPattern pattern = SearchPattern.createPattern(node.getName().toString(),
                IJavaSearchConstants.PACKAGE, IJavaSearchConstants.REFERENCES,
                SearchPattern.R_EXACT_MATCH);
                //checks what references are from java.lang
        SearchPattern pattern1 = SearchPattern.createPattern("java.lang.*",
                IJavaSearchConstants.PACKAGE, IJavaSearchConstants.REFERENCES,
                SearchPattern.R_EXACT_MATCH);
                //checks what references are from java.util
        SearchPattern pattern2 = SearchPattern.createPattern("java.util.*",
                IJavaSearchConstants.PACKAGE, IJavaSearchConstants.REFERENCES,
                SearchPattern.R_EXACT_MATCH);
                //checks what references are from java.io
        SearchPattern pattern3 = SearchPattern.createPattern("java.io.*",
                IJavaSearchConstants.PACKAGE, IJavaSearchConstants.REFERENCES,
                SearchPattern.R_EXACT_MATCH);
                //if the references are the same in node and java.lang, increment counter
        if(pattern1.matchesDecodedKey(pattern))
            operators = operators + 1;
             //if the references are the same in node and java.util, increment counter
        if(pattern2.matchesDecodedKey(pattern))
            operators = operators + 1;
             //if the references are the same in node and java.io, increment counter
        if(pattern3.matchesDecodedKey(pattern))
            operators = operators + 1;
        return true;
    }
    public boolean visit(MethodInvocation node) {
        summer=summer+1;
        //check all references to the node being invoked
        SearchPattern pattern = SearchPattern.createPattern(node.getName().toString(),
                IJavaSearchConstants.PACKAGE, IJavaSearchConstants.REFERENCES,
                SearchPattern.R_EXACT_MATCH);
        if(pattern!=null)
        //if the references exist, increment counter
        summer = summer +1;
        return true;
    }
    }


