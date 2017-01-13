package solutions.heavywater.test.junit;

import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestWebService {
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        System.out.println("Started Testing");
    }
    @Test
    public void A_test() {
        assertTrue(true);
    }

    @Test
    public void B_test() {
        assertTrue(true);
    }
}

