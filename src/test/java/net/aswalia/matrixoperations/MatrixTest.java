/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package net.aswalia.matrixoperations;

import java.util.LinkedList;
import java.util.List;
import net.aswalia.matrixoperations.Matrix;
import net.aswalia.matrixoperations.MatrixElement;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author arvin
 */
public class MatrixTest {
    private String filename;
    private Matrix instance;
    
    public void testEquals() {
        double[][] a = {
            {1, 1},
            {1, 2}
        };
        double[][] b1 = {
            {1, 1, 1},
            {1, 2, 1}
        };
        assertEquals(Boolean.FALSE, new Matrix(a).equals(new Matrix(b1)), "Different sizes");
        double[][] b2 = {
            {1, 2},
            {1, 2}
        };
        assertEquals(Boolean.FALSE, new Matrix(a).equals(new Matrix(b2)), "Different content");
        double[][] b3 = null;
        assertEquals(Boolean.FALSE, new Matrix(a).equals(new Matrix(b3)), "null");
        assertEquals(Boolean.FALSE, new Matrix(a).equals(3.1415), "Different type");
        double[][] b4 = {
            {1, 1},
            {1, 2}
        };
        assertEquals(Boolean.TRUE, new Matrix(a).equals(new Matrix(b4)), "Equals");
    }

