import java.util.*;

public class BCNF {

  /**
   * Implement your algorithm here
   **/
  
	public static ArrayList<ArrayList<Attribute>> findSubset(AttributeSet attributeSet){
		ArrayList<ArrayList<Attribute>> result = new ArrayList<>();
		
		ArrayList<Attribute> myList = new ArrayList<>();
		Iterator<Attribute> attrIterator = attributeSet.iterator();
		while (attrIterator.hasNext()) {
			myList.add(attrIterator.next());
		}
		ArrayList<Attribute> newList = new ArrayList<>();
		allSubHelper(myList, newList, 0, result);
		
		return result;
	}
	
	public static void allSubHelper(List<Attribute> myList, ArrayList<Attribute> newList, int index, ArrayList<ArrayList<Attribute>> result){
		if (index >= myList.size()) {
			result.add(newList);
		} else {
			allSubHelper(myList, newList, index + 1, result);
			ArrayList<Attribute> newListAdded = new ArrayList(newList);
			newListAdded.add(myList.get(index));
			allSubHelper(myList, newListAdded, index + 1, result);
		}
	}
	
	public static Set<AttributeSet> decompose(AttributeSet attributeSet,
                                            Set<FunctionalDependency> functionalDependencies) {
      // If attribute set is null or functional dependencies are null
	if (attributeSet == null || functionalDependencies == null) {
		return null;
	}
	
	

	// Create a new map for decomposition
	Map<AttributeSet, Set<FunctionalDependency>> setDependencyMap = new HashMap<>();

	// Copy an attribute set
	AttributeSet attributeSetCopy = new AttributeSet(attributeSet);
	// Initialize decomposing set of attributes
	setDependencyMap.put(attributeSetCopy, functionalDependencies);
      // Save a violated flag
	boolean violated;

	do {
		violated = false;
		// an iterator gives out attributes set (initial one)
		Iterator<AttributeSet> attributeSetIterator = setDependencyMap.keySet().iterator();
		// If any attribute is not done
		while(attributeSetIterator.hasNext()) {
			// get a pair of attr set and fd 
			AttributeSet attrSet = attributeSetIterator.next();
			Set<FunctionalDependency> setFunctionalDependencies = setDependencyMap.get(attrSet);
			Iterator<FunctionalDependency> dependencyIterator = setFunctionalDependencies.iterator();
			// check for each fd
			while (dependencyIterator.hasNext()) {
				
				FunctionalDependency dependency = dependencyIterator.next();
				AttributeSet independent = dependency.independent();
				AttributeSet independentClosure = BCNF.closure(independent, setFunctionalDependencies);
				
				// If independent's closure does not equal to attributeSet, it is not a super key, then violates BCNF
				if (!attrSet.equals(independentClosure)) {
					violated = true;
					// Modify map
					AttributeSet dependent = dependency.dependent();
					Iterator<Attribute> dependentIterator = dependent.iterator();
					// Add both independent and dependent to the new attribute set
					AttributeSet newAttributeSet = new AttributeSet(independent);
					while (dependentIterator.hasNext()) {
						newAttributeSet.addAttribute(dependentIterator.next());
					}
					Set<FunctionalDependency> newDependencySet = new HashSet<>();
					newDependencySet.add(dependency);
					setDependencyMap.put(newAttributeSet, newDependencySet);
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
			if (violated) {
				break;
			}
		}
	} while (violated);
	
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

      // Initialize closure attribute set
	AttributeSet closure = new AttributeSet(attributeSet);
	int closureSize;
	
//	// Test
//	System.out.println("Closure Function");
//	System.out.println("AttributeSet: " + closure.toString());

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
	
//	// Test
//	System.out.println("--After: " + closure.toString());
	return closure;
  }
}
