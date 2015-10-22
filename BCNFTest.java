import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class BCNFTest {
  /**
   * Performs a basic test on a simple table.
   * gives input attributes (a,b,c) and functional dependency a->c
   * and expects output (a,c),(b,c) or any reordering
   **/
  @org.junit.Test
  public void testSimpleBCNF() {
    //construct table
    AttributeSet attrs = new AttributeSet();
    attrs.addAttribute(new Attribute("a"));
    attrs.addAttribute(new Attribute("b"));
    attrs.addAttribute(new Attribute("c"));

    //create functional dependencies
    Set<FunctionalDependency> fds = new HashSet<>();
    AttributeSet ind = new AttributeSet();
    AttributeSet dep = new AttributeSet();
    ind.addAttribute(new Attribute("a"));
    dep.addAttribute(new Attribute("c"));
    FunctionalDependency fd = new FunctionalDependency(ind, dep);
    fds.add(fd);

    //run client code
    Set<AttributeSet> bcnf = BCNF.decompose(attrs, fds);

    //verify output
    assertEquals("Incorrect number of tables", 2, bcnf.size());

    for(AttributeSet as : bcnf) {
      assertEquals("Incorrect number of attributes", 2, as.size());
      assertTrue("Incorrect table", as.contains(new Attribute("a")));
    }

  }

    @org.junit.Test
    public void testSimpleBCNF2() {
        AttributeSet attrs = new AttributeSet();
        attrs.addAttribute(new Attribute("a"));
        attrs.addAttribute(new Attribute("c"));
        attrs.addAttribute(new Attribute("b"));
        attrs.addAttribute(new Attribute("d"));
        attrs.addAttribute(new Attribute("e"));

        Set<FunctionalDependency> fds = new HashSet<>();
        AttributeSet ind = new AttributeSet();
        AttributeSet dep = new AttributeSet();
        ind.addAttribute(new Attribute("a"));
        dep.addAttribute(new Attribute("b"));
        FunctionalDependency fd = new FunctionalDependency(ind, dep);
        fds.add(fd);
        ind = new AttributeSet();
        dep = new AttributeSet();
        ind.addAttribute(new Attribute("c"));
        dep.addAttribute(new Attribute("d"));
        fd = new FunctionalDependency(ind, dep);
        fds.add(fd);

        Set<AttributeSet> bcnf = BCNF.decompose(attrs, fds);

        assertEquals("Incorrect number of tables", 3, bcnf.size());
        for (AttributeSet as : bcnf) {
            assertTrue(as.size() == 2 || as.size() == 3);
            if (as.size() == 3) {
                assertTrue("Missing a", as.contains(new Attribute("a")));
                assertTrue("Missing c", as.contains(new Attribute("c")));
                assertTrue("Missing e", as.contains(new Attribute("e")));
            } else {
                assertTrue("A->B and C->D", (as.contains(new Attribute("a")) && as.contains(new Attribute("b"))) || (as.contains(new Attribute("c")) && as.contains(new Attribute("d"))));
            }
        }
    }

}