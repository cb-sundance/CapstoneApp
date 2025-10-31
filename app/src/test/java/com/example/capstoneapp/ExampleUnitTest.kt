package your.package.name

<<<<<<< HEAD

=======
>>>>>>> eb5e9b5a92e3c530cf369203c5bafa8749075ab0
import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        // simple unit test example
        val a = 2
        val b = 3
        val expected = 5
        assertEquals(expected, a + b)
    }

    @Test
    fun formatMessage_returnsExpected() {
        val input = "Carissa"
        val result = "Hello, $input!"
        // pretend there's a function to test:
        // assertEquals(result, Utils.formatGreeting(input))
        // since this is a template, assert a known value
        assertEquals("Hello, Carissa!", result)
    }
}
