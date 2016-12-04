import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.Scanner;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;


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
        Map options=JavaCore.getOptions();
        //used to set default compiler options
        JavaCore.setComplianceOptions(JavaCore.VERSION_1_5, options);
        parser.setCompilerOptions(options);
        //create AST
        CompilationUnit cu = (CompilationUnit) parser.createAST(null);
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
    public double getproglength(int totaloperators,int totaloperands)
    {
        return (totaloperators+totaloperands);
    }
    public double getprogvocab(double namesize,double snamesize)
    {
        return namesize+snamesize;
    }
    public double getprogdiff(double namesize,double snamesize,int totaloperands)
    {
        return (snamesize/2)*(totaloperands/namesize);
    }
    public double getvol(double proglength,double progvocab)
    {
        return proglength*(Math.log(progvocab)/Math.log(2));
    }
    public double getcalc(double namesize,double snamesize)
    {
        return ((namesize)*(Math.log(namesize)/Math.log(2)))+((snamesize)*(Math.log(snamesize)/Math.log(2)));
    }
    public double geteffort(double difficulty,double volume)
    {
        return difficulty*volume;
    }
    public double gettimetoprog(double effort)
    {
        return effort/18;
    }
    public double getbugs(double volume)
    {
        return volume/3000;
    }
    public static void main(String args[]) throws IOException{
        Visitor visitor=new Visitor();
        TestParser tp=new TestParser();
        String dirname;
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter the directory path");
        dirname=in.nextLine();
        File inputFile= new File(dirname);
        //send directory name to the method
        listFilesForFolder(inputFile, visitor);
        //get the metrics to calculate halstead complexity
        int totaloperators=visitor.getoperators();
        int totaloperands=visitor.getoperands();
        Set names=visitor.getNames();
        Set snames=visitor.getSnames();
        double namesize=names.size();
        double snamesize=snames.size();
        double proglength=tp.getproglength(totaloperators, totaloperands);
        double progvocab=tp.getprogvocab(namesize, snamesize);
        double difficulty=tp.getprogdiff(namesize, snamesize, totaloperands);
        double volume=tp.getvol(proglength, progvocab);
        double calcproglen=tp.getcalc(namesize, snamesize);
        double effort=tp.geteffort(difficulty, volume);
        double timetoprog=tp.gettimetoprog(effort);
        double sysbugs=tp.getbugs(volume);
        double totalsummer = visitor.getSummer();
        double totalifs = visitor.getoperators();
        System.out.println("The total summer are :" + totalsummer);
        System.out.println("IFS  " + totalifs);
    }
}