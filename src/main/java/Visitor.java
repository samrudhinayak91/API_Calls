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
    public double getoperators()
    {
        return operators;
    }
    public double getSummer() { return summer;}
    public boolean visit(MethodDeclaration node) {
        int sam= node.getNodeType();
        SearchPattern pattern = SearchPattern.createPattern(node.getName().toString(),
                IJavaSearchConstants.PACKAGE, IJavaSearchConstants.REFERENCES,
                SearchPattern.R_EXACT_MATCH);
        SearchPattern pattern1 = SearchPattern.createPattern("java.lang.*",
                IJavaSearchConstants.PACKAGE, IJavaSearchConstants.REFERENCES,
                SearchPattern.R_EXACT_MATCH);
        SearchPattern pattern2 = SearchPattern.createPattern("java.util.*",
                IJavaSearchConstants.PACKAGE, IJavaSearchConstants.REFERENCES,
                SearchPattern.R_EXACT_MATCH);
        SearchPattern pattern3 = SearchPattern.createPattern("java.io.*",
                IJavaSearchConstants.PACKAGE, IJavaSearchConstants.REFERENCES,
                SearchPattern.R_EXACT_MATCH);
        if(pattern1.matchesDecodedKey(pattern))
            operators = operators + 1;
        if(pattern2.matchesDecodedKey(pattern))
            operators = operators + 1;
        if(pattern3.matchesDecodedKey(pattern))
            operators = operators + 1;
        return true;
    }
    public boolean visit(MethodInvocation node) {
        summer=summer+1;
        SearchPattern pattern = SearchPattern.createPattern(node.getName().toString(),
                IJavaSearchConstants.PACKAGE, IJavaSearchConstants.REFERENCES,
                SearchPattern.R_EXACT_MATCH);
        if(pattern!=null)
        summer = summer +1;
        return true;
    }
    }


