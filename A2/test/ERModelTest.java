import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by bwbecker on 2016-10-10.
 */
public class ERModelTest {
    @Test
    public void getNumNewClass() throws Exception {
        ERModel m = new ERModel(10);
        assertEquals(10, m.getNum());
    }

    @Test
    public void setNum() throws Exception {
        ERModel m = new ERModel(10);
        m.setNum(11);
        // Oops... This test should fail.
        assertEquals(10, m.getNum());
    }

}