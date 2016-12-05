import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

/**
 * Created by samrudhinayak on 12/4/16.
 */
public class TestParser {
    public static void parse(String pathname, Visitor visitor) throws IOException
    {
        //pass the string to locate the directory
        File file= new File(pathname);
        //read the file in which it is present
        BufferedReader in=new BufferedReader(new FileReader(file));
        //convert to string
        final StringBuffer buff= new StringBuffer();
        String line = null;
        //separate the code to individual lines
        while(null != (line = in.readLine()))
        {
            buff.append(line).append("\n");
        }
        //create ASTParser
        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        //convert the buffer to a string of characters
        parser.setSource(buff.toString().toCharArray());
        //used to resolve scope issues
        parser.setResolveBindings(true);
        //used to access the current values of options
        Map options= JavaCore.getOptions();
        //used to set default compiler options
        JavaCore.setComplianceOptions(JavaCore.VERSION_1_5, options);
        parser.setCompilerOptions(options);
        //create AST
        CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        cu.getCommentList();
        //System.out.println(cu.getCommentList().size());
        //System.out.println(cu.getExtendedLength(node));
        //visitor to visit each node
        cu.accept(visitor);
    }
    //method to take in multiple files in a folder
    public static void listFilesForFolder(final File folder, Visitor visitor) throws IOException {
        //iterate through the files, if file is a directory, iterate through each file in the directory
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                //System.out.println("Encountered directory");
                listFilesForFolder(fileEntry,visitor);
            } else if (fileEntry.getName().endsWith("java")){
                //get the absolute path to parse
                String pathname=fileEntry.getAbsolutePath();
                parse(pathname, visitor);
            }
        }
    }
}
