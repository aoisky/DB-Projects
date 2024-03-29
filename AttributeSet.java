import java.util.*;

/**
 * An unordered set of Attributes. This could very easily be a Java collection,
 * but an important operation (namely examining the powerset) is not easily done
 * with the Java collection.
 **/
public class AttributeSet {

	//a list of the backing attributes
	private final List<Attribute> _attributes;

	//construct an empty AttributeSet
	public AttributeSet() {
		_attributes = new ArrayList<>();
	}

	//copy constructor
	public AttributeSet(AttributeSet other) {
		_attributes = new ArrayList<>(other._attributes);
	}

	public void addAttribute(Attribute a) {
		if(!_attributes.contains(a))
			_attributes.add(a);
	}

	public boolean contains(Attribute a) {
		return _attributes.contains(a);
	}

	public int size() {
		return _attributes.size();
	}

	public boolean equals(Object other) {
		if(other == null || !(other instanceof AttributeSet)){
			return false;
		}
		//TODO: you should probably implement this
		AttributeSet otherSet = (AttributeSet)other;
		// Check the size of two sets
		if (otherSet.size() != this.size()) {
			return false;
		} else {
			// Compare two sets
			Set<Attribute> set = new HashSet<>();
			Iterator<Attribute> attributeIterator = otherSet.iterator();
			// Put other's attributes to a hashSet
			while (attributeIterator.hasNext()) {
				set.add(attributeIterator.next());
			}
			attributeIterator = this.iterator();
			// Check if every element in the set
			while (attributeIterator.hasNext()) {
				Attribute temp = attributeIterator.next();
				if (!set.contains(temp)) {
					return false;
				} else {
					set.remove(temp);
				}
			}
			return true;
		}
	}

	public Iterator<Attribute> iterator() {
		return _attributes.iterator();
	}

	public String toString() {
		String out = "";
		Iterator<Attribute> iter = iterator();
		while(iter.hasNext())
			out += iter.next() + "\t";

		return out;
	}
	
	// Delete an attribute if it contains A
	public void deleteAttribute(Attribute a) {
		if(_attributes.contains(a))
			_attributes.remove(a);
	}
}
