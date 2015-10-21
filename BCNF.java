import java.util.*;

public class BCNF {

  /**
   * Implement your algorithm here
   **/
  public static Set<AttributeSet> decompose(AttributeSet attributeSet,
                                            Set<FunctionalDependency> functionalDependencies) {
	if (attributeSet == null || functionalDependencies == null) {
		return null;
	}

	// Create a new set for decomposition
	Set<AttributeSet> setOfAttributeSet = new HashSet<>();

	// Copy an attribute set
	AttributeSet attributeSetCopy = new AttributeSet(attributeSet);
	int violates = 0;
	// Initialize decomposing set of attributes
	setOfAttributeSet.add(attributeSetCopy);

	do {
		violates = 0;
		for (FunctionalDependency dependency : functionalDependencies) {
			AttributeSet independent = dependency.independent();
			AttributeSet independentClosure = BCNF.closure(independent, functionalDependencies);
			// If independent's closure does not equal to attributeSet, it is not a super key
			if (!attributeSetCopy.equals(independentClosure)) {
				violates++;
				
			}
		}
	} while (violates != 0);
	

	return setOfAttributeSet;
  }

  /**
   * Recommended helper method
   **/
  public static AttributeSet closure(AttributeSet attributeSet, Set<FunctionalDependency> functionalDependencies) {
    // TODO: implement me!
	if (attributeSet == null || functionalDependencies == null) {
		return null;
	}

	AttributeSet closure = new AttributeSet(attributeSet);
	int closureSize = closure.size();

	do {
		closureSize = closure.size();
		for(FunctionalDependency dependency : functionalDependencies) {
			AttributeSet independent = dependency.independent();
			AttributeSet dependent = dependency.dependent();
			// Get independent's iterator
			Iterator<Attribute> attributeIterator = independent.iterator();
			int containsAttributes = 0;
			// If independent contains more elements, independent cannot be included in the closure
			if (closure.size() < independent.size()) {
				continue;
			}
			// Check if independent has all attributes in the closure set
			while (attributeIterator.hasNext()) {
				Attribute nextAttribute = attributeIterator.next();
				if (closure.contains(nextAttribute)) {
					containsAttributes++;
				}
			}
			// If all attributes are contained in the independent, add all attributes from dependent
			if (containsAttributes == independent.size()) {
				attributeIterator = dependent.iterator();
				while (attributeIterator.hasNext()) {
					Attribute nextDependentAttribute = attributeIterator.next();
					closure.addAttribute(nextDependentAttribute);
				}
			}

		}

	} while (closureSize != closure.size());
	return closure;
  }
}
