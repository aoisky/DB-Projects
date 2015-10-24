import java.util.*;

public class BCNF {

	/**
	 * Implement your algorithm here
	 **/

	public static HashSet<AttributeSet> findSubset(AttributeSet attributeSet) {
		HashSet<AttributeSet> result = new HashSet<>();

		ArrayList<Attribute> myList = new ArrayList<>();
		Iterator<Attribute> attrIterator = attributeSet.iterator();
		while (attrIterator.hasNext()) {
			myList.add(attrIterator.next());
		}
		AttributeSet newList = new AttributeSet();
		allSubHelper(myList, newList, 0, result);

		return result;
	}

	public static void allSubHelper(ArrayList<Attribute> myList, AttributeSet newList, int index, HashSet<AttributeSet> result) {
		if (index >= myList.size()) {
			if (newList == null || newList.size() == 0){
				
			} else {
				result.add(newList);
			}
		} else {
			allSubHelper(myList, newList, index + 1, result);
			AttributeSet newListAdded = new AttributeSet(newList);
			newListAdded.addAttribute(myList.get(index));
			allSubHelper(myList, newListAdded, index + 1, result);
		}
	}

	public static Set<AttributeSet> decompose(AttributeSet attributeSet,
			Set<FunctionalDependency> functionalDependencies) {
		// If attribute set is null or functional dependencies are null
		if (attributeSet == null || functionalDependencies == null) {
			return null;
		}
		Set<AttributeSet> result = new HashSet<AttributeSet>();
		//test
		System.out.println("calling findSubset with : " + attributeSet.toString());
		HashSet<AttributeSet> allSubSets = findSubset(attributeSet);
		System.out.println("Result is : " + allSubSets.toString());
		
		Iterator<AttributeSet> allSubSetIterator = allSubSets.iterator();
		// For all SubSets
		while (allSubSetIterator.hasNext()) {
			
			// Test
			System.out.println("hello");
			// Get a Subset
			AttributeSet mySet = allSubSetIterator.next();
			// Find its closure
			AttributeSet closuredSet = closure(mySet, functionalDependencies);
			// Check if the closure is the same as the original
			// if not equal
			if (!closuredSet.equals(mySet)) {
				
				allSubSets.add(closuredSet);
				//Find the complement
				Iterator<Attribute> closuredSetIterator = closuredSet.iterator();
				Iterator<Attribute> mySetIterator = mySet.iterator();
				AttributeSet newAttributes = new AttributeSet(attributeSet);
				while (closuredSetIterator.hasNext()){
					newAttributes.deleteAttribute(closuredSetIterator.next());
				}
				while (mySetIterator.hasNext()){
					newAttributes.addAttribute(mySetIterator.next());
				}
				allSubSets.add(newAttributes);
			}
		}
		return result;
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

		 // Test
		 System.out.println("Closure Function");
		 System.out.println("Given: " + closure.toString());

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

		 // Test
		 System.out.println("After: " + closure.toString());
		return closure;
	}
}