    /**
     * Test of getMatrixAsArray method, of class Matrix.
     */
    @Test
    public void testGetMatrixAsArray_UAT() throws Exception {
        System.out.println("getMatrixAsArray - UAT");
        filename = "src/test/resources/UAT.mat";
        instance = new Matrix(filename);
        double[][] expResult = {
            {1,1,0,0},
            {1,0,1,0},
            {0,1,0,1},
            {0,0,1,-1}
        };        
        double[][] result = instance.getMatrixAsArray();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getMatrixAsList method, of class Matrix.
     */
    @Test
    public void getMatrixAsList() throws Exception {
        System.out.println("getMatrixAsList - UAT");
        filename = "src/test/resources/UAT.mat";
        instance = new Matrix(filename);
//            {1,1,0,0},
//            {1,0,1,0},
//            {0,1,0,1},
//            {0,0,1,-1}
        List<MatrixElement> expResult = new LinkedList<>();
        expResult.add(new MatrixElement(1,1,1));
        expResult.add(new MatrixElement(1,2,1));
        expResult.add(new MatrixElement(1,3,0));
        expResult.add(new MatrixElement(1,4,0));
        expResult.add(new MatrixElement(2,1,1));
        expResult.add(new MatrixElement(2,2,0));
        expResult.add(new MatrixElement(2,3,1));
        expResult.add(new MatrixElement(2,4,0));
        expResult.add(new MatrixElement(3,1,0));
        expResult.add(new MatrixElement(3,2,1));
        expResult.add(new MatrixElement(3,3,0));
        expResult.add(new MatrixElement(3,4,1));
        expResult.add(new MatrixElement(4,1,0));
        expResult.add(new MatrixElement(4,2,0));
        expResult.add(new MatrixElement(4,3,1));
        expResult.add(new MatrixElement(4,4,-1));
        List<MatrixElement> result = instance.getMatrixAsList();
        assertEquals(expResult, result);
    }

    /**
     * Test of buildMatrix method, of class Matrix.
     */
    @Test
    public void testBuildMatrix_UAT() throws Exception {
        System.out.println("buildMatrix - UAT");
        filename = "src/test/resources/UAT.mat";
        instance = new Matrix(filename);
        LinkedList<MatrixElement> expResult = new LinkedList<>();
        expResult.add(new MatrixElement(1,1,1));
        expResult.add(new MatrixElement(1,2,1));
        expResult.add(new MatrixElement(1,3,0));
        expResult.add(new MatrixElement(1,4,0));
        expResult.add(new MatrixElement(2,1,1));
        expResult.add(new MatrixElement(2,2,0));
        expResult.add(new MatrixElement(2,3,1));
        expResult.add(new MatrixElement(2,4,0));
        expResult.add(new MatrixElement(3,1,0));
        expResult.add(new MatrixElement(3,2,1));
        expResult.add(new MatrixElement(3,3,0));
        expResult.add(new MatrixElement(3,4,1));
        expResult.add(new MatrixElement(4,1,0));
        expResult.add(new MatrixElement(4,2,0));
        expResult.add(new MatrixElement(4,3,1));
        expResult.add(new MatrixElement(4,4,-1));
        assertIterableEquals(expResult, instance.getMatrixAsList());
    }
    
    /**
     * Test of buildMatrix method, of class Matrix.
     */
    @Test
    public void testBuildMatrix_All() throws Exception {
        System.out.println("buildMatrix - All");
        filename = "src/test/resources/bells_and_wistles.mat";
        instance = new Matrix(filename);
        LinkedList<MatrixElement> expResult = new LinkedList<>();
        expResult.add(new MatrixElement(1,1,1.5));
        expResult.add(new MatrixElement(1,2,-2.01));
        expResult.add(new MatrixElement(1,3,0.34));
        expResult.add(new MatrixElement(2,1,-2.55));
        expResult.add(new MatrixElement(2,2,0.001));
        expResult.add(new MatrixElement(2,3,-0.001));
        expResult.add(new MatrixElement(3,1,1));
        expResult.add(new MatrixElement(3,2,1.01));
        expResult.add(new MatrixElement(3,3,-1.1));
        assertIterableEquals(expResult, instance.getMatrixAsList());
    }
    
    /**
     * Test of buildMatrix method, of class Matrix.
     */
    @Test
    public void testBuildMatrix_1x1_noWSNL() {
        System.out.println("buildMatrix - 1x1 - no WS or NL");
        filename = "src/test/resources/1x1-noWS_NL.mat";
        String expected = "Unexpected EOF: digit 6";
        try {
            instance = new Matrix(filename);
            fail("Exception expected");
        } catch (Exception ex) {
            assertEquals(expected, ex.getMessage());
        }
    }
    
    /**
     * Test of buildMatrix method, of class Matrix.
     */
    @Test
    public void testBuildMatrix_1x1_WS() throws Exception {
        System.out.println("buildMatrix - 1x1_WS");
        filename = "src/test/resources/1x1-WS.mat";
        instance = new Matrix(filename);
        LinkedList<MatrixElement> expResult = new LinkedList<>();
        expResult.add(new MatrixElement(1,1,-3.1415926));        
        assertIterableEquals(expResult, instance.getMatrixAsList());
    }
    
    /**
     * Test of buildMatrix method, of class Matrix.
     */
    @Test
    public void testBuildMatrix_1x1_NL() throws Exception {
        System.out.println("buildMatrix - 1x1_NL");
        filename = "src/test/resources/1x1-NL.mat";
        instance = new Matrix(filename);
        LinkedList<MatrixElement> expResult = new LinkedList<>();
        expResult.add(new MatrixElement(1,1,-3.1415926));        
        assertIterableEquals(expResult, instance.getMatrixAsList());
    }
    
    /**
     * Test of buildMatrix method, of class Matrix.
     */
    @Test
    public void testBuildMatrix_Empty() throws Exception {
        System.out.println("buildMatrix - Empty");
        filename = "src/test/resources/empty.mat";
        instance = new Matrix(filename);
        LinkedList<MatrixElement> expResult = new LinkedList<>();
        assertIterableEquals(expResult, instance.getMatrixAsList());
    }

    /**
     * Test of buildMatrix method, of class Matrix.
     */
    @Test
    public void testBuildMatrix_Error_Start_Number_With_Decimalpoint() {
        System.out.println("buildMatrix - Error - 01");
        String expected = "Parse error - expected \'digit\' Got: .";
        filename = "src/test/resources/error_1.mat";
        try {
            instance = new Matrix(filename);
            fail("Exception expected");
        } catch (Exception ex) {
            assertEquals(expected, ex.getMessage());
        }
    }

    /**
     * Test of buildMatrix method, of class Matrix.
     */
    @Test
    public void testBuildMatrix_Error_Number_With_Comma_As_Thousand_Separator() {
        System.out.println("buildMatrix - Error - 02");
        String expected = "Parse error - expected \'digit\' Got: ,";
        filename = "src/test/resources/error_2.mat";
        try {
            instance = new Matrix(filename);
            fail("Exception expected");
        } catch (Exception ex) {
            assertEquals(expected, ex.getMessage());
        }
    }
}
