import java.util.*;

public class BCNF {

	/**
	 * Implement your algorithm here
	 **/

	public static HashSet<HashSet<Attribute>> findSubset(AttributeSet attributeSet) {
		HashSet<HashSet<Attribute>> result = new HashSet<>();

		ArrayList<Attribute> myList = new ArrayList<>();
		Iterator<Attribute> attrIterator = attributeSet.iterator();
		while (attrIterator.hasNext()) {
			myList.add(attrIterator.next());
		}
		HashSet<Attribute> newList = new HashSet<>();
		allSubHelper(myList, newList, 0, result);

		return result;
	}

	public static void allSubHelper(ArrayList<Attribute> myList, HashSet<Attribute> newList, int index,
			HashSet<HashSet<Attribute>> result) {
		if (index >= myList.size()) {
			result.add(newList);
		} else {
			allSubHelper(myList, newList, index + 1, result);
			HashSet<Attribute> newListAdded = new HashSet(newList);
			newListAdded.add(myList.get(index));
			allSubHelper(myList, newListAdded, index + 1, result);
		}
	}

	public static Set<AttributeSet> decompose(AttributeSet attributeSet, Set<FunctionalDependency> functionalDependencies) {
		// If attribute set is null or functional dependencies are null
		if (attributeSet == null || functionalDependencies == null) {
			return null;
		}
		HashSet<HashSet<Attribute>> allSubSets = findSubset(attributeSet);
		Iterator<HashSet<Attribute>> allSubSetIterator = allSubSets.iterator();
		while(allSubSetIterator.hasNext()){
			HashSet<Attribute> tempSet = allSubSetIterator.next();
			Iterator<Attribute> tempSetIterator = tempSet.iterator();
			while(tempSetIterator.hasNext()){
				
			}
			
		}
		return null;
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

		// // Test
		// System.out.println("Closure Function");
		// System.out.println("AttributeSet: " + closure.toString());

		do {
			closureSize = closure.size();
			for (FunctionalDependency dependency : functionalDependencies) {
				AttributeSet independent = dependency.independent();
				AttributeSet dependent = dependency.dependent();
				// Get independent's iterator
				Iterator<Attribute> attributeIterator = independent.iterator();
				int containsAttributes = 0;
				// If independent contains more elements, independent cannot be
				// included in the closure
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
				// If all attributes are contained in the independent, add all
				// attributes from dependent
				if (containsAttributes == independent.size()) {
					attributeIterator = dependent.iterator();
					while (attributeIterator.hasNext()) {
						Attribute nextDependentAttribute = attributeIterator.next();
						closure.addAttribute(nextDependentAttribute);
					}
				}

			}

		} while (closureSize != closure.size());

		// // Test
		// System.out.println("--After: " + closure.toString());
		return closure;
	}
}
