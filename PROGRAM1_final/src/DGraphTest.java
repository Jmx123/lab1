import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
/**
 * 
 */

/**
 * @author 111
 *
 */
public class DGraphTest {

	/**
	 * Test method for {@link DGraph#generateNewText(java.lang.String)}.
	 */
	private DGraph dGraph = new DGraph();
	@Before
	public void creatGraph(){
		String s = "to explore strange new worlds to seek out new life and new civilizations";
		String[] words = s.trim().split("\\s+");
		for (int i = 0; i < words.length - 1; i++) 
		{
			dGraph.addEdge(words[i], words[i+1]);
		}
	}
	
	@Test
	public void testGenerateNewText1() {
		String result = new String();
		//²âÊÔ×ÖÄ¸ºÍ¿Õ¸ñ±êµã
		result = dGraph.generateNewText("Seek to, explore new and exciting synergies");
		System.out.println(result);
		assertEquals("Seek to explore strange new life and exciting synergies",result);
	}
	@Test
	public void testGenerateNewText2() {
		String result = new String();
		//²âÊÔ×ÖÄ¸¿Õ¸ñºÍÆäËû×Ö·û
		result = dGraph.generateNewText("Seek to $explore new 3and exciting synergies");
		System.out.println(result);
		assertEquals("Seek to explore strange new life and exciting synergies",result);
	}
	
	@Test
	public void testGenerateNewText3() {
		String result = new String();
		//´¿×ÖÄ¸
		result = dGraph.generateNewText("Seek");
		System.out.println(result);
		assertEquals("the input text should be longer",result);
	}
	
	@Test
	public void testGenerateNewText4() {
		String result = new String();
		//·Ç×ÖÄ¸
		result = dGraph.generateNewText("23432$%");
		System.out.println(result);
		assertEquals("the input text should contain letter",result);
	}

}
