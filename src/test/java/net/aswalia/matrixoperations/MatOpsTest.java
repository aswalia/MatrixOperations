/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package net.aswalia.matrixoperations;

import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author asi
 */
public class MatOpsTest {

    private final double[][] a = {
        {1, 2, 3},
        {4, 5, 6},
        {7, 8, 9}
    };
    private final double[][] b = {
        {1, 1, 2},
        {2, 1, 2},
        {1, 1, 1}
    };

    private final Matrix A = new Matrix(a);
    private final Matrix B = new Matrix(b);

    @Test
    public void testSubMatrix() throws Exception {
        double[][] sm11 = {
            {5, 6},
            {8, 9}
        };
        assertEquals(new Matrix(sm11), MatOps.subMatrix(A, 1, 1));
        double[][] sm23 = {
            {1, 2},
            {7, 8}
        };
        assertEquals(new Matrix(sm23), MatOps.subMatrix(A, 2, 3));
        double[][] sm32 = {
            {1, 3},
            {4, 6}
        };
        assertEquals(new Matrix(sm32), MatOps.subMatrix(A, 3, 2));
        double[][] sm22 = {
            {1, 3},
            {7, 9}
        };
        assertEquals(new Matrix(sm22), MatOps.subMatrix(A, 2, 2));
        double[][] s4x4 = {
            {1, 2, 4},
            {5, 6, 8},
            {13, 14, 16}
        };
        double[][] fxf = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 16}
        };
        assertEquals(new Matrix(s4x4), MatOps.subMatrix(new Matrix(fxf), 3, 3));
    }

    /**
     * Test of det method, of class MatOps.
     */
    @Test
    public void testDet() throws Exception {
        // Det({{5,6},{8,9}}) = 5*9 - 8*6 = 45 - 48 = -3
        double[][] a1 = {
            {5, 6},
            {8, 9}
        };
        assertEquals(-3, MatOps.det(new Matrix(a1)));
        // Det(A) = 0
        // 1*Det({{5,6},{8,9})-2*Det({{4,6},{7,9}})+3*Det({{4,5},{7,8}}) = 1*(-3)-2*(-6)+3*(-3) = -3+12-9 = 0
        assertEquals(0, MatOps.det(A));
        // Det(B) = 1
        // 1*Det({{1,2},{1,1})-1*Det({{2,2},{1,1}})+2*Det({{2,1},{1,1}}) = 1*(-1)-1*0+2*1 = -1+0+2 = 1
        assertEquals(1, MatOps.det(B));
        double[][] fxf = {
            {1, 2, 3, 4},
            {1, 4, 9, 16},
            {1, 8, 27, 64},
            {4, 6, 8, 10}
        };
        // Det(fxf) = -24
        assertEquals(-24, MatOps.det(new Matrix(fxf)));
    }

    /**
     * Test of trans method, of class MatOps.
     */
    @Test
    public void testTrans() throws Exception {
        double[][] at = {
            {1, 4, 7},
            {2, 5, 8},
            {3, 6, 9}
        };
        assertEquals(new Matrix(at), MatOps.trans(A));
    }

    /**
     * Test of vectMult method, of class MatOps.
     */
    @Test
    public void testVectMult() throws Exception {
        // mult of 3rd row of B with 3rd row of A gives 24 (1*7 + 1*8 + 1*9)
        assertEquals(24, MatOps.vectMult(B.row(2), A.row(2)));
        // mult of 3rd row of B with 3rd column of A gives 18 (1*3 + 1*6 + 1*9)
        assertEquals(18, MatOps.vectMult(B.row(2), A.column(2)));
        // mult of 1st row of A with 1st column of B gives 8 (1*1 + 2*2 + 3*1)
        assertEquals(8, MatOps.vectMult(A.row(0), B.column(0)));
    }

    /**
     * Test of mult method, of class MatOps.
     */
    @Test
    public void testMult() throws Exception {
        // mm = matrix A mult by A's trans
        double[][] mm = {
            {14, 32, 50},
            {32, 77, 122},
            {50, 122, 194}
        };
        assertEquals(new Matrix(mm), MatOps.mult(A, MatOps.trans(A)));
        // ab = A*B
        double[][] ab = {
            {8, 6, 9},
            {20, 15, 24},
            {32, 24, 39}
        };
        assertEquals(new Matrix(ab), MatOps.mult(A, B));
    }

    /**
     * Test of inv method, of class MatOps.
     */
    @Test
    public void testInv() throws Exception {
        // Inverse of A does not exist
        assertEquals(null, MatOps.inv(A));
        // A 3x3 Identity matrix 
        List<MatrixElement> Id = new LinkedList<>();
        Id.add(new MatrixElement(1,1,1.0));
        Id.add(new MatrixElement(2,2,1.0));
        Id.add(new MatrixElement(3,3,1.0));
        assertEquals(new Matrix(Id), MatOps.mult(B, MatOps.inv(B)));
        assertEquals(new Matrix(Id), MatOps.mult(MatOps.inv(B), B));
        double[][] m = {
            {3, 0, 2},
            {2, 0, -2},
            {0, 1, 1}
        };
        assertEquals(new Matrix(Id), MatOps.mult(new Matrix(m), MatOps.inv(new Matrix(m))));
        assertEquals(new Matrix(Id), MatOps.mult(MatOps.inv(new Matrix(m)), new Matrix(m)));
        double[][] f = {
            {1, 1, 2, 1},
            {2, 1, 2, 1},
            {1, 1, 1, 2},
            {2, 1, 2, 2}
        };
        // A 4x4 Identity matrix 
        Id.add(new MatrixElement(4,4,1.0));
        assertEquals(new Matrix(Id), MatOps.mult(new Matrix(f), MatOps.inv(new Matrix(f))));
        assertEquals(new Matrix(Id), MatOps.mult(MatOps.inv(new Matrix(f)), new Matrix(f)));
    }

    /**
     * Test of add method, of class MatOps.
     */
    @Test
    public void testAdd() throws Exception {
        double[][] sum = {
            {2, 3, 5},
            {6, 6, 8},
            {8, 9, 10}
        };
        assertEquals(new Matrix(sum), MatOps.add(A, B));
    }

    /**
     * Test of sub method, of class MatOps.
     */
    @Test
    public void testSub() throws Exception {
        double[][] sub = {
            {0, 1, 1},
            {2, 4, 4},
            {6, 7, 8}
        };
        assertEquals(new Matrix(sub), MatOps.sub(A, B));
    }
    
    @Test
    public void testEnd2End() throws Exception {
        System.out.println("End to end test");
        String filename = "src/test/resources/10x10"
                + ".mat";
        System.out.println("Before matrix build: " + System.currentTimeMillis());
        Matrix m = new Matrix(filename);
        System.out.println("After matrix build:  " + System.currentTimeMillis());
        System.out.println("Matrix m:\n" + m);
        System.out.println();
        System.out.println("Before matrix determinant: " + System.currentTimeMillis());
        System.out.println("Determinant: " + MatOps.det(m));
        System.out.println("After matrix determinant:  " + System.currentTimeMillis());
        System.out.println();
        System.out.println("Before inverse matrix: " + System.currentTimeMillis());
        Matrix mm1 = MatOps.inv(m);
        System.out.println("After inverse matrix:  " + System.currentTimeMillis());
        System.out.println("Inverse of m:\n" + mm1);
        if (mm1 != null) {
            System.out.println();
            System.out.println("Before matrix mult: " + System.currentTimeMillis());
            System.out.println("m X inv(m):\n" + MatOps.mult(m, mm1));
            System.out.println("After matrix mult:  " + System.currentTimeMillis());
        }
    }
}
