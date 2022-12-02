package materialstore.tutorial;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestHelperTest {

    @Test
    public void testToClassNameToJobName() {
        Object obj = new Ex21_write_a_material_then_select_it();
        assertEquals("write_a_material_then_select_it",
                TestHelper.classNameToJobName(obj));
    }
}
