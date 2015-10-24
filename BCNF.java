import java.util.*;

public class BCNF {

	/**
	 * Implement your algorithm here
	 **/
	// Reorganize input and call mydecompose helper function
	public static HashSet<AttributeSet> decompose(AttributeSet attributeSet,
			Set<FunctionalDependency> functionalDependencies) {

		HashSet<FunctionalDependency> myDependencies = new HashSet<FunctionalDependency>();
		Iterator<FunctionalDependency> depIterator = functionalDependencies.iterator();

		while (depIterator.hasNext()) {
			FunctionalDependency dep = depIterator.next();
			AttributeSet dependent = dep.dependent();
			AttributeSet independent = dep.independent();
			if (dependent.size() > 1) {
				Iterator<Attribute> dependentIterator = dependent.iterator();
				while (dependentIterator.hasNext()) {
					AttributeSet myNewDependentSet = new AttributeSet();
					myNewDependentSet.addAttribute(dependentIterator.next());
					FunctionalDependency newFd = new FunctionalDependency(independent, myNewDependentSet);
					myDependencies.add(newFd);
				}
			} else {
				myDependencies.add(dep);
			}

		}
		return myDecompose(attributeSet, myDependencies);

	}

	// function to generate all subsets by calling allSubHelper
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

	// helper function for finding all subsets
	public static void allSubHelper(ArrayList<Attribute> myList, AttributeSet newList, int index,
			HashSet<AttributeSet> result) {
		if (index >= myList.size()) {
			if (newList == null || newList.size() == 0) {
				return;
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

	//
	public static HashSet<AttributeSet> myDecompose(AttributeSet attributeSet,
			Set<FunctionalDependency> functionalDependencies) {
		// If attribute set is null or functional dependencies are null

		HashSet<AttributeSet> result = new HashSet<AttributeSet>();
		HashSet<AttributeSet> resultTwo = new HashSet<AttributeSet>();
		if (attributeSet == null || functionalDependencies == null) {
			result.add(attributeSet);
			return result;
		}
		boolean flag = true;

		HashSet<AttributeSet> allSubSets = findSubset(attributeSet);
		HashSet<FunctionalDependency> myDependencies = new HashSet<FunctionalDependency>(functionalDependencies);
		Iterator<FunctionalDependency> depIterator = myDependencies.iterator();
		while (depIterator.hasNext()) {
			FunctionalDependency dep = depIterator.next();
			AttributeSet independent = dep.independent();
			Iterator<Attribute> independentIterator = independent.iterator();
			AttributeSet dependent = dep.dependent();
			Iterator<Attribute> dependentIterator = dependent.iterator();
			// For each attribute A in W
			while (true) {
				if (independentIterator.hasNext()) {
					Attribute independentAttribute = independentIterator.next();
					if (!attributeSet.contains(independentAttribute)) {
						depIterator.remove();
						break;
					}
				} else if (dependentIterator.hasNext()) {
					Attribute dependentAttribute = dependentIterator.next();
					if (!attributeSet.contains(dependentAttribute)) {
						depIterator.remove();
						break;
					}
				} else {
					break;
				}
			}
		}
		Iterator<FunctionalDependency> testdepIterator = myDependencies.iterator();

		Iterator<AttributeSet> allSubSetIterator = allSubSets.iterator();
		// For all SubSets
		while (allSubSetIterator.hasNext()) {

			// Get a Subset
			AttributeSet mySet = allSubSetIterator.next();
			// Find its closure
			AttributeSet closuredSet = closureMethod(mySet, myDependencies);
			// Check if the closure is the same as the original
			// if not equal
			if (!closuredSet.equals(mySet) && !closuredSet.equals(attributeSet)) {
				flag = false;

				// Find the complement
				Iterator<Attribute> closuredSetIterator = closuredSet.iterator();
				Iterator<Attribute> mySetIterator = mySet.iterator();
				AttributeSet newAttributes = new AttributeSet(attributeSet);
				while (closuredSetIterator.hasNext()) {
					newAttributes.deleteAttribute(closuredSetIterator.next());
				}
				while (mySetIterator.hasNext()) {
					newAttributes.addAttribute(mySetIterator.next());
				}

				result = decompose(closuredSet, myDependencies);
				resultTwo = decompose(newAttributes, myDependencies);
				for (AttributeSet temp : resultTwo) {
					boolean myflag = true;
					for (AttributeSet exist : result) {
						if (temp.equals(exist)) {
							myflag = false;
							break;
						}
					}
					if (myflag) {
						result.add(temp);
					}
				}

				break;
			}
		}
		if (flag) {

			result.add(attributeSet);
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

		return closure;
	}

	// Another implemented closure algorithm on the textbook
	public static AttributeSet closureMethod(AttributeSet attributeSet,
			Set<FunctionalDependency> functionalDependencies) {
		if (attributeSet == null || functionalDependencies == null) {
			return null;
		}

		// Initialization
		Map<Attribute, List<FunctionalDependency>> listMap = new HashMap<>();
		Map<FunctionalDependency, Integer> countMap = new HashMap<>();
		// For each FD in F
		for (FunctionalDependency dependency : functionalDependencies) {
			AttributeSet independent = dependency.independent();
			countMap.put(dependency, independent.size());
			Iterator<Attribute> independentIterator = independent.iterator();
			// For each attribute A in W
			while (independentIterator.hasNext()) {
				Attribute independentAttribute = independentIterator.next();
				if (listMap.containsKey(independentAttribute)) {
					List<FunctionalDependency> fdList = listMap.get(independentAttribute);
					fdList.add(dependency);
				} else {
					List<FunctionalDependency> fdList = new ArrayList<>();
					fdList.add(dependency);
					listMap.put(independentAttribute, fdList);
				}
			}
		}

		AttributeSet newDependency = new AttributeSet(attributeSet);
		AttributeSet update = new AttributeSet(attributeSet);

		// Computation
		while (update.size() > 0) {
			Iterator<Attribute> updateIterator = update.iterator();
			// Choose a value from update
			Attribute updateAttr = updateIterator.next();
			// Update = Update - A
			updateIterator.remove();
			List<FunctionalDependency> fdList = listMap.get(updateAttr);
			// If update attr is not in the functional dependency, just
			// continue;
			if (fdList == null)
				continue;
			// For each FD in A
			for (FunctionalDependency fd : fdList) {

				countMap.put(fd, countMap.get(fd) - 1);
				if (countMap.get(fd) == 0) {
					AttributeSet addSet = new AttributeSet(fd.dependent());

					Iterator<Attribute> newDependencyIterator = newDependency.iterator();
					while (newDependencyIterator.hasNext()) {
						Attribute newDependencyAttr = newDependencyIterator.next();
						if (addSet.contains(newDependencyAttr)) {
							// Delete attribute
							addSet.deleteAttribute(newDependencyAttr);
						}
					}
					// New dependency = new dependency U Add
					// Update = Update U Add
					Iterator<Attribute> addSetIterator = addSet.iterator();
					while (addSetIterator.hasNext()) {
						Attribute addAttribute = addSetIterator.next();
						newDependency.addAttribute(addAttribute);
						update.addAttribute(addAttribute);
					}
				}
			}
		}
		// Test

		return newDependency;
	}
}
