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
	Map<AttributeSet, Set<FunctionalDependency>> setDependencyMap = new HashMap<>();

	// Copy an attribute set
	AttributeSet attributeSetCopy = new AttributeSet(attributeSet);
	// Initialize decomposing set of attributes
	setDependencyMap.put(attributeSetCopy, functionalDependencies);
	boolean violated = false;

	do {
		violated = false;
		attributeSetIterator = setDependencyMap.keySet().iterator();
		while(attributeSetIterator.hasNext()) {
			AttributeSet attrSet = attributeSetIterator.next();
			Set<FunctionalDependency> setFunctionalDependencies = setDependencyMap.get(attrSet);
			Iterator<FunctionalDependency> dependencyIterator = setFunctionalDependencies.iterator();
			while (dependencyIterator.hasNext()) {
				FunctionalDependency dependency = dependencyIterator.next();
				AttributeSet independent = dependency.independent();
				AttributeSet independentClosure = BCNF.closure(independent, setFunctionalDependencies);
				// If independent's closure does not equal to attributeSet, it is not a super key
				if (!attrSet.equals(independentClosure)) {
					violated = true;
					// Modify map
					AttributeSet dependent = dependency.dependent();
					Iterator<AttributeSet> dependentIterator = dependent.iterator();
					// Add both independent and dependent to the new attribute set
					AttributeSet newAttributeSet = new AttributeSet(independent);
					while (dependentIterator.hasNext()) {
						newAttributeSet.addAttribute(dependentIterator.next());
					}
					setDependencyMap.put(newAttributeSet, dependency);
					// Remove current dependency from previous set
					dependencyIterator.remove();
					// Remove elements from current attrSet
					Iterator<Attribute> attributeIterator = attrSet.iterator();
					while (attributeIterator.hasNext()) {
						Attribute attribute = attributeIterator.next();
						if (dependent.contains(attribute)) {
							attributeIterator.remove();
						}
					}
					break;
				}
			}
			if (violated == true) {
				break;
			}
		}
	} while (violated != false);
	
	return setDependencyMap.keySet();
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
